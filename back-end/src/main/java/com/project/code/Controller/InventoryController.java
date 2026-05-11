package com.project.code.Controller;

import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Service.ServiceClass;
import com.project.code.Model.CombinedRequest;

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

    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("products", productRepository.findProductsByStoreId(storeId));

        return map;
    }

    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable long storeid) {
        Map<String, Object> map = new HashMap<>();
        
        if (category.equals(null)) {
            map.put("product", productRepository.findByNameLike(storeid, name));
        } else if (name.equals(null)) {
            map.put("product", productRepository.findByCategoryAndStoreId(storeid, category));
        } else {
            map.put("product", productRepository.findByNameAndCategory(storeid, name, category));
        }

        return map;
    }

    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable long storeid) {
        Map<String, Object> map = new HashMap<>();

        map.put("product", productRepository.findByNameLike(storeid, name));

        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();

        if (!serviceClass.validateProductId(id)) {
            map.put("message", "Product not present in database");
        }

        return map;
    }
// 7. Define the `searchProduct` Method:
//    - This method handles HTTP GET requests to search for products by name within a specific store.
//    - It uses `name` and `storeId` as parameters and searches for products that match the `name` in the specified store.
//    - The search results are returned in the response with the key `"product"`.


// 8. Define the `removeProduct` Method:
//    - This method handles HTTP DELETE requests to delete a product by its ID.
//    - It first validates if the product exists. If it does, it deletes the product from the `ProductRepository` and also removes the related inventory entry from the `InventoryRepository`.
//    - Returns a success message with the key `"message"` indicating successful deletion.


// 9. Define the `validateQuantity` Method:
//    - This method handles HTTP GET requests to validate if a specified quantity of a product is available in stock for a given store.
//    - It checks the inventory for the product in the specified store and compares it to the requested quantity.
//    - If sufficient stock is available, return `true`; otherwise, return `false`.

}
