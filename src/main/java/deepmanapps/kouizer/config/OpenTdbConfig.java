package deepmanapps.kouizer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@ConfigurationProperties(prefix = "opentdb")
@Configuration
public class OpenTdbConfig {

    private Map<Integer, String> categories;

    public Map<Integer, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<Integer, String> categories) {
        this.categories = categories;
    }
}