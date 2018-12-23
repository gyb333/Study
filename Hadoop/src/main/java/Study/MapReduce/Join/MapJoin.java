package Study.MapReduce.Join;

import Study.HDFS.HDFSHelper;
import Study.MapReduce.DistributedJob;
import org.apache.commons.lang.ObjectUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapJoin extends DistributedJob {

    private static final Boolean isLocaltion = false;

    public static void main(String[] args) throws Exception {
        MapJoin mj = new MapJoin() {
            @Override
            public void setJobConfig(Job job) {
                //设置缓存数据
                try {
                    String strLocalPath = getStrLocalInput().replace("Join", "JoinCache");
                    String strPath = "file:///" + strLocalPath;


                    if (!isLocaltion) {
                        String strInputPath = getStrInput().replace("Join", "JoinCache");
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
                    String strODPath = strPath + "/OrderDetail";

                    job.addCacheFile(new URI(strUserPath));
                    job.addCacheFile(new URI(strODPath));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        };
        mj.execJob("Join", isLocaltion, JoinMapper.class, null
                , UserBean.class, OrderDetailBean.class, null, null
                , false);
    }

    public static class JoinMapper extends Mapper<LongWritable, Text, UserBean, OrderDetailBean> {

        private String fileName;

        private Map<String, UserBean> mapUser = new ConcurrentHashMap<>();

        private Set<String> usedUser = new HashSet<>();

        private Map<String, List<OrderDetailBean>> mapOrderDetail = new ConcurrentHashMap<>();

        private boolean isFirst = true;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //获取文件名字
            FileSplit fsplit = (FileSplit) context.getInputSplit();
            fileName = fsplit.getPath().getName();
            if (!isFirst) return;
            isFirst = false;
            //加载 小文件件fs.getPath()
            String path;
            Configuration conf =context.getConfiguration();
//            conf.setBoolean("fs.hdfs.impl.disable.cache", true);

            //获取缓存文件
            for (URI uri : context.getCacheFiles()) {
                path = uri.getPath();
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
                        UserBean ub = new UserBean(userId, splits[1], Integer.parseInt(splits[2]), splits[3]);
                        mapUser.put(userId, ub);

                    } else if (path.contains("OrderDetail")) {
                        String orderId = splits[0];
                        OrderDetailBean odb = new OrderDetailBean(splits[0], splits[1],
                                Float.parseFloat(splits[2]), Integer.parseInt(splits[3]));
                        List<OrderDetailBean> list = mapOrderDetail.getOrDefault(orderId, new ArrayList<OrderDetailBean>());
                        list.add(odb);
                        mapOrderDetail.put(orderId, list);
                    }
                }


                br.close();
                bis.close();


            }


        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println(fileName);
            String line = value.toString();
            String[] words = line.split(",");
            String userId = words[1];
            UserBean ub = mapUser.get(userId);
            if (mapOrderDetail.get(words[0]) != null)
                for (OrderDetailBean each : mapOrderDetail.get(words[0])) {
                    context.write(ub, each);
                }
//            删除匹配完的： 用户信息
//            mapUser.remove(userId);
            usedUser.add(userId);

        }


        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            //输出匹配不上的用户

//            取出脏的  用户数据
            if (mapUser == null) return;
            Set<String> keys = mapUser.keySet();
            keys.removeAll(usedUser);
            for (String key : keys) {
                UserBean ub = mapUser.get(key);
                context.write(ub, new OrderDetailBean());
            }

            mapUser.clear();
            mapUser = null;

        }
    }


}
