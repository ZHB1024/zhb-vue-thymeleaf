package com.zhb.vue.dao.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**

*@author   zhanghb

*date 2018年10月21日下午1:12:48

*/

@Repository
public class CommonDao {

    private Logger logger = LoggerFactory.getLogger(CommonDao.class);
    
    @Autowired
    public SessionFactory sessionFactory;
    
    public Session getSession() {
        // 事务必须是开启的(Required)，否则获取不到
        return sessionFactory.getCurrentSession();
    }

    
    /**
     * *新增实体
     * @param <T>
     * @param entitie
     */
    public  <T> Serializable save(T entity) {
        try {
            Serializable id = getSession().save(entity);
            getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("保存实体成功," + entity.getClass().getName());
            }
            return id;
        } catch (RuntimeException e) {
            logger.error("保存实体异常", e);
            throw e;
        }
    }
    
    /**
     * *批量新增实体
     * @param <T>
     * @param entitie
     */
    public  <T> void batchSave(List<T> entitys) {
        for (int i = 0; i < entitys.size(); i++) {
            getSession().save(entitys.get(i));
            if (i % 20 == 0) {
                // 20个对象后才清理缓存，写入数据库
                getSession().flush();
                getSession().clear();
            }
        }
        // 最后清理一下----防止大于20小于40的不保存
        getSession().flush();
        getSession().clear();
    };
    
    /**
     ** 更新指定的实体
     * 
     * @param <T>
     * @param pojo
     */
    public <T> void updateEntitie(T pojo) {
        getSession().update(pojo);
        getSession().flush();
    }

    /**
     * *根据主键更新实体
     */
    public <T> void updateEntityById(Class entityName, Serializable id) {
        updateEntitie(get(entityName, id));
    }
    
    /**
     ** 更新指定的实体
     * 
     * @param <T>
     * @param pojo
     */
    public <T> void updateEntitie(String className, Object id) {
        getSession().update(className, id);
        getSession().flush();
    }
    
    /**
     * *新增或修改实体
     * @param <T>
     * @param entitie
     */
    public  <T> T saveOrUpdate(T entity) {
        try {
            getSession().saveOrUpdate(entity);
            getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("添加或更新成功," + entity.getClass().getName());
            }
        } catch (RuntimeException e) {
            logger.error("添加或更新异常", e);
            throw e;
        }
        return entity;
    };

    
    /**
     * *删除实体
     * @param <T>
     * @param entitie
     */
    public  <T> void delete(T entity) {
        try {
            getSession().delete(entity);
            getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("删除成功," + entity.getClass().getName());
            }
        } catch (RuntimeException e) {
            logger.error("删除异常", e);
            throw e;
        }
    };
    
    /**
     * *根据主键删除指定的实体
     * @param <T>
     * @param entitie
     */
    public  <T> void deleteEntityById(Class entityName, Serializable id) {
        delete(get(entityName, id));
        getSession().flush();
    };
    
    /**
     * *删除全部的实体
     * 
     * @param <T>
     * 
     * @param entitys
     */
    public  <T> void deleteAllEntitie(Collection<T> entities) {
        for (Object entity : entities) {
            getSession().delete(entity);
            getSession().flush();
        }
    };
    
    /**
     * *根据实体名称和主键获取实体
     * @param <T>
     * @param entityName
     * @param id
     * @return
     */
    public  <T> T get(Class<T> entityClass, Serializable id) {
        return (T) getSession().get(entityClass, id);
    };

    /**
     * *根据实体名称和主键获取实体
     * @param <T>
     * @param entityName
     * @param id
     * @return
     */
    public  <T> T getEntity(Class entityName, Serializable id) {
        T t = (T) getSession().get(entityName, id);
        if (t != null) {
            getSession().flush();
        }
        return t;
    };

}


