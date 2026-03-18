/**
 * UseCase7HotelBookingApp.java
 * 
 * This class demonstrates add-on service selection for confirmed reservations
 * in the Hotel Booking Management System. Optional services are attached
 * to reservations without modifying core booking or inventory logic.
 * 
 * @author YourName
 * @version 1.0
 */

import java.util.*;

// Represents a single optional service
class Service {
    private String serviceName;
    private double serviceCost;

    public Service(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + serviceCost + ")";
    }
}

// Add-On Service Manager: maps reservations to selected services
class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach a service to a reservation
    public void addServiceToReservation(String reservationId, Service service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
        System.out.println("Added " + service + " to Reservation ID: " + reservationId);
    }

    // Get all services for a reservation
    public List<Service> getServicesForReservation(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList());
    }

    // Calculate total additional cost for a reservation
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0.0;
        for (Service s : getServicesForReservation(reservationId)) {
            total += s.getServiceCost();
        }
        return total;
    }

    // Display all services per reservation
    public void displayAllServices() {
        System.out.println("\n===== Add-On Services for Reservations =====");
        for (String resId : reservationServices.keySet()) {
            System.out.println("Reservation ID: " + resId);
            List<Service> services = reservationServices.get(resId);
            for (Service s : services) {
                System.out.println("  - " + s);
            }
            System.out.println("  Total Additional Cost: $" + calculateTotalServiceCost(resId) + "\n");
        }
        System.out.println("===========================================\n");
    }
}

// Main application class
public class UseCase7HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("===== Book My Stay App: Add-On Service Selection =====\n");

        // Simulate confirmed reservations with unique IDs
        String res1 = "S100"; // Single Room assigned to Alice
        String res2 = "SU101"; // Suite Room assigned to Bob
        String res3 = "D102"; // Double Room assigned to Charlie

        // Initialize Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define available services
        Service breakfast = new Service("Breakfast", 15.0);
        Service spa = new Service("Spa Access", 50.0);
        Service airportPickup = new Service("Airport Pickup", 30.0);

        // Guests select services
        serviceManager.addServiceToReservation(res1, breakfast);
        serviceManager.addServiceToReservation(res1, airportPickup);
        serviceManager.addServiceToReservation(res2, spa);
        serviceManager.addServiceToReservation(res2, breakfast);
        serviceManager.addServiceToReservation(res3, breakfast);

        // Display all attached services and total additional costs
        serviceManager.displayAllServices();

        System.out.println("Add-on service selection completed without modifying core booking or inventory state.\n");
        System.out.println("Thank you for using Book My Stay App!");
    }
}
