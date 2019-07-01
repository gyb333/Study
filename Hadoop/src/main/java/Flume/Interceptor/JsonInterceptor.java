package Flume.Interceptor;

import com.alibaba.fastjson.JSONObject;
import org.apache.flume.Context;
import org.apache.flume.Event;

import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.source.taildir.TaildirSource;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *   查看RegexFilteringInterceptor  TimestampInterceptor
 */
public class JsonInterceptor implements Interceptor {
    private String[] schema; //id,name,age,fv
    private String separator; //,

    public JsonInterceptor(String schema, String separator) {
        this.schema = schema.split("[,]");
        this.separator = separator;
    }


    public void initialize() {
        //no-op
    }


    public Event intercept(Event event) {
        Map<String, String> tuple = new LinkedHashMap<String, String>();
        //将传入的Event中的body内容，加上schema，然后在放入到Event
        String line = new String(event.getBody());
        String[] fields = line.split(separator);
        for(int i = 0; i < schema.length; i++) {
            String key = schema[i];
            String value = fields[i];
            tuple.put(key, value);
        }
        String json = JSONObject.toJSONString(tuple);
        //将转换好的json，再放入到Event中
        event.setBody(json.getBytes());
        return event;
    }


    public List<Event> intercept(List<Event> events) {
        Iterator var2 = events.iterator();
        while(var2.hasNext()) {
            Event event = (Event)var2.next();
            this.intercept(event);
        }

        return events;
    }


    public void close() {
        //no-op
    }


    /**
     * Interceptor.Builder的生命周期方法
     * 构造器 -> configure -> build
     */
    public static class Builder implements Interceptor.Builder {

        private String fields;
        private String separator;


        public Interceptor build() {
            //在build创建JsonInterceptor的实例
            return new JsonInterceptor(fields, separator);
        }

        /**
         * 配置文件中应该有哪些属性？
         * 1.数据的分割符
         * 2.字段名字（schema）
         * 3.schema字段的分隔符
         * @param context
         */
        public void configure(Context context) {
            fields = context.getString("fields");
            separator = context.getString("separator");
        }
    }
}
