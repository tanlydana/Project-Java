package PACKAGE_NAME;// FoodOrderSystem.java
import java.io.*;
import java.util.*;

public class FoodOrderSystem {
    private static final String MENU_FILE = "assets/menu.txt";
    private final Scanner scanner = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new HashMap<>();
    private final  Map<Integer, Double> prices = new HashMap<>();
    private final  Map<String, Integer> order = new HashMap<>();
    private final  Map<Integer, Integer> cookingTimes = new HashMap<>();

    public void startOrdering() {
        loadMenu();
        boolean ordering = true;

        while (ordering) {
            displayMenu();
            selectFood(); // Let the user select an item and its quantity
            ordering = askToAddMore(); // Ask if the user wants to add more items
        }

        double totalAmount = displaySummary(); // Display the order summary
        confirmOrder(totalAmount); // Confirm the order
    }

    private void loadMenu() {
        try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue; // Ensure we have ID, Name, Price

                int itemId = Integer.parseInt(parts[0].trim()); // Read ID
                String itemName = parts[1].trim(); // Read Food Name
                double itemPrice = Double.parseDouble(parts[2].trim());
                int cookingTime = Integer.parseInt(parts[3].trim()); // Read Price

                menuItems.put(itemId, itemName);
                prices.put(itemId, itemPrice);
                cookingTimes.put(itemId, cookingTime);

            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
    }

    private void displayMenu() {
        System.out.println("\n------------------- FOOD MENU --------------------\n");
        for (int id : menuItems.keySet()) {
            System.out.printf("| %-5d | %-20s | $%-6.2f | %-2d min |\n",
                    id, menuItems.get(id), prices.get(id), cookingTimes.get(id));
        }
        System.out.println("---------------------------------------------------");
    }

    private void selectFood() {
        boolean validSelection = false;
        while (!validSelection) {
            System.out.print("Enter item number: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Try again.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            if (!menuItems.containsKey(choice)) {
                System.out.println("Invalid selection.");
                continue;
            }

            System.out.print("Enter quantity: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid quantity.");
                scanner.next();
                continue;
            }
            int quantity = scanner.nextInt();
            order.put(menuItems.get(choice), order.getOrDefault(menuItems.get(choice), 0) + quantity);

            validSelection = true; // Exit the loop if the input is valid
        }
    }

    private boolean askToAddMore() {
        System.out.print("Do you need anything else? (yes/no): ");
        String response = scanner.next().trim().toLowerCase();
        if (response.equals("yes")) {
            return true; // User wants to add more, so the loop continues
        } else if (response.equals("no")) {
            return false; // User doesn't want to add more, so exit loop
        } else {
            System.out.println("Invalid input. Please respond with 'yes' or 'no'.");
            return askToAddMore(); // Retry if the input is invalid
        }
    }

    private double displaySummary() {
        double total = 0;
        System.out.println("\nORDER SUMMARY");
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue();
            double price = prices.get(getKeyByValue(menuItems, item));
            double itemTotal = price * quantity;
            System.out.printf("%s x%d - $%.2f\n", item, quantity, itemTotal);
            total += itemTotal;
        }
        System.out.printf("Total: $%.2f\n", total);
        return total;
    }

    private void confirmOrder(double total) {
        System.out.print("Confirm order? (yes/no): ");
        if (scanner.next().trim().equalsIgnoreCase("yes")) {
            System.out.printf("Final Total: $%.2f\n", total);
            System.out.println("Thank you! Please pay and pick up your food at the canteen by showing your student ID ><");

        } else {
            System.out.println("Order canceled.");
        }
    }

    private int getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) return entry.getKey();
        }
        return -1;
    }

    public static void main(String[] args) {
        FoodOrderSystem system = new FoodOrderSystem();
        system.startOrdering();
    }
}