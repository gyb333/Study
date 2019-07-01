package MapReduce.ReduceSideJoin;

import HDFS.HDFSHelper;
import MapReduce.DistributedJob;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Join操作在reduce task中完成
 * <p>
 * 适合两个大表的连接操作
 * <p>
 * reduce端对2个集合做乘积计算，很耗内存，容易导致OOM。
 */
public class ReduceJoin extends DistributedJob {
    private static final Boolean isLocaltion = false;

    public static void main(String[] args) throws Exception {
        ReduceJoin mj = new ReduceJoin() {
            @Override
            public void setJobConfig(Job job) {
                //设置缓存数据
                try {
                    String strLocalPath = getStrLocalInput().replace("ReduceJoin", "JoinCache");
                    String strPath = "file:///" + strLocalPath;
                    if (!isLocaltion) {
                        String strInputPath = getStrInput().replace("ReduceJoin", "JoinCache");
                        strPath = strInputPath;
                        FileSystem fs = null;
                        try {
                            fs = FileSystem.get(job.getConfiguration());
                            HDFSHelper.uploadFile2HDFS(fs, strInputPath, strLocalPath, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fs != null) {
                                try {
                                    fs.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    String strUserPath = strPath + "/user";
                    job.addCacheFile(new URI(strUserPath));
                    job.setPartitionerClass(OrderIDPartitioner.class);
                    job.setNumReduceTasks(2);

                    job.setGroupingComparatorClass(GroupingComparator.class);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        };

        mj.execJob("ReduceJoin", isLocaltion, JoinMap.class, JoinReduce.class
                , Text.class, CompareKey.class, CompareKey.class, NullWritable.class
                , false);

    }


    public static class JoinMap extends Mapper<LongWritable, Text, Text, CompareKey> {
        private String fileName;
        private CompareKey compkey = new CompareKey();
        private Text mkey = new Text();

        private Map<String, CompareKey> mapUser = new ConcurrentHashMap<>();
        private boolean isFirst = true;


        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //获取文件名称
            InputSplit split = context.getInputSplit();
            FileSplit filesplit = (FileSplit) split;
            fileName = filesplit.getPath().getName();

            if (!isFirst) return;
            isFirst = false;
            //加载 小文件件fs.getPath()
            String path;
            Configuration conf = context.getConfiguration();
//            conf.setBoolean("fs.hdfs.impl.disable.cache", true);

            //获取缓存文件
            for (URI uri : context.getCacheFiles()) {
                path = uri.getPath();
                if (!path.contains("user")) continue;
                if (!isLocaltion) {
                    FileSystem fs = FileSystem.newInstance(conf);
                    HDFSHelper.DownloadFile(fs, path, path);
                    fs.close();
                }
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
                BufferedReader br = new BufferedReader(new InputStreamReader(bis));
                String res = null;
                while ((res = br.readLine()) != null) {
                    String[] splits = res.split(",");
                    if (path.contains("user")) {
                        String userId = splits[0];
                        CompareKey ck = new CompareKey("", userId, ReduceSideFlag.User, res);
                        mapUser.put(userId, ck);
                    }
                }


                br.close();
                bis.close();


            }


        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(",");
            String orderId = words[0];
            String userId = words[1];

            if (fileName.contains("order1") || fileName.contains("order2")) {
                CompareKey user = mapUser.get(userId);
                compkey.set(orderId, userId, ReduceSideFlag.Order, user.getInfo());
            } else if (fileName.contains("OrderDetail")) {
                compkey.set(orderId, "", ReduceSideFlag.OrderDetail, line);
            }
            mkey.set(orderId);
            context.write(mkey, compkey);

        }
    }

    public static class JoinReduce extends Reducer<Text, CompareKey, CompareKey, NullWritable> {
        private CompareKey compkey = new CompareKey();
        private NullWritable nw = NullWritable.get();
        private List<CompareKey> orderList = new ArrayList<>();
        private List<CompareKey> orderDetailList = new ArrayList<>();


        protected void reduce(Text key, Iterable<CompareKey> values, Context context) throws IOException, InterruptedException {
            String info = "";
            orderList.clear();
            orderDetailList.clear();
            for (CompareKey each : values) {
                CompareKey ck = new CompareKey();
                try {
                    BeanUtils.copyProperties(ck, each);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
//                   List<CompareKey> orders=orderList.stream()
//                           .filter(f->f.getFlag()==ReduceSideFlag.Order)
//                           .collect(Collectors.toList());
                if (each.getFlag() == ReduceSideFlag.Order)
                    orderList.add(ck);
                else if (each.getFlag() == ReduceSideFlag.OrderDetail)
                    orderDetailList.add(ck);
            }
            String str = "";
            for (CompareKey each : orderList) {
                try {
                    BeanUtils.copyProperties(compkey, each);
                    str = compkey.getInfo();
                    for (CompareKey od : orderDetailList) {
                        info = str + " OrderDetail:" + od.getInfo();
                        BeanUtils.copyProperty(compkey, "info", info);
                        context.write(compkey, nw);

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


            }


        }
    }

}
