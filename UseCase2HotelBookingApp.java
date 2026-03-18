/**
 * UseCase2HotelBookingApp
 * 
 * Demonstrates basic object-oriented design using abstraction,
 * inheritance, and polymorphism for a Hotel Booking System.
 * 
 * It defines different room types and displays their details
 * along with static availability.
 * 
 * @author YourName
 * @version 1.0
 */

// Abstract class
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;

    // Constructor
    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type      : " + roomType);
        System.out.println("Beds           : " + numberOfBeds);
        System.out.println("Price/Night    : $" + pricePerNight);
    }
}

// Single Room class
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

// Double Room class
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

// Suite Room class
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// Main Application Class
public class UseCase2HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App - Room Listing   ");
        System.out.println("=====================================");

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        System.out.println("\n--- Single Room ---");
        single.displayDetails();
        System.out.println("Available Rooms : " + singleAvailable);

        System.out.println("\n--- Double Room ---");
        doubleRoom.displayDetails();
        System.out.println("Available Rooms : " + doubleAvailable);

        System.out.println("\n--- Suite Room ---");
        suite.displayDetails();
        System.out.println("Available Rooms : " + suiteAvailable);

        System.out.println("\nApplication terminated successfully.");
    }
}
