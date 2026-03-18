/**
 * UseCase9HotelBookingApp.java
 * 
 * This class demonstrates structured validation and error handling
 * for the Hotel Booking Management System. It validates room types
 * and inventory before confirming reservations, throwing custom
 * exceptions on invalid input while keeping the system stable.
 * 
 * @author YourName
 * @version 1.0
 */

import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class representing a booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Requested Room: " + roomType;
    }
}

// Centralized Room Inventory with validation
class RoomInventory {
    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) throws InvalidBookingException {
        if (count < 0) throw new InvalidBookingException("Room count cannot be negative.");
        availabilityMap.put(roomType, count);
    }

    public boolean isRoomTypeValid(String roomType) {
        return availabilityMap.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) throws InvalidBookingException {
        int current = getAvailability(roomType);
        if (current <= 0) {
            throw new InvalidBookingException("No available rooms for type: " + roomType);
        }
        availabilityMap.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");
        for (String roomType : availabilityMap.keySet()) {
            System.out.println(roomType + " : Available = " + availabilityMap.get(roomType));
        }
        System.out.println("=================================");
    }
}

// Validator for booking requests
class BookingValidator {
    private RoomInventory inventory;

    public BookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void validate(Reservation reservation) throws InvalidBookingException {
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!inventory.isRoomTypeValid(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type requested: " + reservation.getRoomType());
        }
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + reservation.getRoomType());
        }
    }
}

// Booking Service with validation
class BookingService {
    private RoomInventory inventory;
    private BookingValidator validator;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator(inventory);
    }

    public void processReservation(Reservation res) {
        try {
            // Validate before processing
            validator.validate(res);

            // Allocate room
            inventory.decrementAvailability(res.getRoomType());
            System.out.println("Booking confirmed for " + res.getGuestName() +
                               " (" + res.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main application class
public class UseCase9HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Error Handling & Validation =====\n");

        try {
            RoomInventory inventory = new RoomInventory();
            inventory.addRoomType("Single Room", 2);
            inventory.addRoomType("Double Room", 1);
            inventory.addRoomType("Suite Room", 0); // simulate fully booked

            inventory.displayInventory();

            BookingService bookingService = new BookingService(inventory);

            // Valid booking
            bookingService.processReservation(new Reservation("Alice", "Single Room"));

            // Invalid room type
            bookingService.processReservation(new Reservation("Bob", "Triple Room"));

            // Empty guest name
            bookingService.processReservation(new Reservation("", "Double Room"));

            // No availability
            bookingService.processReservation(new Reservation("Charlie", "Suite Room"));

        } catch (InvalidBookingException e) {
            System.out.println("System initialization error: " + e.getMessage());
        }

        System.out.println("\nSystem continues running safely after handling errors.");
        System.out.println("Thank you for using Book My Stay App!");
    }
}
