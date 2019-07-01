package Study;


import Serializer.*;
import org.junit.Test;

import java.io.*;

public class SerializationBeanTest {


    private  String path="D:\\test";

    @Test
    public void serializerTest() throws IOException, ClassNotFoundException {
        SerializationBean sb =new SerializationBean("UserID","UserName",10);
        File file = new File(path);
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(SerializerUtils.serializer(sb));
        fos.close();
    }


    @Test
    public void deserializerTest() throws IOException, ClassNotFoundException {
        byte[] bytes= FileUtils.toByteArray(path);
        SerializationBean sb =SerializerUtils.deserializer(bytes);
        System.out.println(sb);
    }

    @Test
    public void serializerHdoopTest() throws IOException, ClassNotFoundException {
        SerializationBean sb =new SerializationBean("UserID","UserName",10);
        File file = new File(path);
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(WritableUtil.serializer(sb));
        fos.close();
    }

    @Test
    public void deserializerHdoopTest() throws IOException, ClassNotFoundException {
        byte[] bytes=FileUtils.toByteArray(path);
        SerializationBean sb = WritableUtil.deserializer(bytes,SerializationBean.class);
        System.out.println(sb);
    }


    @Test
    public void serializerProtostuffTest() throws IOException, ClassNotFoundException {
        SerializationBean sb =new SerializationBean("UserID","UserName",10);
        File file = new File(path);
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(ProtostuffUtil.serializer(sb));
        fos.close();
    }

    @Test
    public void deserializerProtostuffTest() throws IOException, ClassNotFoundException {
        byte[] bytes=FileUtils.toByteArray(path);
        SerializationBean sb = ProtostuffUtil.deserializer(bytes,SerializationBean.class);
        System.out.println(sb);
    }

    @Test
    public void serializerFastJsonTest() throws IOException, ClassNotFoundException {
        SerializationBean sb =new SerializationBean("UserID","UserName",10);
        File file = new File(path);
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(FastJsonUtil.serializer(sb));
        fos.close();
    }
    @Test
    public void deserializerFastJsonTest() throws IOException, ClassNotFoundException {
        byte[] bytes=FileUtils.toByteArray(path);
        SerializationBean sb =FastJsonUtil.deserializer(bytes,SerializationBean.class);
        System.out.println(sb);
    }
}
