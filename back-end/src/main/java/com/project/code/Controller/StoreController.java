package com.project.code.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.project.code.Model.Store;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    OrderService orderService;

    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> map = new HashMap<>();

        storeRepository.save(store);

        map.put("message", "Store successfully added to the database");

        return map;
    }

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        Store store = storeRepository.findByid(storeId);

        if (store != null) {
            return true;
        }

        return false;
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        Map<String, String> map = new HashMap<>();
        
        try {
            orderService.saveOrder(placeOrderRequestDTO);
            map.put("message", "Order successfully saved");
        } catch (Exception e) {
            map.put("Error", "" + e);
        }

        return map;
        
}
