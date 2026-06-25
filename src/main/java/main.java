import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Category;
import model.Product;
import model.CategorySpecification;
import model.ProductSpecification;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManger = factory.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {
            entityManger.getTransaction().begin();

            System.out.print("Введите название категорий: ");
            String categoryName = scanner.nextLine();

            System.out.print("Введите характеристики (через запятую и пробел): ");
            String specsInput = scanner.nextLine();

            Category category = new Category();
            category.setName(categoryName);

            category.setCategorySpecifications(new ArrayList<>());

            entityManger.persist(category);

            String[] specsArray = specsInput.split(", ");
            for (String specName : specsArray) {
                if (!specName.trim().isEmpty()) {
                    CategorySpecification catSpec = new CategorySpecification();
                    catSpec.setName(specName.trim());
                    catSpec.setCategory(category);
                    entityManger.persist(catSpec);
                    category.getCategorySpecifications().add(catSpec);
                }
            }

            entityManger.getTransaction().commit();
            System.out.println("Категория создана\n");


            List<Category> categories = entityManger.createQuery("FROM Category",
                    Category.class).getResultList();

            for (Category c : categories) {
                System.out.println(c.getId() + ". " + c.getName());
            }

            System.out.print("Выберите категорию: ");
            Long categoryId = scanner.nextLong();
            scanner.nextLine();

            Category selectedCategory = entityManger.find(Category.class, categoryId);
            if (selectedCategory == null) {
                System.out.println("Ошибка: Категория не найдена.");
                return;
            }

            System.out.print("Введите название товара: ");
            String productName = scanner.nextLine();

            System.out.print("Введите стоимость товара: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            entityManger.getTransaction().begin();

            Product product = new Product();
            product.setName(productName);
            product.setPrice(price);
            product.setCategory(selectedCategory);
            entityManger.persist(product);

            for (CategorySpecification catSpec : selectedCategory.getCategorySpecifications()) {
                System.out.print(catSpec.getName() + ": ");
                String valueInput = scanner.nextLine();

                ProductSpecification prodSpec = new ProductSpecification();
                prodSpec.setProduct(product);
                prodSpec.setCategorySpecification(catSpec);
                prodSpec.setName(valueInput);

                entityManger.persist(prodSpec);
            }

            entityManger.getTransaction().commit();

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



