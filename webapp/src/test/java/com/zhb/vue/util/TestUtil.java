package com.zhb.vue.util;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.StringUtil;


public class TestUtil {
    
    @Test
    @Transactional
    @Rollback(true)
    public void saveOrUpdateTest() {
        int num = 43;
        int result = 49/10;
        StringUtil.println(result+"");
    }
    
    //@Test
    @Transactional
    @Rollback(true)
    public void downLoadFromUrl() {
        try {
            String url = "http://www.mtl018.com/data/attachment/forum/201503/25/151647kn2lb563cgl3gn55.jpg";
            DownloadUtil.downLoadFromUrl(url, "test.jpg", "D:/java/attachment/upload/original");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    //@Test
    @Transactional
    @Rollback(true)
    public void getChildUrlFromUrl() {
        String url = PropertyUtil.getSpiderUrl();
        String urlTarget = PropertyUtil.getSpiderUrlTarget();
        Integer totalPage = PropertyUtil.getSpiderTotalPage();
        String totalUrl = url + urlTarget + totalPage + ".html";
        
        Document doc = JsoupUtil.getDocumentByUrl(totalUrl);
        if (null == doc) {
            return;
        }
        Element main = doc.getElementById("moderate");
        Elements liEle = main.getElementsByTag("li");
        for (Element link : liEle) {
            Elements links = link.getElementsByClass("z");
            System.out.println(links.get(0).attr("abs:href"));
        }
    }
    //@Test
    @Transactional
    @Rollback(true)
    public void getImgUrlFromUrl() {
        
        Document doc = JsoupUtil.getDocumentByUrl("http://www.mtl018.com/thread-1996-1-156.html");
        if (null == doc) {
            return;
        }
        Elements main = doc.getElementsByClass("t_f");
        for (Element link : main) {
            Elements hrefs = link.select("img[src]");
            for (Element element : hrefs) {
                System.out.println(element.attr("abs:src"));
            }
                
        }
    }

}
