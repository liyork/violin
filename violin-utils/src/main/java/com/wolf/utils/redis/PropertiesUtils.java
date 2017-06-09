package com.wolf.utils.redis;

import java.io.InputStream;
import java.util.Properties;

/**

 */
public class PropertiesUtils {

    public static Properties  getProperties(String propertiesName) {
        InputStream fis = null;
        Properties properties = null;
        try {
            fis = PropertiesUtils.class.getResourceAsStream("/" + propertiesName + ".properties");
            if (fis != null) {
                properties = new Properties();
                properties.load(fis);
            }
        } catch (Exception e) {

        } finally {
            try{
                fis.close();
            } catch(Exception e) {

            }
        }
        return properties;
    }

    public static Properties  getPropertiesWithOutSuffix(String propertiesName) {
        InputStream fis = null;
        Properties properties = null;
        try {
            fis = PropertiesUtils.class.getResourceAsStream("/" + propertiesName );
            if (fis != null) {
                properties = new Properties();
                properties.load(fis);
            }
        } catch (Exception e) {

        } finally {
            try{
                fis.close();
            } catch(Exception e) {

            }
        }
        return properties;
    }

}
