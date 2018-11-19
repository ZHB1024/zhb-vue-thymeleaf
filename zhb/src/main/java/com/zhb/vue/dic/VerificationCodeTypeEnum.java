package com.zhb.vue.dic;

import java.util.ArrayList;
import java.util.List;

import com.zhb.forever.framework.vo.KeyValueVO;

public enum VerificationCodeTypeEnum {
    
    REGISTER("注册",0),UPDATE_PASSWORD("修改密码",1);
    
    private String name;
    private int index;
    
    private VerificationCodeTypeEnum(String name,int index){
        this.name = name;
        this.index = index;
    }
    public static String getName(int index){
        for (VerificationCodeTypeEnum typeEnum : VerificationCodeTypeEnum.values()) {
            if (typeEnum.getIndex() == index) {
                return typeEnum.getName();
            }
        }
        return "未定义";
    }

    public static List<KeyValueVO> getAll(){
        List<KeyValueVO> vos = new ArrayList<KeyValueVO>();
        for (VerificationCodeTypeEnum typeEnum: VerificationCodeTypeEnum.values()) {
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
