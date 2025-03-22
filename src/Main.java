package PACKAGE_NAME;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String studentFile = "assets/students.txt";
        boolean isMatch = false;

        // Keep asking until the user enters the correct name and ID
        while (!isMatch) {
            System.out.print("Enter your full name (No Space): ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter your student ID: ");
            String studentId = scanner.nextLine().trim();

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
        foodOrderSystem.startOrdering();

        scanner.close();
    }

    // Check student info in the txt file
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
}
