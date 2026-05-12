package com.project.code.Repo;

import com.project.code.Model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findByEmail(String email);

    public Customer findByid(Long id);

// 3. Add any additional methods you may need for custom queries:
//    - You can create other query methods as needed, like finding customers by name or phone number, etc.

// Example: public List<Customer> findByName(String name);
}


