package deepmanapps.kouizer.config;

import deepmanapps.kouizer.domain.Category;
import deepmanapps.kouizer.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

    public final OpenTdbConfig openTdbConfig;

    public final CategoryRepository categoryRepository;


    @Override
    public void run(String... args) throws Exception {

        Map<Integer, String> categories = openTdbConfig.getCategories();

        if (categoryRepository.count() == 0) {
            List<Category> categoryEntities = new ArrayList<>();

            for (Map.Entry<Integer, String> entry : categories.entrySet()) {
                Category category =  Category.builder().name(entry.getValue()).externalId(entry.getKey().longValue()).build();
                categoryEntities.add(category);
            }

            categoryRepository.saveAll(categoryEntities);
            System.out.println("✅ Successfully loaded " + categoryEntities.size() + " categories from YAML to the database.");

        } else {
            System.out.println("ℹ️ Category table is already populated. Skipping YAML import.");
        }

    }
}
