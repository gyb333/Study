package Study.Serializer;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.*;

/**
 *  // 开启map端输出压缩
 * configuration.setBoolean("mapreduce.map.output.compress", true);
 * // 设置map端输出压缩方式
 * configuration.setClass("mapreduce.map.output.compress.codec", BZip2Codec.class, CompressionCodec.class);
 *
 * 在Reduce输出端采用压缩
 * // 设置reduce端输出压缩开启
 * FileOutputFormat.setCompressOutput(job, true);
 * // 设置压缩的方式
 * FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
 */
public class CompressUtil {

    // compress("e:/test.txt","org.apache.hadoop.io.compress.BZip2Codec");
    public static void compress(String fileName, String method) throws ClassNotFoundException {
        // 1 获取压缩的方式的类
        Class codecClass = Class.forName(method);

        Configuration conf = new Configuration();
        // 2 通过名称找到对应的编码/解码器
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
        // 3 创建压缩文件路径的输入流
        File fileIn = new File(fileName);
        InputStream in = null;
        // 4 该压缩方法对应的文件扩展名
        File fileOut = new File(fileName + codec.getDefaultExtension());
        OutputStream out = null;
        CompressionOutputStream cout = null;

        try {
            in = new FileInputStream(fileIn);
            out = new FileOutputStream(fileOut);

            cout = codec.createOutputStream(out);
            // 5 流对接
            IOUtils.copyBytes(in, cout, 1024 * 1024 * 5, false); // 缓冲区设为5MB

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 6 关闭资源
                if (in != null)
                    in.close();
                if (cout != null)
                    cout.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    //decompres("e:/test.txt.bz2");
    public static void decompres(String filename) throws FileNotFoundException {

        Configuration conf = new Configuration();
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);

        // 1 获取文件的压缩方法
        CompressionCodec codec = factory.getCodec(new Path(filename));

        // 2 判断该压缩方法是否存在
        if (null == codec) {
            System.out.println("Cannot find codec for file " + filename);
            return;
        }
        InputStream cin = null;
        OutputStream out = null;

        try {
            // 3 创建压缩文件的输入流
            cin = codec.createInputStream(new FileInputStream(filename));
            // 4 创建解压缩文件的输出流
            File fout = new File(filename + ".decoded");
            out = new FileOutputStream(fout);
            // 5 流对接
            IOUtils.copyBytes(cin, out, 1024 * 1024 * 5, false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 6 关闭资源
                cin.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
