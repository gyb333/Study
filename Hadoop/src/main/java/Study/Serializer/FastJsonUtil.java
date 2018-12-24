package Study.Serializer;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FastJsonUtil {

    public static <T>  byte[] serializer(T t) throws IOException, ClassNotFoundException {
       return  JSONObject.toJSONString(t).getBytes();

    }

    public static <T> T deserializer(byte[] bytes,Class<T> c) throws IOException, ClassNotFoundException {
        JSONObject json=JSONObject.parseObject(new String(bytes));
        T t =JSONObject.toJavaObject(json,c);
        return t;
    }
}
