package com.project.code.Controller;

import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Service.ServiceClass;
import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ServiceClass serviceClass;

    @PutMapping
    public Map<String,String> updateInventory(@RequestBody CombinedRequest combinedRequest) {
        Inventory inventory = combinedRequest.getInventory();
        Map<String, String> map = new HashMap<>();
        
        if (!serviceClass.validateProductId(combinedRequest.getProduct().getId())) {
            map.put("message", "Id " + combinedRequest.getProduct().getId() + " not present in database");
            return map;
        }

        if (inventory != null) {
            try {
                Inventory result = serviceClass.getInventoryId(inventory);

                if (result != null) {
                    inventory.setId(result.getId());
                    inventoryRepository.save(inventory);
                } else {
                    map.put("message", "No data available for this product or store id");
                    return map;
                }
            } catch (DataIntegrityViolationException e) {
                map.put("message", "Error: " + e);
                System.out.println(e);
                return map;
            } catch (Exception e) {
                map.put("message", "Error: " + e);
                System.out.println(e);
                return map;
            }

            return map;
        }

        return map;
    }

    @PostMapping
    public Map<String, String> saveInventory(Inventory inventory) {
        Map<String, String> map = new HashMap<>();
        
        if (!serviceClass.validateInventory(inventory)) {
            map.put("message", "Data already present");
            return map;
        }

        try {
            inventoryRepository.save(inventory);
            map.put("message", "Data saved successfully");
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        } catch (Exception e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        }
        
        return map;
    }

    @GetMapping("/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("products", productRepository.findProductsByStoreId(storeId));

        return map;
    }

    @GetMapping("filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable Long storeId) {
        Map<String, Object> map = new HashMap<>();
        
        if (category.equals(null)) {
            map.put("product", productRepository.findByNameLike(storeId, name));
        } else if (name.equals(null)) {
            map.put("product", productRepository.findByCategoryAndStoreId(storeId, category));
        } else {
            map.put("product", productRepository.findByNameAndCategory(storeId, name, category));
        }

        return map;
    }

    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeId) {
        Map<String, Object> map = new HashMap<>();

        map.put("product", productRepository.findByNameLike(storeId, name));

        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {
        Map<String, String> map = new HashMap<>();

        if (!serviceClass.validateProductId(id)) {
            map.put("message", "Product with id " + id + " not present in database");
            return map;
        }

        inventoryRepository.deleteByProductId(id);
        map.put("message", "Product with id " + id + " deleted in database");

        return map;
    }

    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable int quantity, @PathVariable Long storeId, @PathVariable Long productId) {
        Inventory inventory = inventoryRepository.findByProductIdandStoreId(productId, storeId);

        if (quantity <= inventory.getStockLevel()) {
            return true;
        }

        return false;
    }
}
