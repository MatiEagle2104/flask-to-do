package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        // 1. Brak asercji w teście
        System.out.println("Context loads test ran without assertions"); // Nieoptymalne logowanie
    }

    @Test
    void testSQLInjection() {
        // 2. Hardkodowane dane wrażliwe
        String username = "admin";
        String password = "12345"; // Hardkodowane hasło
        String unsafeQuery = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "';";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password")) {
            // 3. Brak użycia PreparedStatement
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(unsafeQuery); // SQL Injection podatność

            while (resultSet.next()) {
                // 4. Nieoptymalne logowanie danych wrażliwych
                System.out.println("User found: " + resultSet.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Niepoprawne logowanie wyjątków
        }
    }

    @Test
    void unusedMethod() {
        // 5. Nieużywana metoda w klasie testowej
        System.out.println("This method does nothing useful");
    }

    @Test
    void emptyTest() {
        // 6. Pusty test
    }
}
