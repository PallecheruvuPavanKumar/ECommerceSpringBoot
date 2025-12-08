package com.Ecommerce.service;

import com.Ecommerce.model.Order;
import com.Ecommerce.model.OrderItem;
import com.Ecommerce.model.Product;
import com.Ecommerce.model.dto.OrderItemRequest;
import com.Ecommerce.model.dto.OrderItemResponse;
import com.Ecommerce.model.dto.OrderRequest;
import com.Ecommerce.model.dto.OrderResponse;
import com.Ecommerce.repository.OrderRepo;
import com.Ecommerce.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    
    @Autowired
    private ProductRepo productRepo ;
    @Autowired
    private OrderRepo orderRepo;
    public OrderResponse placeOrder(OrderRequest request) {
        
        Order order = new Order();
        order.setOrderId("ORD"+UUID.randomUUID().toString().substring(0,8).toUpperCase());
        order.setCustomerName(request.customerName());
        order.setOrderDate(LocalDate.now());
        order.setStatus("Placed");
        order.setEmail(request.email());
        
        List<OrderItem> orderItems=new ArrayList<>();
        
        List<OrderItemRequest> items = request.items();
        
        for(OrderItemRequest itemRequest :items) {
            Product product = productRepo.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepo.save(product);
            
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())))
                    .order(order)
                    .build();
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        
        Order saveOrder = orderRepo.save(order);
        
        List<OrderItemResponse > orderItemResponses=new ArrayList<>();
        for(int i=0;i<order.getOrderItems().size();i++){
            orderItemResponses.add(new OrderItemResponse(saveOrder.getOrderItems().get(i).getProduct().getName(),saveOrder.getOrderItems().get(i).getQuantity(),
                    saveOrder.getOrderItems().get(i).getTotalPrice()));
        }
        
        return new OrderResponse(saveOrder.getOrderId(),
                saveOrder.getCustomerName(),
                saveOrder.getEmail(),
                saveOrder.getStatus(),order.getOrderDate(),
                orderItemResponses);
    }
    
    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses=new ArrayList<>();
        
        for(Order order :orders){
            List<OrderItemResponse > itemResponses=new ArrayList<>();
            for(OrderItem orderItem :order.getOrderItems()){
                itemResponses.add(new OrderItemResponse(orderItem.getProduct().getName()
                ,orderItem.getQuantity(),
                        orderItem.getTotalPrice()));
            }
            orderResponses.add(new OrderResponse(order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses));
        }
        
        return orderResponses;
    }
}
