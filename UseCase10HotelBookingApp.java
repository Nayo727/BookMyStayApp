/**
 * UseCase10HotelBookingApp.java
 * 
 * This class demonstrates safe booking cancellation and inventory rollback
 * in the Hotel Booking Management System. Cancelled reservations restore
 * room availability and maintain consistent system state.
 * 
 * @author YourName
 * @version 1.0
 */

import java.util.*;

// Reservation class representing a confirmed booking
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               " | Guest: " + guestName +
               " | Room: " + roomType;
    }
}

// Centralized Room Inventory with rollback support
class RoomInventory {
    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        availabilityMap.put(roomType, count);
    }

    public boolean isRoomTypeValid(String roomType) {
        return availabilityMap.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public void incrementAvailability(String roomType) {
        availabilityMap.put(roomType, getAvailability(roomType) + 1);
    }

    public void decrementAvailability(String roomType) {
        availabilityMap.put(roomType, getAvailability(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");
        for (String roomType : availabilityMap.keySet()) {
            System.out.println(roomType + " : Available = " + availabilityMap.get(roomType));
        }
        System.out.println("=================================");
    }
}

// BookingHistory class stores confirmed reservations
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation res) {
        history.add(res);
        System.out.println("Added to history: " + res.getReservationId());
    }

    public void removeReservation(String reservationId) {
        history.removeIf(res -> res.getReservationId().equals(reservationId));
    }

    public Reservation getReservationById(String reservationId) {
        for (Reservation res : history) {
            if (res.getReservationId().equals(reservationId)) return res;
        }
        return null;
    }

    public void displayAllBookings() {
        System.out.println("\n===== Booking History =====");
        if (history.isEmpty()) {
            System.out.println("No active bookings.");
        } else {
            for (Reservation res : history) {
                System.out.println(res);
            }
        }
        System.out.println("===========================\n");
    }
}

// Cancellation Service with rollback logic using Stack
class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack; // Tracks cancelled reservation IDs

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {
        Reservation res = history.getReservationById(reservationId);
        if (res == null) {
            System.out.println("Cancellation failed: Reservation ID " + reservationId + " does not exist or already cancelled.");
            return;
        }

        // Rollback: restore inventory and remove from history
        inventory.incrementAvailability(res.getRoomType());
        history.removeReservation(reservationId);
        rollbackStack.push(reservationId);

        System.out.println("Reservation " + reservationId + " cancelled successfully. Inventory restored.");
    }

    public void displayRollbackStack() {
        System.out.println("Cancelled Reservation IDs (LIFO order): " + rollbackStack);
    }
}

// Main application class
public class UseCase10HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Booking Cancellation & Rollback =====\n");

        // Initialize inventory and history
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed reservations
        Reservation res1 = new Reservation("S100", "Alice", "Single Room");
        Reservation res2 = new Reservation("D101", "Bob", "Double Room");
        Reservation res3 = new Reservation("SU102", "Charlie", "Suite Room");

        bookingHistory.addReservation(res1);
        bookingHistory.addReservation(res2);
        bookingHistory.addReservation(res3);

        inventory.decrementAvailability("Single Room");
        inventory.decrementAvailability("Double Room");
        inventory.decrementAvailability("Suite Room");

        inventory.displayInventory();
        bookingHistory.displayAllBookings();

        // Initialize cancellation service
        CancellationService cancellationService = new CancellationService(inventory, bookingHistory);

        // Cancel a reservation
        cancellationService.cancelReservation("D101"); // Bob's reservation
        cancellationService.cancelReservation("S999"); // Non-existent reservation

        inventory.displayInventory();
        bookingHistory.displayAllBookings();

        cancellationService.displayRollbackStack();

        System.out.println("\nBooking cancellation and inventory rollback completed successfully.");
        System.out.println("Thank you for using Book My Stay App!");
    }
}
