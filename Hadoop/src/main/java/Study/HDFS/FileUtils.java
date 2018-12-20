package Study.HDFS;

import java.io.File;

public class FileUtils {

    public static  File[] GetFiles(String path){
        File[] files =null;
        File file = new File(path);
        if(file.exists()&&file.isDirectory()){
            files= file.listFiles();
        }
        return  files;
    }



}
