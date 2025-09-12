package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "api", "model", "repository", "service", "persistence", "util"
})
public class VitalmasApplication {
    public static void main(String[] args) {
        SpringApplication.run(VitalmasApplication.class, args);
    }
}
