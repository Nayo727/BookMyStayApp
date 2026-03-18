import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase5HotelBookingApp
 * 
 * Demonstrates booking request handling using a Queue (FIFO).
 * Requests are collected and stored in arrival order without
 * modifying inventory.
 * 
 * @author YourName
 * @version 1.0
 */

// -------------------- RESERVATION MODEL --------------------
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Room: " + roomType);
    }
}

// -------------------- BOOKING QUEUE --------------------
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO Order) ---");

        if (queue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (no removal)
    public Reservation peekNext() {
        return queue.peek();
    }
}

// -------------------- MAIN APPLICATION --------------------
public class UseCase5HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" Book My Stay App - Booking Requests ");
        System.out.println("========================================");

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room"));

        // Display queue (FIFO order preserved)
        bookingQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNext();
        if (next != null) {
            next.display();
        }

        System.out.println("\nAll requests stored. No inventory changes made.");
    }
}
