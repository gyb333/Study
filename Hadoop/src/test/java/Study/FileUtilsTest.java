package Study;

import HDFS.FileUtils;
import org.junit.Test;

import java.io.File;

public class FileUtilsTest {

    @Test
    public void GetFiles() {
        String path="D:\\work\\Study\\bigdata\\input\\InvertedIndex";
        File[] files = FileUtils.GetFiles(path);
        for(File file :files){
            System.out.println(file.getPath());
            System.out.println(file.getName());
        }

    }
}
