package com.zhb.vue.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.zhb.forever.framework.vo.OrderVO;

public class DaoUtil {
    
    public static void addOrders(CriteriaBuilder cb,CriteriaQuery<?>cq,Root<?>root,List<OrderVO> orderVos) {
        List<Order> orders = new ArrayList<>();
        for (OrderVO orderVO : orderVos) {
            if (orderVO.isAscending()) {
                orders.add(cb.asc(root.get(orderVO.getPropertyName())));
            }else {
                orders.add(cb.desc(root.get(orderVO.getPropertyName())));
            }
        }
        cq.orderBy(orders.toArray(new Order[orders.size()]));
    }

}
