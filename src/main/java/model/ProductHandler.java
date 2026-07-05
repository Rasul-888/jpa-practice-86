package model;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Scanner;

public class ProductHandler {
    public static void handleProductCreation(EntityManager entityManger, Scanner scanner) {
        List<Category> categories = entityManger.createQuery("FROM Category",
                Category.class).getResultList();

        if (categories.isEmpty()) {
            System.out.println("Категорий еще нет. Сначала создайте категорию.");
            return;
        }

        for (Category c : categories) {
            System.out.println(c.getId() + "." + c.getName());
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
            System.out.print(catSpec.getName() + ":");
            String valueInput = scanner.nextLine();

            ProductSpecification prodSpec = new ProductSpecification();
            prodSpec.setProduct(product);
            prodSpec.setCategorySpecification(catSpec);
            prodSpec.setName(valueInput);

            entityManger.persist(prodSpec);
        }

        entityManger.getTransaction().commit();
        System.out.println("Товар добавлен!\n");
    }
}