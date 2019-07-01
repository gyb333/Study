package Serializer;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * 持久化数据到磁盘
 * 作为通信数据格式传输
 */
public class SerializationBean implements WritableComparable<SerializationBean>, Serializable {
    private static final long serialVersionUID = 1139250871118514325L;

    @Override
    public int compareTo(SerializationBean o) {
        return userID.compareTo(o.userID);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(userID);
        out.writeUTF(userName);
        out.writeInt(age);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        userID=in.readUTF();
        userName=in.readUTF();
        age=in.readInt();
    }

    public SerializationBean(){

    }

    public SerializationBean(String userID,String userName,int age){
        set(userID,userName,age);
    }

    public void set(String userID,String userName,int age){
        this.userID=userID;
        this.userName=userName;
        this.age=age;

    }

    @Override
    public String toString() {
        return "UserBean [userID=" + userID + ", userName=" + userName + ", age="
                + age  + "]";
    }

    private String userID;
    private String userName;
    private int age;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
