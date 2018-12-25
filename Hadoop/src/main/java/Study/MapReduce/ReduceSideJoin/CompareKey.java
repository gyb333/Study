package Study.MapReduce.ReduceSideJoin;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CompareKey implements WritableComparable<CompareKey> {


    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderID);
        out.writeUTF(userID);
        out.writeUTF(flag.toString());
        out.writeUTF(info);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        orderID=in.readUTF();
        userID=in.readUTF();
        flag=ReduceSideFlag.valueOf(in.readUTF());
        info=in.readUTF();
    }


    @Override
    public int compareTo(CompareKey o) {
        int result=orderID.compareTo(o.orderID);
        if(result==0){
            result=userID.compareTo(o.userID);
            if(result==0){
                result=flag.compareTo(o.flag);
                if(result==0)
                    result=info.compareTo(o.info);
            }
        }
        return result;
    }

    public CompareKey() {
        set();
    }
    public CompareKey(String orderID, String userID, ReduceSideFlag flag, String info) {
        set(orderID,userID,flag,info);
    }

    public void set(String orderID, String userID, ReduceSideFlag flag, String info) {
        this.orderID = orderID;
        this.userID = userID;
        this.flag = flag;
        this.info = info;
    }
    public void set() {
        this.orderID = "";
        this.userID = "";
        this.flag = ReduceSideFlag.Order;
        this.info = "";
    }

    @Override
    public String toString() {
        return
                "orderID='" + orderID + '\'' +
                ", userID='" + userID + '\'' +
                ", flag=" + flag +
                ", info='" + info + '\'' +
                '}';
    }

    private String orderID;
    private String userID;
    private ReduceSideFlag flag;
    private String info;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ReduceSideFlag getFlag() {
        return flag;
    }

    public void setFlag(ReduceSideFlag flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
