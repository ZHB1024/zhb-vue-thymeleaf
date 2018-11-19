package com.zhb.vue.vo;

import java.util.ArrayList;
import java.util.List;

/**
* @author 作者:zhanghb
* @createDate 创建时间：2018年8月30日 上午13:51:36
*/
public class MenuVO {

    private String id;
    private String url;
    private String name;
    private String icon;
    private List<MenuVO> children;
    public MenuVO() {
        super();
        // TODO Auto-generated constructor stub
    }
    public MenuVO(String id, String url, String name) {
        super();
        this.id = id;
        this.url = url;
        this.name = name;
    }
    public MenuVO(String id, String url, String name, List<MenuVO> children) {
        super();
        this.id = id;
        this.url = url;
        this.name = name;
        this.children = children;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void addChild(MenuVO menu){
        if(children==null){
            children = new ArrayList<MenuVO>();
        }
        children.add(menu);
    }
    public void addChildren(List<MenuVO> menus){
        if(children==null){
            children = new ArrayList<MenuVO>();
        }
        children.addAll(menus);
    }
    public List<MenuVO> getChildren() {
        return children;
    }
}
