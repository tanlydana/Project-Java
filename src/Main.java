import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Menu for selecting user type (user/admin)
        System.out.println("Welcome to AUPP Food Order System");
        System.out.println("1. User");
        System.out.println("2. Admin");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // If the user selects "1" for user, proceed with food order system
        if (choice == 1) {
            userLogin(scanner);
        } else if (choice == 2) {
            adminLogin(scanner);
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }

    private static void userLogin(Scanner scanner) {
        String studentFile = "assets/students.txt";
        boolean isMatch = false;
        String name = "";
        String studentId = "";

        // Keep asking until the user enters the correct name and ID
        while (!isMatch) {
            System.out.print("Enter your full name (No Space): ");
            name = scanner.nextLine().trim();

            System.out.print("Enter your student ID: ");
            studentId = scanner.nextLine().trim();

            // Check if the name and ID are valid
            if (checkStudent(studentFile, name, studentId)) {
                System.out.println("\n\tWelcome to the Food Order System!");
                isMatch = true;
            } else {
                System.out.println("Name or student ID does not match. Please try again.");
            }
        }

        // Start ordering food
        FoodOrderSystem foodOrderSystem = new FoodOrderSystem();
        foodOrderSystem.startOrdering(name, studentId);
    }

    private static void adminLogin(Scanner scanner) {
        String adminFile = "assets/admin_credentials.txt";
        boolean isAuthenticated = false;
        String adminName, adminPassword;

        // Ask for admin credentials
        while (!isAuthenticated) {
            System.out.print("Enter admin name: ");
            adminName = scanner.nextLine().trim();

            System.out.print("Enter admin password: ");
            adminPassword = scanner.nextLine().trim();

            // Check if the admin credentials are valid
            if (checkAdminCredentials(adminFile, adminName, adminPassword)) {
                System.out.println("Admin login successful.");
                adminPanel(scanner);
                isAuthenticated = true;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    // Method to check student info in the txt file
    private static boolean checkStudent(String filePath, String name, String studentId) {
        try {
            Scanner fileScanner = new Scanner(new File(filePath));
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) {
                    if (data[0].trim().equalsIgnoreCase(name) && data[1].trim().equals(studentId)) {
                        fileScanner.close();
                        return true;
                    }
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Student file not found. Please check the file path.");
        }
        return false;
    }

    // Method to check admin credentials in the admin file
    private static boolean checkAdminCredentials(String filePath, String adminName, String adminPassword) {
        try {
            Scanner fileScanner = new Scanner(new File(filePath));
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length == 2) {
                    if (data[0].trim().equals(adminName) && data[1].trim().equals(adminPassword)) {
                        fileScanner.close();
                        return true;
                    }
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Admin credentials file not found. Please check the file path.");
        }
        return false;
    }

    // Admin panel to perform actions (like find top spender)
    private static void adminPanel(Scanner scanner) {
        System.out.print("Enter date to find top spender (yyyy-MM-dd): ");
        String date = scanner.nextLine().trim();

        // Find top spender based on the date
        findTopSpender(date);
    }

    // Method to find the top spender based on the date
    private static void findTopSpender(String date) {
        Map<String, Double> spenders = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("assets/customer_orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && data[3].equals(date)) {
                    String studentName = data[1];
                    double amount = Double.parseDouble(data[2]);

                    spenders.put(studentName, spenders.getOrDefault(studentName, 0.0) + amount);
                }
            }

            // Find top spender
            String topSpender = null;
            double maxSpent = 0.0;
            for (Map.Entry<String, Double> entry : spenders.entrySet()) {
                if (entry.getValue() > maxSpent) {
                    maxSpent = entry.getValue();
                    topSpender = entry.getKey();
                }
            }

            if (topSpender != null) {
                System.out.println("The top spender on " + date + " is " + topSpender + " with a total of $" + maxSpent);
            } else {
                System.out.println("No orders found for the given date.");
            }

        } catch (IOException e) {
            System.out.println("Error reading the customer orders file: " + e.getMessage());
        }
    }
}

// FoodOrderSystem class as previously defined...
