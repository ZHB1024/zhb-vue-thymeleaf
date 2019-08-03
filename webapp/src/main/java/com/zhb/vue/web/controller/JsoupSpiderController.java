package com.zhb.vue.web.controller;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.dsl.Disruptor;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.forever.mq.disruptor.DisruptorUtil;
import com.zhb.vue.thread.spider.mtsqom.DownloadFromQueueRunnable;
import com.zhb.vue.thread.spider.mtsqom.ReadEndUrlToQueueRunnable;
import com.zhb.vue.thread.spider.mtsqom.disruptor.ConsumerDisruptor;
import com.zhb.vue.thread.spider.mtsqom.disruptor.ProducerDisruptor;
import com.zhb.vue.thread.spider.mtsqom.disruptor.ProducerRunnable;
import com.zhb.vue.thread.spider.qbl.ReadUrlToQueueRunnable;
import com.zhb.vue.thread.spider.yx.SpiderYXRunnable;
import com.zhb.vue.web.util.WebAppUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/htgl/jsoupspidercontroller")
public class JsoupSpiderController {
    
    private Logger logger = LoggerFactory.getLogger(JsoupSpiderController.class);
    
    
    @RequestMapping(value="/toindex",method=RequestMethod.GET)
    public String toSpider(HttpServletRequest request,HttpServletResponse response){
        /*Document doc = JsoupUtil.getDocumentByUrl("http://111av.org/html/tupian/siwa/index_2.html");
        if (null != doc) {
            Elements divs = doc.getElementsByClass("art");
            if (null != divs) {
                for (Element element : divs) {
                    Elements hrefs = element.select("a[href]");
                    if (null == hrefs) {
                        continue;
                    }
                    for (Element a : hrefs) {
                        String href = a.attr("abs:href");
                        System.out.println(href);
                    }
                }
            }
        }*/
        try {
            DownloadUtil.downLoadFromUrl("http://cache1.361lu.com/m3u8/VmN0SkJ3RDRZcjd6T2hzdnI4Wmg2b3UxZ2JsWUZON2F6MEQ1bkhJQ1I0NXh3a3lRem42aWppM1NmTGlvUnA1WjllcTNnck5Zd3VKaU1VK1g4R2dyemdSL0kvU1RIRVk4MGJVTHZWK01LUjJaS1A3dmxRYlpMWVdWK0Z3VjNpRVkxU2tQcFFMa2drMkFaaWt3aks4NUxyOTZhNUJMZ3d2RFhURTZza25qek9vPQ==", "1635.mp4", PropertyUtil.getUploadPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "htgl/spider/index";
    }

    //http://www.mtl018.com/forum-58-1.html
    @RequestMapping(value="/spideryellow",method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData spiderYellow(HttpServletRequest request,HttpServletResponse response){
        AjaxData ajaxData = new AjaxData();
        String userId = WebAppUtil.getUserId(request);
        String url = "http://www.mtl018.com/forum.php?mod=forumdisplay&fid=160&page=";
        String urlTarget = PropertyUtil.getSpiderUrlTarget();

        int beginPage = 1;
        int endPage = 45;
        int totalThread = 10;
        int per = endPage/totalThread;
        ExecutorService es1 = Executors.newFixedThreadPool(totalThread);
        for(int i=0;i<totalThread;i++) {
            if(i != totalThread-1) {
                es1.execute(new ReadEndUrlToQueueRunnable(i+"","queueB-"+(i%totalThread),url,beginPage+(i*per),beginPage+(i*per)+per-1));
            }else {
                es1.execute(new ReadEndUrlToQueueRunnable(i+"","queueB-"+(i%totalThread),url,beginPage+(i*per),endPage));
            }
            
        }
        es1.shutdown();
        
        ExecutorService es2 = Executors.newFixedThreadPool(totalThread*2);
        for(int i=0;i<totalThread*2;i++) {
            es2.execute(new DownloadFromQueueRunnable(i+totalThread+"","queueB-"+(i%totalThread),userId));
        }
        
        es2.shutdown();
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //http://111av.org/html/tupian/siwa/index.html
    @RequestMapping(value="/spideryellow2",method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData spiderYellow2(HttpServletRequest request,HttpServletResponse response){
        AjaxData ajaxData = new AjaxData();
        String userId = WebAppUtil.getUserId(request);
        String url = PropertyUtil.getSpiderUrl();
        Integer totalPage = PropertyUtil.getSpiderTotalPage();
        
        int totalThread = 100;
        int perPage = totalPage/totalThread;
        
        //ExecutorService es = Executors.newFixedThreadPool(totalThread+1);
        ThreadPoolExecutor es = 
                new ThreadPoolExecutor(totalThread, totalThread, 10000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        
        //启动10个线程爬取网页图片链接
        for(int i=1 ;i <= totalThread ;i++) {
            if (i != totalThread) {
                es.execute(new ReadUrlToQueueRunnable(i+"",url,(i-1)*perPage+1,i*perPage,userId));
            }else {
                es.execute(new ReadUrlToQueueRunnable(i+"",url,(i-1)*perPage+1,totalPage,userId));
            }

        }
        
        //启动1个线程读取队列里的链接，并下载保存
        //es.execute(new DownloadQBLFromQueueRunnable(resources1,resources2,userId));
        
        es.shutdown();
        
        ajaxData.setFlag(true);

        return ajaxData;
    }
    
    //https://gaokao.chsi.com.cn/sch/search--ss-on,searchType-1,option-qg,start-20.dhtml
    @RequestMapping(value="/spideryx",method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData spiderYx(HttpServletRequest request,HttpServletResponse response){
        AjaxData ajaxData = new AjaxData();
        String baseUrl = "https://gaokao.chsi.com.cn/sch/search--ss-on,searchType-1,option-qg,start-";
        
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.execute(new SpiderYXRunnable(1, 137, 20, baseUrl,WebAppUtil.getUserId(request)));
        es.shutdown();
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //http://www.mtl018.com/forum-58-1.html
    @RequestMapping(value="/spideryellowbydisruptor",method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData spiderYellowByDisruptor(HttpServletRequest request,HttpServletResponse response){
        AjaxData ajaxData = new AjaxData();
        String userId = WebAppUtil.getUserId(request);
        String url = "http://www.mtl018.com/forum.php?mod=forumdisplay&fid=160&page=";
        String urlTarget = PropertyUtil.getSpiderUrlTarget();

        int beginPage = 1;
        int endPage = 45;
        int totalThread = 10;
        int per = endPage/totalThread;
        
        Disruptor<KeyValueVO> disruptor = DisruptorUtil.getDisruptor();
        ConsumerDisruptor[] consumerDisruptors = new ConsumerDisruptor[totalThread];
        for(int i=0;i < totalThread;i++) {
            consumerDisruptors[i] = new ConsumerDisruptor("消费者-"+(i+1), userId);
        }
        disruptor.handleEventsWithWorkerPool(consumerDisruptors);
        disruptor.start();
        
        ExecutorService es = Executors.newFixedThreadPool(totalThread);
        for(int i=0;i<totalThread;i++) {
            if(i != totalThread-1) {
                ProducerDisruptor producer = new ProducerDisruptor(disruptor.getRingBuffer(),"生产者"+(i+1) ,beginPage+(i*per),beginPage+(i*per)+per-1,url);
                es.execute(new ProducerRunnable(producer));
            }else {
                ProducerDisruptor producer = new ProducerDisruptor(disruptor.getRingBuffer(),"生产者"+(i+1) ,beginPage+(i*per),endPage,url);
                es.execute(new ProducerRunnable(producer));
            }
        }
        es.shutdown();
        disruptor.shutdown();
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //https://gaokao.chsi.com.cn/sch/search--ss-on,searchType-1,option-qg,start-20.dhtml
    @RequestMapping(value="/spidervcg",method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData spiderVCG(HttpServletRequest request,HttpServletResponse response){
        AjaxData ajaxData = new AjaxData();
        String baseUrl = "https://www.vcg.com/creative/search?page=2&phrase=风景";
        
        Document document = JsoupUtil.getDocumentByUrl(baseUrl);
        if (null != document) {
            Elements elements = JsoupUtil.getElementsByDocumentClass(document, "gallery_inner");
            if (null != elements && elements.size() > 0) {
                Elements srcs = JsoupUtil.getElementsBySelect(elements.get(0), "img[src]");
                if (null != srcs) {
                    int num = 0;
                    for (Element element : srcs) {
                        String src = JsoupUtil.getElementsByAttr(element, "abs:src");
                        try {
                            DownloadUtil.downLoadFromUrl(src, num + ".jpg", JsoupUtil.IMAGE_BASE_SAVE_PATH);
                        } catch (IOException e) {
                            logger.error(e.getMessage(),e);
                        }
                        num++;
                    }
                }
            }
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    

}
