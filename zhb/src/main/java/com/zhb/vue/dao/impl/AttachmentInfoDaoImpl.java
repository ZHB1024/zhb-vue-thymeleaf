package com.zhb.vue.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.dao.AttachmentInfoDao;
import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.params.AttachmentInfoParam;
import com.zhb.vue.pojo.AttachmentInfoData;

@Repository
public class AttachmentInfoDaoImpl extends CommonDao implements AttachmentInfoDao {
    
    @Override
    public void saveOrUpdate(AttachmentInfoData data) {
        sessionFactory.getCurrentSession().saveOrUpdate(data);
    }

    @Override
    public List<AttachmentInfoData> getAttachmentInfos(AttachmentInfoParam param, List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AttachmentInfoData> criteriaQuery = criteriaBuilder.createQuery(AttachmentInfoData.class);
        Root<AttachmentInfoData> root = criteriaQuery.from(AttachmentInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getFileName())) {
            conditions.add(criteriaBuilder.like(root.get("fileName"), "%" + param.getFileName() + "%"));
        }
        if (StringUtil.isNotBlank(param.getFilePath())) {
            conditions.add(criteriaBuilder.equal(root.get("filePath"), param.getFilePath()));
        }
        if (StringUtil.isNotBlank(param.getFileSize())) {
            conditions.add(criteriaBuilder.equal(root.get("fileSize"), param.getFileSize()));
        }
        if (StringUtil.isNotBlank(param.getContentType())) {
            conditions.add(criteriaBuilder.equal(root.get("contentType"), param.getContentType()));
        }
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
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
        Query<AttachmentInfoData> query = session.createQuery(criteriaQuery);
        
        return query.getResultList();
    }

    @Override
    public AttachmentInfoData getAttachmentInfoById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AttachmentInfoData> criteriaQuery = criteriaBuilder.createQuery(AttachmentInfoData.class);
        Root<AttachmentInfoData> root = criteriaQuery.from(AttachmentInfoData.class);
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
        Query<AttachmentInfoData> query = session.createQuery(criteriaQuery);
        List<AttachmentInfoData> datas = query.list();
        if (null == datas || datas.size() ==0) {
            return null;
        }
        return datas.get(0);
    }

    @Override
    public List<AttachmentInfoData> getAttachmentInfoByFileName(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AttachmentInfoData> criteriaQuery = criteriaBuilder.createQuery(AttachmentInfoData.class);
        Root<AttachmentInfoData> root = criteriaQuery.from(AttachmentInfoData.class);
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("fileName"), fileName));
        Query<AttachmentInfoData> query = session.createQuery(criteriaQuery);
        return query.list();
    }

    @Override
    public void deleteAttachmentInfo(AttachmentInfoData data) {
        sessionFactory.getCurrentSession().delete(data);
    }

    @Override
    public Long getAttachmentInfosPageCount(AttachmentInfoParam param) {
        if (null == param) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<AttachmentInfoData> root = criteriaQuery.from(AttachmentInfoData.class);
        
        criteriaQuery.select(criteriaBuilder.count(root.get("id")));
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getFileName())) {
            conditions.add(criteriaBuilder.like(root.get("fileName"), "%" + param.getFileName() + "%"));
        }
        if (StringUtil.isNotBlank(param.getFilePath())) {
            conditions.add(criteriaBuilder.equal(root.get("filePath"), param.getFilePath()));
        }
        if (StringUtil.isNotBlank(param.getFileSize())) {
            conditions.add(criteriaBuilder.equal(root.get("fileSize"), param.getFileSize()));
        }
        if (StringUtil.isNotBlank(param.getContentType())) {
            conditions.add(criteriaBuilder.equal(root.get("contentType"), param.getContentType()));
        }
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
        }
        if (null != param.getDeleteFlag()) {
            conditions.add(criteriaBuilder.equal(root.get("deleteFlag"), param.getDeleteFlag()));
        }
        
        if (conditions.size() > 0) {
            criteriaQuery.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<AttachmentInfoData> getAttachmentInfosPage(AttachmentInfoParam param, List<OrderVO> orderVos) {
        if (null == param) {
            return null;
        }
        
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AttachmentInfoData> criteriaQuery = criteriaBuilder.createQuery(AttachmentInfoData.class);
        Root<AttachmentInfoData> root = criteriaQuery.from(AttachmentInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(criteriaBuilder.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getFileName())) {
            conditions.add(criteriaBuilder.like(root.get("fileName"), "%" + param.getFileName() + "%"));
        }
        if (StringUtil.isNotBlank(param.getFilePath())) {
            conditions.add(criteriaBuilder.equal(root.get("filePath"), param.getFilePath()));
        }
        if (StringUtil.isNotBlank(param.getFileSize())) {
            conditions.add(criteriaBuilder.equal(root.get("fileSize"), param.getFileSize()));
        }
        if (StringUtil.isNotBlank(param.getContentType())) {
            conditions.add(criteriaBuilder.equal(root.get("contentType"), param.getContentType()));
        }
        if (null != param.getType()) {
            conditions.add(criteriaBuilder.equal(root.get("type"), param.getType()));
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
        
        Query<AttachmentInfoData> query = session.createQuery(criteriaQuery);
        if (null != param.getPageSize()) {
            query.setMaxResults(param.getPageSize());
        }
        
        if (null != param.getStart()) {
            query.setFirstResult(param.getStart());
        }
        
        return query.getResultList();
    }

    @Override
    public List<Object[]> statisticAttachment() {
        StringBuilder sb =  new StringBuilder();
        sb.append(" select a.type, count(a.id) ");
        sb.append("      from attachment_info a ");
        sb.append("             group by a.type ");
        sb.append("             order by a.type asc ");
        
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
        List<Object[]> results = query.list();
        return results;
    }

}
