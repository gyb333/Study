package Base.Common;


import org.apache.log4j.PropertyConfigurator;

import java.io.InputStream;

public class ConfigUtils {



    public static void configureUserPath(){
        String path = System.getProperty("user.dir") + "\\Resources\\";
        PropertyConfigurator.configure(path);
    }

    public static void configureInputStream(){
        InputStream is =ConfigUtils.class.getResourceAsStream("/log4j.properties");
        PropertyConfigurator.configure(is);
    }




}
