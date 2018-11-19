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
import com.zhb.vue.dao.VerificationCodeInfoDao;
import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.VerificationCodeInfoData;

@Repository
public class VerificationCodeInfoDaoImpl extends CommonDao implements VerificationCodeInfoDao {

    @Override
    public void saveOrUpdate(VerificationCodeInfoData data) {
        if (null == data) {
            return ;
        }
        sessionFactory.getCurrentSession().saveOrUpdate(data);
    }

    @Override
    public List<VerificationCodeInfoData> getVerificationCodes(VerificationCodeInfoParam param,List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<VerificationCodeInfoData> criteriaQuery = criteriaBuilder.createQuery(VerificationCodeInfoData.class);
        Root<VerificationCodeInfoData> root = criteriaQuery.from(VerificationCodeInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getEmail())) {
            conditions.add(criteriaBuilder.equal(root.get("email"), param.getEmail()));
        }
        
        if (StringUtil.isNotBlank(param.getMobilePhone())) {
            conditions.add(criteriaBuilder.equal(root.get("mobilePhone"), param.getMobilePhone()));
        }
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
        }
        if (StringUtil.isNotBlank(param.getCode())) {
            conditions.add(criteriaBuilder.equal(root.get("code"), param.getCode()));
        }
        if (null != param.getDeleteFlag()) {
            conditions.add(criteriaBuilder.equal(root.get("deleteFlag"), param.getDeleteFlag()));
        }
        
        //小于等于这个时间
        if (null != param.getCreateTime()) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), param.getCreateTime()));
        }
        
        if (conditions.size() > 0 ) {
            criteriaQuery.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(criteriaBuilder, criteriaQuery, root, orderVos);
        }
        
        Query<VerificationCodeInfoData> query = session.createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public void delete(VerificationCodeInfoData data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Override
    public Long getVerificationCodesPageCount(VerificationCodeInfoParam param) {
        if (null == param) {
            return 0l;
        }
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<VerificationCodeInfoData> root = criteriaQuery.from(VerificationCodeInfoData.class);
        
        criteriaQuery.select(criteriaBuilder.count(root.get("id")));
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getEmail())) {
            conditions.add(criteriaBuilder.like(root.get("email"), "%" + param.getEmail() + "%" ));
        }
        
        if (StringUtil.isNotBlank(param.getMobilePhone())) {
            conditions.add(criteriaBuilder.equal(root.get("mobilePhone"), param.getMobilePhone()));
        }
        
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
        }
        
        if (StringUtil.isNotBlank(param.getCode())) {
            conditions.add(criteriaBuilder.equal(root.get("code"), "%" + param.getCode() + "%"));
        }
        
        if (null != param.getDeleteFlag()) {
            conditions.add(criteriaBuilder.equal(root.get("deleteFlag"), param.getDeleteFlag()));
        }
        
        if (conditions.size() > 0 ) {
            criteriaQuery.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<VerificationCodeInfoData> getVerificationCodesPage(VerificationCodeInfoParam param,
            List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<VerificationCodeInfoData> criteriaQuery = criteriaBuilder.createQuery(VerificationCodeInfoData.class);
        Root<VerificationCodeInfoData> root = criteriaQuery.from(VerificationCodeInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getEmail())) {
            conditions.add(criteriaBuilder.like(root.get("email"), "%" + param.getEmail() + "%"));
        }
        
        if (StringUtil.isNotBlank(param.getMobilePhone())) {
            conditions.add(criteriaBuilder.equal(root.get("mobilePhone"), param.getMobilePhone()));
        }
        
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
        }
        
        if (StringUtil.isNotBlank(param.getCode())) {
            conditions.add(criteriaBuilder.equal(root.get("code"),"%" + param.getCode() + "%"));
        }
        
        if (null != param.getDeleteFlag()) {
            conditions.add(criteriaBuilder.equal(root.get("deleteFlag"), param.getDeleteFlag()));
        }
        
        if (conditions.size() > 0 ) {
            criteriaQuery.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(criteriaBuilder, criteriaQuery, root, orderVos);
        }
        
        
        Query<VerificationCodeInfoData> query = session.createQuery(criteriaQuery);
        if (null != param.getPageSize()) {
            query.setMaxResults(param.getPageSize());
        }
        
        if (null != param.getStart()) {
            query.setFirstResult(param.getStart());
        }
        return query.list();
    }

}
