package com.zhb.vue.web.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年12月11日上午9:20:09
*/

@Controller
@RequestMapping("/reflectcontroller")
public class ReflectController {
    
    protected Logger logger = LoggerFactory.getLogger(ReflectController.class);

    @RequestMapping("/toReflect")
    public void toReflect(){
        Person person = new Person("张三","男",18);
        printFields(person);
        printMethods(person);
        
        Class c = person.getClass();
        try {
            Method method1 = c.getMethod("sayName");
            Object object = method1.invoke(person);
            logger.info("syaName:{}",object.toString());
            
            Method method2 = c.getMethod("sayLove",new Class[]{String.class});
            logger.info("syaLove:{}",method2.invoke(person,"小红"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
 // java 5种 获取对象的方式
    private void getObjects() throws Exception {
        // By using new keyword
        Employee emp1 = new Employee("zhang");
        emp1.setName("Naresh");
        System.out.println(emp1 + ", hashcode : " + emp1.hashCode());

        // By using Class class's newInstance() method
        Employee emp2 = (Employee) Class.forName("com.forever.zhb.controller.annotation.Employee").newInstance();
        // Or we can simply do this
        // Employee emp2 = Employee.class.newInstance();
        emp2.setName("Rishi");
        System.out.println(emp2 + ", hashcode : " + emp2.hashCode());

        // By using Constructor class's newInstance() method
        Constructor<Employee> constructor = Employee.class.getConstructor(String.class);
        Employee emp3 = constructor.newInstance("");
        emp3.setName("Yogesh");
        System.out.println(emp3 + ", hashcode : " + emp3.hashCode());

        // By using clone() method
        Employee emp4 = (Employee) emp3.clone();
        emp4.setName("Atul");
        System.out.println(emp4 + ", hashcode : " + emp4.hashCode());

        // By using Deserialization
        // Serialization
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.obj"));
        out.writeObject(emp4);
        out.close();
        // Deserialization
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.obj"));
        Employee emp5 = (Employee) in.readObject();
        in.close();
        emp5.setName("Akash");
        System.out.println(emp5 + ", hashcode : " + emp5.hashCode());
    }
    
    private void printMethods(Object object){
        Class c = object.getClass();
        Method[] methods = c.getMethods();
        if (null != methods) {
            for (Method method : methods) {
                StringBuilder sb = new StringBuilder();
                Class returnType = method.getReturnType();
                String mentodName = method.getName();
                Class[] paras = method.getParameterTypes();
                Parameter[] parameters = method.getParameters();
                sb.append("public " + returnType.getName());
                sb.append(" " + mentodName + "(");
                if (null != parameters && parameters.length > 0) {
                    for (Parameter parameter : parameters) {
                        sb.append(parameter.getType().getName() + " " + parameter.getName() + ",");
                    }
                    sb.substring(0, sb.lastIndexOf(","));
                }
                sb.append(")");
                logger.info(sb.toString());
            }
        }
    }
    
    private void printFields(Object object){
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();
        if (null != fields) {
            for (Field field : fields) {
                StringBuilder sb = new StringBuilder();
                sb.append("private");
                sb.append(" " + field.getType().getName());
                sb.append(" " + field.getName());
                logger.info(sb.toString());
            }
        }
        
    }
    
    class Person{
        private String name;
        private String sex;
        private int age;
        
        public Person(){
            
        }
        
        public Person(String name,String sex,int age){
            this.name = name;
            this.sex = sex;
            this.age = age;
        }
        
        public String sayName(){
            return "I am " + name ;
        }
        
        public String sayLove(String girl){
            return "I love " + girl;
        }
    }
    
    class Employee implements Serializable, Cloneable {

        private static final long serialVersionUID = 1L;
        private String name;

        public Employee() {
            System.out.println("Employee Constructor() Called...");
        }

        public Employee(String name) {
            System.out.println("Employee Constructor(name) Called...");
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Employee other = (Employee) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + "]";
        }

        @Override
        public Object clone() {
            Object obj = null;
            try {
                obj = super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return obj;
        }

    }

}


