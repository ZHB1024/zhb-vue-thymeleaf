package com.zhb.vue.web.controller.test.builder;

/**
 * @author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
 * @createDate 2019年3月28日上午8:57:40
 */

public class Person {

    private String xm;// 姓名
    private String xb;// 性别
    private String sfzh;// 身份证号
    private String gj;// 国籍
    private String mz;// 民族

    private Person(BuilderPerson builderPerson) {
        xm = builderPerson.xm;
        xb = builderPerson.xb;
        sfzh = builderPerson.sfzh;
        gj = builderPerson.gj;
        mz = builderPerson.mz;
    }

    public static class BuilderPerson implements Builder<Person> {

        private String xm;// 姓名
        private String xb;// 性别
        private String sfzh;// 身份证号
        private String gj;// 国籍
        private String mz;// 民族

        public BuilderPerson xm(String val) {
            xm = val;
            return this;
        }

        public BuilderPerson xb(String val) {
            xb = val;
            return this;
        }

        public BuilderPerson sfzh(String val) {
            sfzh = val;
            return this;
        }

        public BuilderPerson gj(String val) {
            gj = val;
            return this;
        }

        public BuilderPerson mz(String val) {
            mz = val;
            return this;
        }

        @Override
        public Person build() {
            return new Person(this);
        }

    }

    public static void main(String[] args) {
        Person person = new Person.BuilderPerson().xm("hello world").xb("男").build();
        System.out.println(person.xm + "--" + person.xb + "--" + person.sfzh + "--" + person.gj + "--" + person.mz);
    }
}
