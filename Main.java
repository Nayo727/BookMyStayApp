import java.util.*;

// Booking Request
class BookingRequest {
    String requestId;
    String roomType;

    public BookingRequest(String requestId, String roomType) {
        this.requestId = requestId;
        this.roomType = roomType;
    }
}

// Reservation
class Reservation {
    String reservationId;
    String roomId;
    String roomType;

    public Reservation(String reservationId, String roomId, String roomType) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.roomType = roomType;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Room ID: " + roomId +
               ", Room Type: " + roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToRooms = new HashMap<>();

    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Add booking request
    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Process booking request (FIFO)
    public Reservation processRequest() {

        BookingRequest request = requestQueue.poll();

        if (request == null) {
            System.out.println("No requests available.");
            return null;
        }

        String roomType = request.roomType;

        // Atomic allocation block
        synchronized (this) {

            if (inventoryService.getAvailableRooms(roomType) <= 0) {
                System.out.println("No rooms available for " + roomType);
                return null;
            }

            // Generate unique room ID
            String roomId = generateRoomId(roomType);

            // Store allocated room
            allocatedRoomIds.add(roomId);

            roomTypeToRooms
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            // Update inventory immediately
            inventoryService.decrementRoom(roomType);

            // Confirm reservation
            Reservation reservation = new Reservation(
                    UUID.randomUUID().toString(),
                    roomId,
                    roomType
            );

            System.out.println("Booking Confirmed → " + reservation);
            return reservation;
        }
    }

    // Unique Room ID Generator
    private String generateRoomId(String roomType) {
        String roomId;

        do {
            int number = 100 + new Random().nextInt(900);
            roomId = roomType.charAt(0) + String.valueOf(number);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Deluxe", 2);
        inventory.addRoomType("Suite", 1);

        BookingService bookingService = new BookingService(inventory);

        // Add requests (FIFO)
        bookingService.addRequest(new BookingRequest("REQ1", "Deluxe"));
        bookingService.addRequest(new BookingRequest("REQ2", "Deluxe"));
        bookingService.addRequest(new BookingRequest("REQ3", "Deluxe")); // should fail
        bookingService.addRequest(new BookingRequest("REQ4", "Suite"));

        // Process all requests
        for (int i = 0; i < 5; i++) {
            bookingService.processRequest();
        }
    }
}
