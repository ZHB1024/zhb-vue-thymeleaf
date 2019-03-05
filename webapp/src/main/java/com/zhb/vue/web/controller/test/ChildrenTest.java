package com.zhb.vue.web.controller.test;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2019年3月5日下午1:43:07
*/

public class ChildrenTest extends AbstractTest implements InterfaceTest{
    
    static {
        System.out.println("ChildrenTest static{}");
    }

    public ChildrenTest() {
        System.out.println("ChildrenTest constructor");
    }

}


