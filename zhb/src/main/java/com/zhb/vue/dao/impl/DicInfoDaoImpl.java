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
import com.zhb.vue.dao.DicInfoDao;
import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.params.DicInfoParam;
import com.zhb.vue.pojo.DicInfoData;

@Repository
public class DicInfoDaoImpl extends CommonDao implements DicInfoDao {
    
    @Override
    public void saveOrUpdate(DicInfoData data) {
        sessionFactory.getCurrentSession().saveOrUpdate(data);
    }
    
    @Override
    public List<DicInfoData> getDicInfos(DicInfoParam param,List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        Session session = sessionFactory.getCurrentSession();
        //Criteria criteria = session.createCriteria(DicInfoData.class);
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DicInfoData> criteriaQuery = criteriaBuilder.createQuery(DicInfoData.class);
        Root<DicInfoData> root = criteriaQuery.from(DicInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getCategory())) {
            conditions.add(criteriaBuilder.equal(root.get("category"), param.getCategory()));
        }
        
        if (StringUtil.isNotBlank(param.getCode())) {
            conditions.add(criteriaBuilder.equal(root.get("code"), param.getCode()));
        }
        if (StringUtil.isNotBlank(param.getName())) {
            conditions.add(criteriaBuilder.equal(root.get("name"), param.getName()));
        }
        if (StringUtil.isNotBlank(param.getName2())) {
            conditions.add(criteriaBuilder.equal(root.get("name2"), param.getName2()));
        }
        if (StringUtil.isNotBlank(param.getName3())) {
            conditions.add(criteriaBuilder.equal(root.get("name3"), param.getName3()));
        }
        if (StringUtil.isNotBlank(param.getType())) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
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
        
        Query<DicInfoData> query = session.createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public Long getDicInfosPageCount(DicInfoParam param) {
        if (null == param) {
            return 0l;
        }
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<DicInfoData> root = criteriaQuery.from(DicInfoData.class);
        
        criteriaQuery.select(criteriaBuilder.count(root.get("id")));
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getCategory())) {
            conditions.add(criteriaBuilder.equal(root.get("category"), param.getCategory()));
        }
        
        if (StringUtil.isNotBlank(param.getCode())) {
            conditions.add(criteriaBuilder.equal(root.get("code"), param.getCode()));
        }
        if (StringUtil.isNotBlank(param.getName())) {
            conditions.add(criteriaBuilder.equal(root.get("name"), param.getName()));
        }
        if (StringUtil.isNotBlank(param.getName2())) {
            conditions.add(criteriaBuilder.equal(root.get("name2"), param.getName2()));
        }
        if (StringUtil.isNotBlank(param.getName3())) {
            conditions.add(criteriaBuilder.equal(root.get("name3"), param.getName3()));
        }
        if (StringUtil.isNotBlank(param.getType())) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
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
    public List<DicInfoData> getDicInfosPage(DicInfoParam param, List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DicInfoData> criteriaQuery = criteriaBuilder.createQuery(DicInfoData.class);
        Root<DicInfoData> root = criteriaQuery.from(DicInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getCategory())) {
            conditions.add(criteriaBuilder.equal(root.get("category"), param.getCategory()));
        }
        
        if (StringUtil.isNotBlank(param.getCode())) {
            conditions.add(criteriaBuilder.equal(root.get("code"), param.getCode()));
        }
        if (StringUtil.isNotBlank(param.getName())) {
            conditions.add(criteriaBuilder.equal(root.get("name"), param.getName()));
        }
        if (StringUtil.isNotBlank(param.getName2())) {
            conditions.add(criteriaBuilder.equal(root.get("name2"), param.getName2()));
        }
        if (StringUtil.isNotBlank(param.getName3())) {
            conditions.add(criteriaBuilder.equal(root.get("name3"), param.getName3()));
        }
        if (StringUtil.isNotBlank(param.getType())) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
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
        
        
        Query<DicInfoData> query = session.createQuery(criteriaQuery);
        if (null != param.getPageSize()) {
            query.setMaxResults(param.getPageSize());
        }
        
        if (null != param.getStart()) {
            query.setFirstResult(param.getStart());
        }
        return query.list();
    }

    @Override
    public List<String> getDicCategory() {
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<DicInfoData> root = criteriaQuery.from(DicInfoData.class);
        
        criteriaQuery.select(root.get("category"));
        
        criteriaQuery.groupBy(root.get("category"));
        
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("category")));
        
        Query<String> query = session.createQuery(criteriaQuery);
        
        return query.list();
    }

    @Override
    public List<String> getDicTypeByCategory(DicInfoParam param) {
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<DicInfoData> root = criteriaQuery.from(DicInfoData.class);
        
        criteriaQuery.select(root.get("type"));
        
        if (StringUtil.isNotBlank(param.getCategory())) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("category"), param.getCategory()));
        }
        
        criteriaQuery.groupBy(root.get("type"));
        
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("type")));
        
        Query<String> query = session.createQuery(criteriaQuery);
        
        return query.list();
    }

    @Override
    public List<Object[]> statisticDic() {
        StringBuilder sb =  new StringBuilder();
        sb.append(" select a.category, count(a.id) ");
        sb.append("      from dic_info a ");
        sb.append("             group by a.category ");
        sb.append("             order by a.category asc ");
        
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
        List<Object[]> results = query.list();
        return results;
    }


}
