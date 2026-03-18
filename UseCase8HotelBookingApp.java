/**
 * UseCase8HotelBookingApp.java
 * 
 * This class demonstrates booking history tracking and reporting for
 * the Hotel Booking Management System. Confirmed reservations are
 * stored chronologically and can be reviewed or summarized by admin.
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
    private double roomPrice;

    public Reservation(String reservationId, String guestName, String roomType, double roomPrice) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
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

    public double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               " | Guest: " + guestName +
               " | Room: " + roomType +
               " | Price: $" + roomPrice;
    }
}

// BookingHistory class maintains a chronological record of confirmed reservations
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add a confirmed reservation to history
    public void addReservation(Reservation res) {
        history.add(res);
        System.out.println("Added to history: " + res.getReservationId() + " (" + res.getGuestName() + ")");
    }

    // Retrieve all reservations (read-only)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

// BookingReportService generates reports from booking history
class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all confirmed bookings
    public void displayAllBookings() {
        System.out.println("\n===== Booking History =====");
        List<Reservation> reservations = history.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
        System.out.println("===========================\n");
    }

    // Generate a summary report of bookings by room type
    public void generateSummaryByRoomType() {
        Map<String, Integer> summary = new HashMap<>();
        for (Reservation res : history.getAllReservations()) {
            summary.put(res.getRoomType(), summary.getOrDefault(res.getRoomType(), 0) + 1);
        }

        System.out.println("===== Booking Summary by Room Type =====");
        for (String roomType : summary.keySet()) {
            System.out.println(roomType + " : " + summary.get(roomType) + " bookings");
        }
        System.out.println("=======================================\n");
    }
}

// Main application class
public class UseCase8HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Booking History & Reporting =====\n");

        // Initialize booking history
        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed reservations
        Reservation res1 = new Reservation("S100", "Alice", "Single Room", 50.0);
        Reservation res2 = new Reservation("SU101", "Bob", "Suite Room", 150.0);
        Reservation res3 = new Reservation("D102", "Charlie", "Double Room", 90.0);
        Reservation res4 = new Reservation("S103", "Diana", "Single Room", 50.0);

        // Add confirmed bookings to history
        bookingHistory.addReservation(res1);
        bookingHistory.addReservation(res2);
        bookingHistory.addReservation(res3);
        bookingHistory.addReservation(res4);

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Display all bookings
        reportService.displayAllBookings();

        // Generate summary report by room type
        reportService.generateSummaryByRoomType();

        System.out.println("Booking history tracking and reporting completed.\n");
        System.out.println("Thank you for using Book My Stay App!");
    }
}
