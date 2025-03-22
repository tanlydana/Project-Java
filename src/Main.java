import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String studentFile = "assets/students.txt";
        boolean isMatch = false;

        while (!isMatch) {
            System.out.print("Enter your full name (No Space): ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter your student ID: ");
            String studentId = scanner.nextLine().trim();

            if (checkStudent(studentFile, name, studentId)) {
                System.out.println("\n\tWelcome to the Food Order System!");
                isMatch = true;
            } else {
                System.out.println("Name or student ID does not match. Please try again.");
            }
        }

        FoodOrderSystem foodOrderSystem = new FoodOrderSystem();
        foodOrderSystem.startOrdering();

        scanner.close();
    }

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
