package auction.repository;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CategoryDao {
    private final Set<String> categories;

    public CategoryDao() {
        categories = new LinkedHashSet<>();
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        categories.add(category);
    }
}
