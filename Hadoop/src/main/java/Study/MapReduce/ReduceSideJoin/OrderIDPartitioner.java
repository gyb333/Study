package Study.MapReduce.ReduceSideJoin;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderIDPartitioner extends Partitioner<Text, CompareKey> {

    /**
     * Get the partition number for a given key (hence record) given the total
     * number of partitions i.e. number of reduce-tasks for the job.
     *
     * <p>Typically a hash function on a all or a subset of the key.</p>
     *
     * @param text          the key to be partioned.
     * @param compareKey    the entry value.
     * @param numPartitions the total number of partitions.
     * @return the partition number for the <code>key</code>.
     */
    @Override
    public int getPartition(Text text, CompareKey compareKey, int numPartitions) {
        return Integer.parseInt(text.toString().substring(5))%numPartitions;
    }
}
