package com.project.code.Service;

import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.StoreRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Customer;
import com.project.code.Model.Store;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.Product;
import com.project.code.Model.Inventory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustumerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository oderDetailsRepository;

    @Autowired
    private OrderItemRepository OrderItemRepository;

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequestDTO) {
        Customer customer = customerRepository.findByEmail(placeOrderRequestDTO.getCustomerEmail());

        if(customer == null) {
            customer = new Customer();
            customer.setName(placeOrderRequestDTO.getCostumerName());
            customer.setEmail(placeOrderRequestDTO.getCostumerEmail());
            customer.setPhone(placeOrderRequestDTO.getCostumerPhone());
        }

        customerRepository.save(customer);

        Store store = storeRepository.findById(placeOrderRequest.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found"));

        OrderDetails orderDetails = new OrderDetails(customer, store, placeOrderRequestDTO.getTotalPrice(), java.time.LocalDateTime.now());
        // List listOrderItems = orderDetails.getOrderItems();

        for(PurchaseProductDTO purchaseProduct : placeOrderRequestDTO.getPurchaseProduct()) {
            Product product = productRepository.findById(purchaseProduct.getId());
            
            OrderItem orderItem = new OrderItem(orderDetails, product, purchaseProduct.getQuantity(), purchaseProduct.getPrice() * purchaseProduct.getQuantity());
            orderItemRepository.save(orderItem);

            Inventory inventory = inventoryRepository.findByProductIdandStoreId(product.getId(), store.getId());
            inventory.setStockLevel(inventory.getStockLevel() - purchaseProduct.getQuantity());
            inventoryRepository.save(inventory);

            // listOrderItems.add(orderItem);
        }

        // orderDetailsRepository.save(orderDetails);
    }
}
