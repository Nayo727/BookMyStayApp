import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3HotelBookingApp
 * 
 * Demonstrates centralized room inventory management using HashMap.
 * Replaces scattered variables with a single source of truth.
 * 
 * @author YourName
 * @version 1.0
 */

// Inventory Class (Single Source of Truth)
class RoomInventory {

    // HashMap to store room type -> availability
    private Map<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Method to add or initialize room type
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Method to get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Method to display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Application
public class UseCase3HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" Book My Stay App - Inventory System ");
        System.out.println("========================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Simulate update (e.g., booking happened)
        System.out.println("\nBooking 1 Single Room...");
        int currentSingle = inventory.getAvailability("Single Room");
        inventory.updateAvailability("Single Room", currentSingle - 1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated successfully.");
    }
}
