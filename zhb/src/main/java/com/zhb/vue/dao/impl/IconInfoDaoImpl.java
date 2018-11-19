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
import com.zhb.vue.dao.IconInfoDao;
import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.params.IconInfoParam;
import com.zhb.vue.pojo.IconInfoData;

@Repository
public class IconInfoDaoImpl extends CommonDao implements IconInfoDao {
    
    @Override
    public void saveOrUpdate(IconInfoData data) {
        sessionFactory.getCurrentSession().saveOrUpdate(data);
    }


    @Override
    public List<IconInfoData> getIconInfos(IconInfoParam param,List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<IconInfoData> criteriaQuery = criteriaBuilder.createQuery(IconInfoData.class);
        Root<IconInfoData> root = criteriaQuery.from(IconInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getName())) {
            conditions.add(criteriaBuilder.equal(root.get("name"), param.getName()));
        }
        if (StringUtil.isNotBlank(param.getValue())) {
            conditions.add(criteriaBuilder.equal(root.get("value"), param.getValue()));
        }
        if (null != param.getDeleteFlag()) {
            conditions.add(criteriaBuilder.equal(root.get("deleteFlag"), param.getDeleteFlag()));
        }
        
        if (conditions.size() > 0) {
            criteriaQuery.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(criteriaBuilder, criteriaQuery, root, orderVos);
        }
        Query<IconInfoData> query = session.createQuery(criteriaQuery);
        
        return query.getResultList();
    }


    @Override
    public IconInfoData getIconInfoById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<IconInfoData> criteriaQuery = criteriaBuilder.createQuery(IconInfoData.class);
        Root<IconInfoData> root = criteriaQuery.from(IconInfoData.class);
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
        Query<IconInfoData> query = session.createQuery(criteriaQuery);
        List<IconInfoData> datas = query.list();
        if (null == datas || datas.size() ==0) {
            return null;
        }
        return datas.get(0);
    }

}
