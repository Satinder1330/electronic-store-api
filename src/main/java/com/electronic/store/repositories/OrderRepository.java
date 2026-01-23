package com.electronic.store.repositories;

import com.electronic.store.entities.Order;
import com.electronic.store.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,String> {
    Page<Order> findByUser(Pageable pageable, User user);
}
