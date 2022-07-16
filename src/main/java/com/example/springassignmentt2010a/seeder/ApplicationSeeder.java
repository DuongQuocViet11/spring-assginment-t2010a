package com.example.springassignmentt2010a.seeder;

import com.example.springassignmentt2010a.entity.*;
import com.example.springassignmentt2010a.entity.enums.OrderSimpleStatus;
import com.example.springassignmentt2010a.entity.enums.ProductSimpleStatus;
import com.example.springassignmentt2010a.repository.OrderDetailRepository;
import com.example.springassignmentt2010a.repository.OrderRepository;
import com.example.springassignmentt2010a.repository.ProductRepository;
import com.example.springassignmentt2010a.repository.UserRepository;
import com.example.springassignmentt2010a.seeder.util.LocalDatetimehelper;
import com.example.springassignmentt2010a.util.StringHelper;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class ApplicationSeeder implements CommandLineRunner {
    boolean createSeedData = true;
    final OrderRepository orderRepository;
    final ProductRepository productRepository;
    final UserRepository userRepository;
    final OrderDetailRepository orderDetailRepository;
    Faker faker;
    Random random = new Random();
    int numberOfProduct = 200;
    int numberOfOrder = 1000;
    int numberOfUser = 10;

    public ApplicationSeeder(
            OrderRepository orderRepository,
            ProductRepository productRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) throws Exception {
        if(createSeedData){
            orderRepository.deleteAll();
            orderDetailRepository.deleteAll();
            productRepository.deleteAll();
            seedProduct();
            seedUser();
            seedOrder();
        }
    }

    private void seedUser(){
        List<User> listUsers = new ArrayList<>();
        for (int i = 0; i < numberOfUser; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setFullName(faker.name().fullName());
            user.setPhone(faker.phoneNumber().phoneNumber());
            user.setEmail(faker.name().title());
            user.setStatus(ProductSimpleStatus.ACTIVE);
            listUsers.add(user);
        }
        userRepository.saveAll(listUsers);
    }

    private void seedOrder() {
        List<Product> products = productRepository.findAll();
        List<User> users = userRepository.findAll();

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < numberOfOrder; i++) {

            Order order = new Order();
            User user = users.get(random.nextInt(users.size()));
            order.setUser(user);
            order.setId(UUID.randomUUID().toString());
            order.setCreatedAt(LocalDatetimehelper.generateLocalDate());

            Set<OrderDetail> orderDetails = new HashSet<>();

            int randomOrderDetailNumber = faker.number().numberBetween(1, 5);
            HashSet<String> existingProductId = new HashSet<>();
            for (int j = 0; j < randomOrderDetailNumber; j++) {
                OrderDetail orderDetail = new OrderDetail();
                Product randomProduct = products.get(
                        faker.number().numberBetween(0, products.size() - 1));

                if (existingProductId.contains(randomProduct.getId())) {
                    continue;
                }
                existingProductId.add(randomProduct.getId());

                orderDetail.setId(new OrderDetailId(order.getId(), randomProduct.getId()));
                orderDetail.setOrder(order);
                orderDetail.setProduct(randomProduct);
                orderDetail.setUnitPrice(randomProduct.getPrice());
                orderDetail.setQuantity(faker.number().numberBetween(1, 5));
                order.addTotalPrice(orderDetail);
                orderDetails.add(orderDetail);
            }
            order.setOrderDetails(orderDetails);
            order.setStatus(OrderSimpleStatus.PENDING);
            orders.add(order);
        }
        orderRepository.saveAll(orders);
    }

    private void seedProduct() {
        List<Product> listProduct = new ArrayList<>();
        for (int i = 0; i < numberOfProduct; i++) {
            System.out.println(i + 1);
            Product product = new Product();
            product.setId(UUID.randomUUID().toString());
            product.setName(faker.name().title());
            product.setSlug(StringHelper.toSlug(product.getName()));
            product.setDescription(faker.lorem().sentence()); // text
            product.setPrice(
                    new BigDecimal(faker.number().numberBetween(100, 200) * 10000));
            product.setCreatedBy("0");
            product.setUpdatedBy("0");
            product.setDetail(faker.lorem().sentence());
            product.setThumbnails(faker.avatar().image());
            product.setStatus(ProductSimpleStatus.ACTIVE);
            listProduct.add(product);
            System.out.println(product.toString());
        }
        productRepository.saveAll(listProduct);
    }

    public static void main(String[] args) {

    }
}
