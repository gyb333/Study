package Study.Serializer;

import org.apache.hadoop.io.Writable;

import java.io.*;

public class SerializerUtils {
    public static <T> byte[] serializer(T t) throws IOException {
        //对象转成字节码
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(t);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        outputStream.close();
        return bytes;
    }

    public static <T> T deserializer(byte[] bytes) throws IOException, ClassNotFoundException {
        //字节码转换成对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        T t = (T) inputStream.readObject();
        inputStream.close();
        return t;

    }




}
