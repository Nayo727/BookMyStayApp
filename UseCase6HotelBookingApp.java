/**
 * UseCase6HotelBookingApp.java
 * 
 * This class demonstrates reservation confirmation and room allocation
 * for the Hotel Booking Management System. It dequeues booking requests,
 * assigns unique room IDs, updates inventory, and prevents double-booking.
 * 
 * @author YourName
 * @version 1.0
 */

import java.util.*;

// Reservation represents a guest booking request
class Reservation {
    private String guestName;
    private String requestedRoomType;

    public Reservation(String guestName, String requestedRoomType) {
        this.guestName = guestName;
        this.requestedRoomType = requestedRoomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRequestedRoomType() {
        return requestedRoomType;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Requested Room: " + requestedRoomType;
    }
}

// Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void submitRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request submitted: " + reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

// Centralized Room Inventory
class RoomInventory {
    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        availabilityMap.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public boolean decrementAvailability(String roomType) {
        int current = availabilityMap.getOrDefault(roomType, 0);
        if (current > 0) {
            availabilityMap.put(roomType, current - 1);
            return true;
        } else {
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");
        for (String roomType : availabilityMap.keySet()) {
            System.out.println(roomType + " : Available = " + availabilityMap.get(roomType));
        }
        System.out.println("=================================");
    }
}

// Booking Service: processes requests and allocates unique rooms
class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRoomIds; // roomType -> set of room IDs
    private int roomIdCounter;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashMap<>();
        roomIdCounter = 100; // starting room ID
    }

    // Process a single reservation
    public void processReservation(Reservation res) {
        String roomType = res.getRequestedRoomType();
        int available = inventory.getAvailability(roomType);

        if (available <= 0) {
            System.out.println("Sorry " + res.getGuestName() + ", no " + roomType + " available.");
            return;
        }

        // Generate unique room ID
        String roomId = generateUniqueRoomId(roomType);

        // Assign room and decrement inventory
        if (inventory.decrementAvailability(roomType)) {
            System.out.println("Reservation confirmed for " + res.getGuestName() +
                               ". Assigned Room ID: " + roomId + " (" + roomType + ")");
        } else {
            System.out.println("Error: Unable to allocate room for " + res.getGuestName());
        }
    }

    // Generate unique room ID
    private String generateUniqueRoomId(String roomType) {
        allocatedRoomIds.putIfAbsent(roomType, new HashSet<>());
        String roomId;
        do {
            roomId = roomType.substring(0, 1).toUpperCase() + roomIdCounter++;
        } while (allocatedRoomIds.get(roomType).contains(roomId));
        allocatedRoomIds.get(roomType).add(roomId);
        return roomId;
    }
}

// Main application class
public class UseCase6HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Reservation Confirmation & Room Allocation =====\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        bookingQueue.submitRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.submitRequest(new Reservation("Bob", "Suite Room"));
        bookingQueue.submitRequest(new Reservation("Charlie", "Double Room"));
        bookingQueue.submitRequest(new Reservation("Diana", "Single Room"));
        bookingQueue.submitRequest(new Reservation("Ethan", "Single Room")); // exceeds availability

        System.out.println();

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Process booking requests in FIFO order
        while (!bookingQueue.isEmpty()) {
            Reservation res = bookingQueue.getNextRequest();
            bookingService.processReservation(res);
        }

        System.out.println();
        inventory.displayInventory();

        System.out.println("\nThank you for using Book My Stay App!");
    }
}
