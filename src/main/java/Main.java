import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = factory.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {
            entityManager.getTransaction().begin();
            User user = new User();
            user.setLogin("admin1234");
            user.setPassword("@dmiN1234");
            user.setRole(UserRole.ADMIN);
            user.setCreatedAt(LocalDateTime.now());
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            System.out.println("Тестовый пользователь создан (ID: " + user.getId() + ")\n");

            CategoryHandler.handleCategoryCreation(entityManager, scanner);
            System.out.println("Категория успешно создана!\n");

            ProductHandler.handleProductCreation(entityManager, scanner);

            OrderHandler.handleOrderCreation(entityManager, scanner);

            ReviewHandler.handleReviewCreation(entityManager, scanner);

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Произошла ошибка при работе с БД:");
            e.printStackTrace();
        } finally {
            scanner.close();
            entityManager.close();
            factory.close();
        }
    }
}