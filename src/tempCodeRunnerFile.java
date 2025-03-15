import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String studentFile = "../assets/students.csv";
        Scanner scanner = new Scanner(System.in);
        boolean isAuthenticated = false;

        // User authentication loop
        while (!isAuthenticated) {
            System.out.print("Enter your full name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter your student ID: ");
            String studentId = scanner.nextLine().trim();

            // Verify student credentials
            isAuthenticated = verifyStudent(studentFile, name, studentId);

            if (!isAuthenticated) {
                System.out.println("Name or student ID does not match. Please try again.");
            }
        }

        // Start the food ordering system
        FoodOrderSystem foodOrderSystem = new FoodOrderSystem();
        foodOrderSystem.startOrdering();

        scanner.close();
    }

    // Method to verify student information from CSV
    private static boolean verifyStudent(String filePath, String name, String studentId) {
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().trim().split(",");
                if (data.length < 2) continue; // Skip invalid lines

                String validName = data[0].trim();
                String validId = data[1].trim();

                if (validName.equalsIgnoreCase(name) && validId.equals(studentId)) {
                    System.out.println("Welcome to the Food Order System!");
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Student file not found. Please check the file path.");
        }
        return false;
    }
}
