package com.example.demo.service;



import com.example.demo.entites.Order;
import com.example.demo.entites.OrderItem;
import com.example.demo.entites.User;
import com.example.demo.entites.Book;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;

public interface OrderService {
    Order placeOrder(User user, List<OrderItem> items);
    List<Order> getOrdersByUser(User user);
}

@Service
class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Order placeOrder(User user, List<OrderItem> items) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now()); //


        for (OrderItem item : items) {
            item.setOrder(order);
            Book book = bookRepository.findById(item.getBook().getId()).orElseThrow();
            item.setBook(book);
        }

        order.setItems(items);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
