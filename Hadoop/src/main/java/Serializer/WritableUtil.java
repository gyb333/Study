package Serializer;

import org.apache.hadoop.io.Writable;

import java.io.*;

public class WritableUtil {

    public static byte[] serializer(Writable writable) throws IOException {
        //创建一个输出字节流对象
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataout = new DataOutputStream(out);

        //将结构化数据的对象writable写入到输出字节流。
        writable.write(dataout);
        return out.toByteArray();
    }

    public static <T extends Writable> T deserializer(byte[] bytes,Class<T> c)  {
        T t = null;
        try {
            t = c.newInstance();
            //创建一个输入字节流对象，将字节数组中的数据，写入到输入流中
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            DataInputStream datain = new DataInputStream(in);
            //将输入流中的字节流数据反序列化
            t.readFields(datain);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
}
