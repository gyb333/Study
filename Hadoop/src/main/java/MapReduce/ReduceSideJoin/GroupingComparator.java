package MapReduce.ReduceSideJoin;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupingComparator extends WritableComparator {

    //由父类创建： 组合键对象,防止null空指针
    public GroupingComparator() {
        super(Text.class, true);
    }

    /**
     *针对map端输出的Key，做分组，归到同一个组为一个reduceTask
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Text a1= (Text) a;
        Text b1= (Text) b;
        int result=a1.compareTo(b1);
        return result;
    }

}
