package com.zhb.vue.web.controller.test;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.zhb.forever.framework.util.StringUtil;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2019年3月5日上午11:38:49
*/

public class Test {
    
    public static final InnerObject INNER_OBJECT = new InnerObject("hello");

    static {
        System.out.println("test static{}");
    }
    
    static {
        String property = System.getenv("propertyPath");
        if (StringUtil.isBlank(property)) {
            System.out.println("环境变量未配置propertyPath");
        }else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(property);
                Properties properties = new Properties();
                properties.load(fis);
                String logo = properties.getProperty("sys.logo.name");
                System.out.println(logo);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public Test(String name) {
        System.out.println("test constructor");
    }
    
    
    public static void main(String[] args) {
        //setTest();
        System.out.println(INNER_OBJECT.getName());
        INNER_OBJECT.setName("world");
        System.out.println(INNER_OBJECT.getName());
    }
    
    //set集合
    public static void setTest() {
        Set<Integer> set = new TreeSet<Integer>();
        set.add(8);
        set.add(2);
        set.add(5);
        set.add(7);
        set.add(1);
        set.add(6);
        set.add(0);
        set.add(9);
        set.add(3);
        
        Integer[] result = new Integer[set.size()];
        set.toArray(result);
        for (Integer integer : result) {
            System.out.println(integer);
        }
        
    }
    
    public static class InnerObject{
        private String name;
        
        public InnerObject(String name) {
            this.name = name;
            System.out.println(this.name);
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }

}


