package model;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ReviewHandler {
    public static void handleReviewCreation(EntityManager entityManager, Scanner scanner) {
        System.out.print("Введите ID пользователя для создания отзыва: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        User user = entityManager.find(User.class, userId);
        if (user == null) {
            System.out.println("Ошибка: Пользователь не найден.");
            return;
        }

        System.out.print("Введите ID товара для отзыва: ");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        Product product = entityManager.find(Product.class, productId);
        if (product == null) {
            System.out.println("Ошибка: Товар не найден.");
            return;
        }

        List<Order> allOrders = entityManager.createQuery("FROM Order", Order.class).getResultList();
        boolean hasBought = false;

        for (Order order : allOrders) {
            if (order.getUser() != null && order.getUser().getId().equals(userId)) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item.getProduct() != null && item.getProduct().getId().equals(productId)) {
                        hasBought = true;
                        break;
                    }
                }
            }
            if (hasBought) {
                break;
            }
        }

        if (!hasBought) {
            System.out.println("Ошибка: Вы не можете оставить отзыв на товар, который не заказывали.");
            return;
        }

        System.out.print("Оцените товар (от 1 до 5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите текст отзыва: ");
        String reviewText = scanner.nextLine();

        entityManager.getTransaction().begin();

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setText(reviewText);
        review.setIsPublished(true);
        review.setCreatedAt(LocalDateTime.now());

        entityManager.persist(review);

        entityManager.getTransaction().commit();
        System.out.println("Отзыв успешно добавлен!\n");
    }
}