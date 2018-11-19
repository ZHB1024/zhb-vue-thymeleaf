package com.zhb.vue.web.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WriteJSUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(WriteJSUtil.class);
    
    public static String writeJS(String msg,HttpServletResponse response) {
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print("<script>alert('"+msg+"');</script>");
            out.print("<script>history.go(-1);</script>");
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error("response print exception");
            e.printStackTrace();
        }
        return null;
    }

}
