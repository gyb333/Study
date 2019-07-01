package MapReduce.Example;

import MapReduce.DistributedJob;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 输入
 * A:B,C,D,F,E,O
 * B:A,C,E,K
 * C:F,A,D,I
 * D:A,E,F,L
 * 输出
 * B-C：A
 **/

/**
 * 输入
 * A:B,C,D,F,E,O
 * B:A,C,E,K
 * C:F,A,D,I
 * D:A,E,F,L
 *
 * 输出
 *B A
 *C A
 * */


public class CommonFriendsOne extends DistributedJob {

    public static class CommonFriendsOneMapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text mKey = new Text();
        private Text friend = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(":");
            friend.set(words[0]);
            String[] friends = words[1].split(",");
            for (String each : friends) {
                mKey.set(each);
                context.write(mKey, friend);
            }
        }
    }

    /**
     * [B, C, D, F, G, H, I, K, O]
     * B-C:A
     * B-D:A
     * B-F:A
     * B-G:A
     * B-H:A
     * B-I:A
     * B-K:A
     * B-O:A
     * C-D:A
     * C-F:A
     * C-G:A
     * C-H:A
     * C-I:A
     * C-K:A
     * C-O:A
     */
    public static class CommonFriendsOneReducer extends Reducer<Text, Text, Text, Text> {
        private Text common=new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            ArrayList<String> list = new ArrayList<>();
            for (Text each : values) {
                list.add(each.toString());
            }
            Collections.sort(list);
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    common.set(list.get(i) + "-" + list.get(j));
                    context.write(common, key);
                }
            }

        }
    }

    public static void main(String[] args) throws Exception {
        String clsName = CommonFriendsOne.class.getSimpleName();
        boolean isLocaltion = true;
        Class<? extends Mapper> clsMapper = CommonFriendsOneMapper.class;
        Class<? extends Reducer> clsReducer = CommonFriendsOneReducer.class;
        Class<?> clsMapOutputKey = Text.class;
        Class<?> clsMapOutputValue = Text.class;
        Class<?> clsOutputKey = Text.class;
        Class<?> clsOutputValue = Text.class;
        new CommonFriendsOne().execJob(clsName, isLocaltion, clsMapper, clsReducer,
                clsMapOutputKey, clsMapOutputValue, clsOutputKey, clsOutputValue
                , true);

    }

}
