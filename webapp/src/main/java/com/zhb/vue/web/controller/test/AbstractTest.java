package com.zhb.vue.web.controller.test;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2019年3月5日下午1:48:34
*/

public abstract class AbstractTest {

    static {
        System.out.println("AbstractTest static{}");
    }
    
    public AbstractTest() {
        System.out.println("AbstractTest constructor");
    }

}


