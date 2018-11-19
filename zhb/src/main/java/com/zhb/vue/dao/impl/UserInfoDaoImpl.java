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
import com.zhb.vue.dao.UserInfoDao;
import com.zhb.vue.dao.base.CommonDao;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.UserInfoData;

@Repository
public class UserInfoDaoImpl extends CommonDao implements UserInfoDao {
    
    @Override
    public void saveOrUpdate(UserInfoData data) {
        sessionFactory.getCurrentSession().saveOrUpdate(data);
    }
    
    @Override
    public List<UserInfoData> getUserInfos(UserInfoParam param,List<OrderVO> orderVos){
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserInfoData> cq = cb.createQuery(UserInfoData.class);
        Root<UserInfoData> root = cq.from(UserInfoData.class);
        
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtil.isNotBlank(param.getId())) {
            conditions.add(cb.equal(root.get("id"), param.getId()));
        }
        if (StringUtil.isNotBlank(param.getUserName())) {
            conditions.add(cb.equal(root.get("userName"), param.getUserName()));
        }
        if (StringUtil.isNotBlank(param.getEmail())) {
            conditions.add(cb.equal(root.get("email"), param.getEmail()));
        }
        
        if (conditions.size() > 0 ) {
            cq.where(conditions.toArray(new Predicate[conditions.size()]));
        }
        
        //排序
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(cb, cq, root, orderVos);
        }
        
        Query<UserInfoData> query = session.createQuery(cq);
        return query.list();
    }
    
    @Override
    public List<UserInfoData> getAllUserInfos(List<OrderVO> orderVos){
        Session session = sessionFactory.getCurrentSession();
        
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserInfoData> cq = cb.createQuery(UserInfoData.class);
        Root<UserInfoData> root = cq.from(UserInfoData.class);
        
        //排序
        if (null != orderVos && orderVos.size() > 0) {
            DaoUtil.addOrders(cb, cq, root, orderVos);
        }
        
        Query<UserInfoData> query = session.createQuery(cq);
        return query.list();
    }

    @Override
    public UserInfoData getUserInfoById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserInfoData> cq = cb.createQuery(UserInfoData.class);
        Root<UserInfoData> root = cq.from(UserInfoData.class);
        
        cq.where(cb.equal(root.get("id"), id));
        Query<UserInfoData> query = session.createQuery(cq);
        List<UserInfoData> datas = query.list();
        if (null == datas || datas.size() ==  0 ) {
            return null;
        }
        return datas.get(0);
    }

}
