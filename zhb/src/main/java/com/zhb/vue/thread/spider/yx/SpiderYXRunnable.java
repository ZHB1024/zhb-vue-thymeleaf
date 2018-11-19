package com.zhb.vue.thread.spider.yx;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.pojo.YXInfoData;
import com.zhb.vue.service.ServiceFactory;
import com.zhb.vue.service.YXInfoService;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月31日下午12:04:18
*/

public class SpiderYXRunnable implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(SpiderYXRunnable.class);
    
    private int beginPage;
    private int endPage ;
    private int per ;
    private String baseUrl = null;
    private String createUserId;
    
    private YXInfoService yxInfoService = ServiceFactory.getYXInfoService();

    public SpiderYXRunnable(int beginPage,int endPage,int per,String baseUrl,String createUserId) {
        this.beginPage = beginPage;
        this.endPage = endPage;
        this.per = per;
        this.baseUrl = baseUrl;
        this.createUserId = createUserId;
    }

    @Override
    public void run() {
        List<YXInfoData> result = new ArrayList<>();
        String targetUrl = null;
        for(int i = beginPage-1 ;i<endPage;i++) {
            targetUrl = baseUrl + i*per + ".dhtml";
            Document doc = JsoupUtil.getDocumentByUrl(targetUrl);
            if (null != doc) {
                Elements divs = doc.getElementsByClass("ch-table");
                if (null != divs) {
                    for (Element element : divs) {
                        Elements trs = element.select("tr");
                        if (null == trs) {
                            continue;
                        }
                        for (Element tr : trs) {
                            Elements tds = tr.select("td");
                            if (null == tds) {
                                continue;
                            }
                            if (tds.size() > 0) {
                                YXInfoData data = new YXInfoData();
                                data.setCreateUserId(createUserId);
                                int k=1;
                                for (Element td : tds) {
                                    if (null != td.text()&& !"".equals(td.text())) {
                                        setValue(k, td.text(), data);
                                    }
                                    k++;
                                }
                                result.add(data);
                            }
                        }
                    }
                }
            }
        }
        if (result.size() > 0) {
            yxInfoService.batchSave(result);
            logger.info("批量添加院校信息 " + result.size() + " 个");
        }
    }
    
    private void setValue(int k,String value,YXInfoData data) {
        switch (k) {
        case 1:
            data.setName(value);
            break;
        case 2:
            data.setCity(value);
            break;
        case 3:
            data.setBelong(value);
            break;
        case 4:
            data.setType(value);
            break;
        case 5:
            data.setXlcc(value);
            break;
        case 6:
            if (value.contains("985")) {
                data.setIs985(1);
            }
            if (value.contains("211")) {
                data.setIs211(1);
            }
            break;
        case 7:
            data.setIsYJSY(1);
            break;
        case 8:
            data.setPoint(value);
            break;

        default:
            break;
        }
    }

}


