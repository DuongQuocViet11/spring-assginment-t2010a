package com.example.springassignmentt2010a.entity.search;

import com.example.springassignmentt2010a.entity.Order;
import com.example.springassignmentt2010a.entity.OrderDetail;
import com.example.springassignmentt2010a.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;

public class OrderSpecification implements Specification<Order> {

    private SearchCriteria searchCriteria;
    public OrderSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (searchCriteria.getOperator()) {
            case EQUALS:
                return criteriaBuilder.equal(
                        root.get(searchCriteria.getKey()),
                        searchCriteria.getValue());
            case LIKE:
                return criteriaBuilder.like(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case GREATER_THAN_OR_EQUALS:
                if (root.get(searchCriteria.getKey()).getJavaType() == LocalDateTime.class){
                    return criteriaBuilder.greaterThanOrEqualTo(
                            root.get(searchCriteria.getKey()), (LocalDateTime) searchCriteria.getValue());
                } else {
                    return criteriaBuilder.greaterThanOrEqualTo(
                            root.get(searchCriteria.getKey()),
                            String.valueOf(searchCriteria.getValue()));
                }
            case LESS_THAN:
                return criteriaBuilder.lessThan(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case LESS_THAN_OR_EQUALS:
                if (root.get(searchCriteria.getKey()).getJavaType() == LocalDateTime.class){
                    return criteriaBuilder.lessThanOrEqualTo(
                            root.get(searchCriteria.getKey()), (LocalDateTime) searchCriteria.getValue());
                } else {
                    return criteriaBuilder.lessThanOrEqualTo(
                            root.get(searchCriteria.getKey()),
                            String.valueOf(searchCriteria.getValue()));
                }
            case JOIN_DETAIL_PRODUCT:
                Join<OrderDetail, Product> orderDetailProductJoin = root.join("orderDetails").join("product");
                Predicate predicate = criteriaBuilder.or(

                        criteriaBuilder.like(orderDetailProductJoin.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%")
                );
                return predicate;
            case JOIN_USER:
                From<Order, User> orderUserJoin = root.join("user");
                return criteriaBuilder.like(
                        orderUserJoin.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%"
                );
        }
        return null;
    }
}
