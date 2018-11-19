package com.zhb.vue.dic;

import java.util.ArrayList;
import java.util.List;

import com.zhb.forever.framework.vo.KeyValueVO;

public enum FunctionTypeEnum {
    
    ROOT("根菜单",0),ONE_LEVEL("一级菜单",1),TWO_LEVEL("二级菜单",2);
    
    private String name;
    private int index;
    
    private FunctionTypeEnum(String name,int index){
        this.name = name;
        this.index = index;
    }
    
    public static String getName(int index){
        for (FunctionTypeEnum typeEnum : FunctionTypeEnum.values()) {
            if (typeEnum.getIndex() == index) {
                return typeEnum.getName();
            }
        }
        return "未定义";
    }

    public static List<KeyValueVO> getAll(){
        List<KeyValueVO> vos = new ArrayList<KeyValueVO>();
        for (FunctionTypeEnum typeEnum: FunctionTypeEnum.values()) {
            KeyValueVO vo = new KeyValueVO();
            vo.setKey(typeEnum.getName());
            vo.setValue(typeEnum.getIndex()+"");
            vos.add(vo);
        }
        return vos;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
