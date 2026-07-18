package model;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Scanner;

public class OrderHandler {
    public static void handleOrderCreation(EntityManager entityManager, Scanner scanner) {
        System.out.println("Введите ID пользователя для оформления заказа: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        User user = entityManager.find(User.class, userId);
        if (user == null){
            System.out.println("Ошибка: пользователь не найден.");
            return;

        }

        System.out.print("Введите адрес доставки: ");
        String address = scanner.nextLine();

        System.out.print("Введите ID товара для добавления в заказ: ");
        Long productId = scanner.nextLong();

        System.out.print("Введите количество товара: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = entityManager.find(Product.class, productId);
        if (product == null) {
            System.out.println("Ошибка: товар не найден.");
            return;
        }

        entityManager.getTransaction().begin();

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryAddress(address);
        order.setCreatedAt(LocalDateTime.now());
        entityManager.persist(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        entityManager.persist(orderItem);

        order.getOrderItems().add(orderItem);

        entityManager.getTransaction().commit();
        System.out.println("Заказ успешно создан!\n");
    }
}
