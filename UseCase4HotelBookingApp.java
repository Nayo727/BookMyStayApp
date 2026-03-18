import java.util.HashMap;
import java.util.Map;

/**
 * UseCase4HotelBookingApp
 * 
 * Demonstrates room search functionality with read-only access.
 * Only available rooms are displayed without modifying inventory.
 * 
 * @author YourName
 * @version 1.0
 */

// -------------------- DOMAIN MODEL --------------------
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type   : " + roomType);
        System.out.println("Beds        : " + numberOfBeds);
        System.out.println("Price/Night : $" + pricePerNight);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// -------------------- INVENTORY (STATE HOLDER) --------------------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Read-only method
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Expose entire inventory (read-only usage)
    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

// -------------------- SEARCH SERVICE --------------------
class RoomSearchService {

    public void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {

        System.out.println("\n--- Available Rooms ---");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Rooms : " + available);
                System.out.println("-----------------------------");
            }
        }
    }
}

// -------------------- MAIN APPLICATION --------------------
public class UseCase4HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" Book My Stay App - Room Search ");
        System.out.println("========================================");

        // Initialize rooms (domain objects)
        Room[] rooms = {
            new SingleRoom(),
            new DoubleRoom(),
            new SuiteRoom()
        };

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 0); // Not available
        inventory.addRoomType("Suite Room", 2);

        // Perform search (read-only)
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println("\nSearch completed. No changes made to inventory.");
    }
}
