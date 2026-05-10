package com.project.code.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    public Inventory findByProductIdandStoreId(Long productId, Long storeId);

    public List<Inventory> findByStoreId(Long storeId);

    @Modifying
    @Transactional
    public void deleteByProductId(Long productId);
}
