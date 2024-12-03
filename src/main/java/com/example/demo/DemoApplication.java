package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {

    // 1. Public zmienna statyczna (potencjalny problem z bezpieczeństwem)
    public static List<String> globalList = new ArrayList<>();

    // 2. Nieprawidłowa obsługa wyjątków
    public static void main(String[] args) {
        try {
            SpringApplication.run(DemoApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace(); // Niepoprawne logowanie wyjątków
        }
    }

    // 3. Metoda zbyt długa i z nieużywanymi zmiennymi
    @RequestMapping("/")
    public String home() {
        String unusedVariable = "This variable is not used"; // Nieużywana zmienna
        int a = 1, b = 2;
        if (a == b) {
            return "Logic error: this will never happen"; // Nieosiągalny kod
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("Iterating: " + i); // Nieoptymalne logowanie
        }
        return "Hello Docker World!";
    }

    // 4. Hardkodowane dane wrażliwe
    @RequestMapping("/secret")
    public String secret() {
        String password = "12345"; // Hardkodowane hasło
        return "The secret is: " + password;
    }

    // 5. Brak zamknięcia zasobu
    @RequestMapping("/file")
    public String readFile() {
        java.io.FileReader fileReader;
        try {
            fileReader = new java.io.FileReader("test.txt");
            char[] buffer = new char[100];
            fileReader.read(buffer);
            // Zasób nie jest zamykany
        } catch (Exception e) {
            return "Error reading file";
        }
        return "File read";
    }

    // 6. Metoda robi za dużo
    @RequestMapping("/complex")
    public void complexMethod() {
        globalList.add("This method does multiple things"); // Użycie zmiennej globalnej
        home(); // Wywołanie innej metody kontrolera w sposób nieoptymalny
        System.out.println("Complex logic here!"); // Nieoptymalne logowanie
    }
}
