package com.zhb.vue.thread.spider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * @ClassName: SpiderProperty
 * @description: 
 * @author: 张会彬
 * @Date: 2019年8月4日 上午10:21:53
 */

public class SpiderProperty {
    
    private static final Log log = LogFactory.getLog(SpiderProperty.class);
    
  //spider
    private static String spiderUrl;
    private static String spiderUrlTarget;
    private static Integer spiderTotalPage;
    private static String spiderDownloadPath;
    
    static{
        String propertyPath = System.getenv("propertyPath");
        if (null == propertyPath) {
            log.info("环境变量未配置PropertyPath");
        }else{
            FileInputStream in = null;
            try {
                in = new FileInputStream(propertyPath);
                Properties properties = new Properties();
                properties.load(in);
                
                //spider
                spiderUrl = properties.getProperty("sys.spider.image.url");
                spiderUrlTarget = properties.getProperty("sys.spider.image.url.target");
                spiderTotalPage = Integer.valueOf(properties.getProperty("sys.spider.image.total.page"));
                
                spiderDownloadPath = properties.getProperty("sys.spider.download.path");
                
            } catch (IOException e) {
            }
        }
    }

    public static String getSpiderUrl() {
        return spiderUrl;
    }

    public static Integer getSpiderTotalPage() {
        return spiderTotalPage;
    }

    public static String getSpiderUrlTarget() {
        return spiderUrlTarget;
    }

    public static String getSpiderDownloadPath() {
        return spiderDownloadPath;
    }
    
}
