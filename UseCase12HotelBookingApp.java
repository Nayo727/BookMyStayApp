/**
 * UseCase12HotelBookingApp.java
 * 
 * Demonstrates persistence and system recovery in the Book My Stay App.
 * Booking history and room inventory are serialized to disk before shutdown
 * and restored on startup to ensure continuity of system state.
 * 
 * @author YourName
 * @version 1.0
 */

import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               " | Guest: " + guestName +
               " | Room: " + roomType;
    }
}

// Serializable RoomInventory
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        availabilityMap.put(roomType, count);
    }

    public boolean allocateRoom(String roomType) {
        int available = availabilityMap.getOrDefault(roomType, 0);
        if (available > 0) {
            availabilityMap.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void incrementAvailability(String roomType) {
        availabilityMap.put(roomType, availabilityMap.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("===== Inventory =====");
        for (String roomType : availabilityMap.keySet()) {
            System.out.println(roomType + " : Available = " + availabilityMap.get(roomType));
        }
        System.out.println("=====================");
    }
}

// BookingHistory storing Reservations
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation res) {
        history.add(res);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }

    public void displayAllBookings() {
        System.out.println("\n===== Booking History =====");
        if (history.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Reservation res : history) {
                System.out.println(res);
            }
        }
        System.out.println("===========================\n");
    }
}

// PersistenceService handles saving and loading system state
class PersistenceService {

    public static void saveState(RoomInventory inventory, BookingHistory history, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(inventory);
            out.writeObject(history);
            System.out.println("System state saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save system state: " + e.getMessage());
        }
    }

    public static Object[] loadState(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Persistence file not found. Starting with fresh state.");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();
            System.out.println("System state loaded successfully from " + filename);
            return new Object[] { inventory, history };
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load system state. Starting fresh: " + e.getMessage());
            return null;
        }
    }
}

// Main application class
public class UseCase12HotelBookingApp {
    private static final String STATE_FILE = "hotel_state.ser";

    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Data Persistence & System Recovery =====\n");

        // Attempt to load persisted state
        Object[] loadedState = PersistenceService.loadState(STATE_FILE);
        RoomInventory inventory;
        BookingHistory bookingHistory;

        if (loadedState != null) {
            inventory = (RoomInventory) loadedState[0];
            bookingHistory = (BookingHistory) loadedState[1];
        } else {
            // Initialize fresh state
            inventory = new RoomInventory();
            inventory.addRoomType("Single Room", 3);
            inventory.addRoomType("Double Room", 2);
            inventory.addRoomType("Suite Room", 1);

            bookingHistory = new BookingHistory();

            // Add a sample reservation
            Reservation res = new Reservation("S100", "Alice", "Single Room");
            bookingHistory.addReservation(res);
            inventory.allocateRoom(res.getRoomType());
        }

        // Display current state
        inventory.displayInventory();
        bookingHistory.displayAllBookings();

        // Simulate shutdown: persist state
        PersistenceService.saveState(inventory, bookingHistory, STATE_FILE);

        System.out.println("\nSystem shutdown completed. Next startup will restore state.");
    }
}
