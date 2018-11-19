package com.zhb.vue.service.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhb.vue.dao.base.CommonDao;

/**

*@author   zhanghb

*date 2018年10月21日下午1:12:26

*/


public interface CommonService {

    
    /**
     * *新增实体
     * @param <T>
     * @param entitie
     */
      <T> Serializable save(T entity) ;
      
      /**
       * *批量新增实体
       * @param <T>
       * @param entitie
       */
      <T> void batchSave(List<T> entitys) ;
      
      /**
       ** 更新指定的实体
       * 
       * @param <T>
       * @param pojo
       */
      <T> void updateEntitie(T pojo);
      
      /**
       * *根据主键更新实体
       */
      <T> void updateEntityById(Class entityName, Serializable id);
      
      /**
       ** 更新指定的实体
       * 
       * @param <T>
       * @param pojo
       */
      <T> void updateEntitie(String className, Object id) ;
    
    /**
     * *新增或修改实体
     * @param <T>
     * @param entitie
     */
      <T> T saveOrUpdate(T entity) ;

    
    /**
     * *删除实体
     * @param <T>
     * @param entitie
     */
      <T> void delete(T entity) ;
    
    /**
     * *根据主键删除指定的实体
     * @param <T>
     * @param entitie
     */
      <T> void deleteEntityById(Class entityName, Serializable id);
    
    /**
     * *删除全部的实体
     * 
     * @param <T>
     * 
     * @param entitys
     */
      <T> void deleteAllEntitie(Collection<T> entities) ;
    
    /**
     * *根据实体名称和主键获取实体
     * @param <T>
     * @param entityName
     * @param id
     * @return
     */
      <T> T get(Class<T> entityClass, Serializable id) ;

    /**
     * *根据实体名称和主键获取实体
     * @param <T>
     * @param entityName
     * @param id
     * @return
     */
      <T> T getEntity(Class entityName, Serializable id) ;

}


