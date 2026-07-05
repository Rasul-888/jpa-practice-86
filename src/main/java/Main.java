import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Scanner;
import model.ProductHandler;
import model.CategoryHandler;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManger = factory.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {
            entityManger.getTransaction().begin();
            CategoryHandler.handleCategoryCreation(entityManger, scanner);

            entityManger.getTransaction().commit();
            System.out.println("Категория создана\n");

            ProductHandler.handleProductCreation(entityManger, scanner);

        } catch (Exception e) {
            if (entityManger.getTransaction().isActive()) {
                entityManger.getTransaction().rollback();
            }
            System.err.println("Ошибка базы данных:");
            e.printStackTrace();
        } finally {
            scanner.close();
            entityManger.close();
            factory.close();
        }
    }
}



