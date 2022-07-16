package com.example.springassignmentt2010a.repository;

import com.example.springassignmentt2010a.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>, JpaSpecificationExecutor<OrderDetail> {
}
