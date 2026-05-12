package com.project.code.Service;

import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Model.Product;
import com.project.code.Model.Inventory;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {
    
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public ServiceClass(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public boolean validateInventory(Inventory inventory) {
        Inventory result = inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(), inventory.getStore().getId());

        if(result == null) {
            return true;
        }

        return false;
    }

    public boolean validateProduct(Product product) {
        Product result = productRepository.findByName(product.getName());

        if(result == null) {
            return true;
        }

        return false;
    }

    public boolean validateProductId(Long id) {
        Product result = productRepository.findByid(id);

        if(result == null) {
            return false;
        }

        return true;
    }

    public Inventory getInventoryId(Inventory inventory) {
        Inventory result = inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(), inventory.getStore().getId());

        return result;
    }
}
