package com.zhb.vue.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.dao.FunctionInfoDao;
import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.params.FunctionInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;

@Repository
public class FunctionInfoDaoImpl extends CommonDao implements FunctionInfoDao {
    
    
    @Override
    public void saveOrUpdate(FunctionInfoData data) {
        sessionFactory.getCurrentSession().saveOrUpdate(data);
    }
    
    @Override
    public void delFunctionInfoData(FunctionInfoData data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Override
    public List<FunctionInfoData> getFunctions(FunctionInfoParam param,List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<FunctionInfoData> criteriaQuery = criteriaBuilder.createQuery(FunctionInfoData.class);
        Root<FunctionInfoData> root = criteriaQuery.from(FunctionInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getName())) {
            conditions.add(criteriaBuilder.equal(root.get("name"), param.getName()));
        }
        
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
        }
        
        if (StringUtil.isNotBlank(param.getPath())) {
            conditions.add(criteriaBuilder.equal(root.get("path"), param.getPath()));
        }
        
        if (null != param.getIconInfoData()) {
            conditions.add(criteriaBuilder.equal(root.get("iconInfoData"), param.getIconInfoData()));
        }
        
        if (null != param.getParentFunctionInfo()) {
            conditions.add(criteriaBuilder.equal(root.get("parentFunctionInfo"), param.getParentFunctionInfo()));
        }
        
        if (null != param.getDeleteFlag()) {
            conditions.add(criteriaBuilder.equal(root.get("deleteFlag"), param.getDeleteFlag()));
        }
        
        if (conditions.size() > 0 ) {
            criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[conditions.size()])));
        }
        
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(criteriaBuilder, criteriaQuery, root, orderVos);
        }
        
        Query<FunctionInfoData> query = session.createQuery(criteriaQuery);
        return query.list();
    }
    
    @Override
    public List<FunctionInfoData> getAllFunctions(List<OrderVO> orderVos) {
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<FunctionInfoData> criteriaQuery = criteriaBuilder.createQuery(FunctionInfoData.class);
        Root<FunctionInfoData> root = criteriaQuery.from(FunctionInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        conditions.add(criteriaBuilder.equal(root.get("type"), 1));
        
        criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[conditions.size()])));
        
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(criteriaBuilder, criteriaQuery, root, orderVos);
        }
        
        Query<FunctionInfoData> query = session.createQuery(criteriaQuery);
        return query.list();
    }


    @Override
    public int getMaxOrder(FunctionInfoParam param) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        
        Root<FunctionInfoData> root = criteriaQuery.from(FunctionInfoData.class);
        
        criteriaQuery.select(criteriaBuilder.max(root.get("order")));
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getParentId()) && !"undefined".equals(param.getParentId())) {
            conditions.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), param.getParentId()),criteriaBuilder.equal(root.get("parentFunctionInfo"), param.getParentFunctionInfo())));
        }
        if (conditions.size() > 0 ) {
            criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[conditions.size()])));
        }
        
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public FunctionInfoData getFunctionById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<FunctionInfoData> criteriaQuery = criteriaBuilder.createQuery(FunctionInfoData.class);
        Root<FunctionInfoData> root = criteriaQuery.from(FunctionInfoData.class);
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
        Query<FunctionInfoData> query = session.createQuery(criteriaQuery);
        List<FunctionInfoData> datas = query.list();
        if (null == datas || datas.size() ==0) {
            return null;
        }
        return datas.get(0);
    }

}
