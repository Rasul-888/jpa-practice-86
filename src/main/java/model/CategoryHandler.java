package model;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Scanner;

public class CategoryHandler {
    public static void handleCategoryCreation(EntityManager entityManger, Scanner scanner) {
        System.out.print("Введите название категорий: ");
        String categoryName = scanner.nextLine();

        System.out.print("Введите характеристики (через запятую и пробел): ");
        String specsInput = scanner.nextLine();

        entityManger.getTransaction().begin();

        Category category = new Category();
        category.setName(categoryName);
        category.setCategorySpecifications(new ArrayList<>());

        entityManger.persist(category);

        String[] specsArray = specsInput.split(",");
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
    }
}