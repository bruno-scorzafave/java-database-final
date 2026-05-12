package com.project.code.Repo;

import com.project.code.Model.Store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT i FROM Store i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    public List<Store> findBySubName(String pname);

    public Store findByid(Long id);;

}
