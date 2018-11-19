package com.zhb.vue.service.base.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.service.base.CommonService;

/**

*@author   zhanghb

*date 2018年10月21日下午1:33:23

*/

@Service
public class CommonServiceImpl implements CommonService{

    private Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);
    
    @Autowired
    public CommonDao commonDao;
    
    @Override
    @Transactional
    public  <T> Serializable save(T entity) {
        return commonDao.save(entity);
    }
    
    @Override
    @Transactional
    public  <T> void batchSave(List<T> entitys) {
        commonDao.batchSave(entitys);
    };
    
    @Override
    @Transactional
    public <T> void updateEntitie(T pojo) {
        commonDao.updateEntitie(pojo);
    }

    @Override
    @Transactional
    public <T> void updateEntityById(Class entityName, Serializable id) {
        commonDao.updateEntityById(entityName,id);
    }

    @Override
    @Transactional
    public <T> void updateEntitie(String className, Object id) {
        commonDao.updateEntitie(className,id);
    };
    
    @Override
    @Transactional
    public  <T> T saveOrUpdate(T entity) {
        return commonDao.saveOrUpdate(entity);
    };

    @Override
    @Transactional
    public  <T> void delete(T entity) {
        commonDao.delete(entity);
    };
    
    @Override
    @Transactional
    public  <T> void deleteEntityById(Class entityName, Serializable id) {
        commonDao.deleteEntityById(entityName,id);
    };
    
    @Override
    @Transactional
    public  <T> void deleteAllEntitie(Collection<T> entities) {
        commonDao.deleteAllEntitie(entities);
    };
    
    @Override
    @Transactional
    public  <T> T get(Class<T> entityClass, Serializable id) {
        return commonDao.get(entityClass, id);
    };

    @Override
    @Transactional
    public  <T> T getEntity(Class entityName, Serializable id) {
        return commonDao.getEntity(entityName, id);
    }

}


