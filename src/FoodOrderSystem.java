// FoodOrderSystem.java
import java.io.*;
import java.util.*;

public class FoodOrderSystem {
    private static final String MENU_FILE = "../assets/menu.csv";
    private final Scanner scanner = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new HashMap<>();
    private final Map<Integer, Double> prices = new HashMap<>();
    private final Map<String, Integer> order = new HashMap<>();

    public void startOrdering() {
        loadMenu();
        boolean ordering = true;

        while (ordering) {
            displayMenu();
            selectFood();
            ordering = askToAddMore();
        }

        double totalAmount = displaySummary();
        confirmOrder(totalAmount);
    }

private void loadMenu() {
    try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE))) {
        br.readLine(); // Skip header
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 3) continue; // Ensure we have ID, Name, Price

            int itemId = Integer.parseInt(parts[0].trim()); // Read ID
            String itemName = parts[1].trim(); // Read Food Name
            double itemPrice = Double.parseDouble(parts[2].trim()); // Read Price

            menuItems.put(itemId, itemName);
            prices.put(itemId, itemPrice);
        }
    } catch (IOException | NumberFormatException e) {
        System.out.println("Error loading menu: " + e.getMessage());
    }
}


    private void displayMenu() {
        System.out.println("\n------ FOOD MENU ------");
        for (int id : menuItems.keySet()) {
            System.out.printf("%d. %s - $%.2f\n", id, menuItems.get(id), prices.get(id));
        }
    }

    private void selectFood() {
        while (true) {
            System.out.print("Enter item number (0 to finish): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Try again.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            if (choice == 0) break;
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
        }
    }

    private boolean askToAddMore() {
        System.out.print("Add more items? (yes/no): ");
        return scanner.next().trim().equalsIgnoreCase("yes");
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
            System.out.println("Thank you! Pick up your order at the canteen.");
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
}
