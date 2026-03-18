/**
 * UseCase11HotelBookingApp.java
 * 
 * This class simulates concurrent booking requests in a multi-threaded
 * environment. It demonstrates thread safety, synchronized access to shared
 * booking queue and inventory, and prevention of double allocation.
 * 
 * @author YourName
 * @version 1.0
 */

import java.util.*;
import java.util.concurrent.*;

// Reservation class
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
}

// Thread-safe Room Inventory
class RoomInventory {
    private Map<String, Integer> availabilityMap;

    public RoomInventory(Map<String, Integer> initialAvailability) {
        this.availabilityMap = new ConcurrentHashMap<>(initialAvailability);
    }

    // Thread-safe decrement operation
    public synchronized boolean allocateRoom(String roomType) {
        int available = availabilityMap.getOrDefault(roomType, 0);
        if (available > 0) {
            availabilityMap.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("===== Inventory =====");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " : Available = " + entry.getValue());
        }
        System.out.println("=====================");
    }
}

// Booking Queue Processor (thread-safe)
class BookingProcessor implements Runnable {
    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    public BookingProcessor(Queue<Reservation> bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation res;
            synchronized (bookingQueue) {
                res = bookingQueue.poll();
            }
            if (res == null) break; // Queue empty, exit thread

            // Allocate room safely
            boolean allocated = inventory.allocateRoom(res.getRoomType());
            if (allocated) {
                System.out.println(Thread.currentThread().getName() +
                        " allocated " + res.getRoomType() +
                        " to " + res.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " failed to allocate " + res.getRoomType() +
                        " for " + res.getGuestName() + " (No availability)");
            }

            // Simulate processing delay
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
    }
}

// Main Application
public class UseCase11HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Concurrent Booking Simulation =====\n");

        // Initialize shared booking queue
        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Single Room"));
        bookingQueue.add(new Reservation("Diana", "Suite Room"));
        bookingQueue.add(new Reservation("Eve", "Single Room"));

        // Initialize inventory
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single Room", 2);
        initialInventory.put("Double Room", 1);
        initialInventory.put("Suite Room", 1);

        RoomInventory inventory = new RoomInventory(initialInventory);

        // Create a pool of threads simulating multiple guests
        int numThreads = 3;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-" + (i + 1));
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException ignored) {}
        }

        System.out.println("\nAll booking requests processed.");
        inventory.displayInventory();

        System.out.println("\nConcurrent booking simulation completed successfully.");
    }
}
