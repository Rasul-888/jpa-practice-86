import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Category;
import model.Product;
import model.CategorySpecification;
import model.ProductSpecification;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = factory.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {

            List<Category> categories = entityManager.createQuery("FROM Category", Category.class).getResultList();

            if (categories.isEmpty()) {
                System.out.println("В базе пока нет категорий.");
                return;
            }


            for (Category c : categories) {
                System.out.println(c.getId() + ". " + c.getName());
            }

            System.out.print("Выберите категорию: ");
            Long categoryId = scanner.nextLong();
            scanner.nextLine();


            Category selectedCategory = entityManager.find(Category.class, categoryId);
            if (selectedCategory == null) {
                System.out.println("Ошибка: Категория с ID " + categoryId + " не найдена.");
                return;
            }


            System.out.print("Введите название товара: ");
            String productName = scanner.nextLine();

            System.out.print("Введите стоимость товара: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            entityManager.getTransaction().begin();


            Product product = new Product();
            product.setName(productName);
            product.setPrice(price);
            product.setCategory(selectedCategory);
            entityManager.persist(product);


            List<CategorySpecification> catSpecs = selectedCategory.getCategorySpecifications();

            for (CategorySpecification catSpec : catSpecs) {
                System.out.print(catSpec.getName() + ": ");
                String valueInput = scanner.nextLine();


                ProductSpecification prodSpec = new ProductSpecification();
                prodSpec.setProduct(product);
                prodSpec.setCategorySpecification(catSpec);
                prodSpec.setName(valueInput);

                entityManager.persist(prodSpec);
            }

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Произошла ошибка при работе с базой данных:");
            e.printStackTrace();
        } finally {
            scanner.close();
            entityManager.close();
            factory.close();
        }
    }
}



