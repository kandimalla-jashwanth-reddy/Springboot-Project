package com.example.demo.service;

import com.example.demo.entites.Book;
import com.example.demo.entites.Order;
import com.example.demo.entites.OrderItem;
import com.example.demo.entites.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Service Interface
public interface OrderService {
    Order placeOrder(User user, List<OrderItem> items);
    List<Order> getOrdersByUser(User user);

    // ✅ Add this for placing orders by IDs
    List<String> placeOrders(Long userId, List<Long> bookIds);
}

// Service Implementation
@Service
class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Order placeOrder(User user, List<OrderItem> items) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now().atStartOfDay());

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

    // ✅ This is the missing method your controller calls
    @Override
    public List<String> placeOrders(Long userId, List<Long> bookIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Book> books = bookRepository.findAllById(bookIds);
        if (books.isEmpty()) {
            throw new RuntimeException("No valid books found for the given IDs");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now().atStartOfDay());

        List<OrderItem> items = new ArrayList<>();
        for (Book book : books) {
            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setOrder(order);
            items.add(item);
        }

        order.setItems(items);
        orderRepository.save(order);

        return books.stream().map(Book::getTitle).toList();
    }
}
