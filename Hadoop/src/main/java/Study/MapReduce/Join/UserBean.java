package Study.MapReduce.Join;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class UserBean implements WritableComparable<UserBean> {
    @Override
    public int compareTo(UserBean o) {
        return userID.compareTo(o.userID);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(userID);
        out.writeUTF(userName);
        out.writeInt(age);
        out.writeUTF(userFriend);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        userID=in.readUTF();
        userName=in.readUTF();
        age=in.readInt();
        userFriend=in.readUTF();
    }
    public UserBean(){
    }

    public UserBean(String userID,String userName,int age,String userFriend){
        set(userID,userName,age,userFriend);
    }

    public void set(String userID,String userName,int age,String userFriend){
        this.userID=userID;
        this.userName=userName;
        this.age=age;
        this.userFriend=userFriend;
    }

    @Override
    public String toString() {
        return "UserBean [userID=" + userID + ", userName=" + userName + ", age="
                + age + ", userFriend=" + userFriend + "]";
    }

    private String userID;
    private String userName;
    private int age;
    private String userFriend;

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

    public String getUserFriend() {
        return userFriend;
    }

    public void setUserFriend(String userFriend) {
        this.userFriend = userFriend;
    }


}
