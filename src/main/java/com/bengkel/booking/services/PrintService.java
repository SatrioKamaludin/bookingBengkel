package com.bengkel.booking.services;

import java.util.List;
import java.util.stream.Collectors;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.models.ItemService;

public class PrintService {

	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";

		System.out.printf("%-25s %n", title);
		System.out.println(line);

		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			} else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}

	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Vechicle Id", "Warna", "Brand", "Transmisi", "Tahun", "Tipe Kendaraan");
		System.out.format(line);
		int number = 1;
		String vehicleType = "";
		for (Vehicle vehicle : listVehicle) {
			if (vehicle instanceof Car) {
				vehicleType = "Mobil";
			} else {
				vehicleType = "Motor";
			}
			System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(),
					vehicle.getTransmisionType(), vehicle.getYearRelease(), vehicleType);
			number++;
		}
		System.out.printf(line);
	}

	public static void printCustomerData(Customer customer) {
		System.out.println("Customer Id: " + customer.getCustomerId());
		System.out.println("Nama: " + customer.getName());
		System.out.println("Alamat: " + customer.getAddress());
		if (customer instanceof MemberCustomer) {
			System.out.println("Membership: Member");
			double saldoCoin = ((MemberCustomer) customer).getSaldoCoin();
			System.out.printf("Saldo Coin: %.2f%n", saldoCoin);
		} else {
			System.out.println("Membership: Non Member");
		}
		List<Vehicle> vehicles = customer.getVehicles();
		if (vehicles.size() > 0) {
			printVechicle(vehicles);
		}
	}

	public static void printServices(List<ItemService> listServices) {
		int number = 1;
		System.out.println("+------------------------------------------------------------------+");
		System.out.printf("| %-3s | %-10s | %-15s | %-14s | %-10s |\n",
				"No.", "Service Id", "Nama Service", "Tipe Kendaraan", "Harga");
		System.out.println("+------------------------------------------------------------------+");
		for (ItemService service : listServices) {
			System.out.printf("| %-3s | %-10s | %-15s | %-14s | %-10s |\n",
					number, service.getServiceId(), service.getServiceName(), service.getVehicleType(),
					service.getPrice());
			number++;
		}
		System.out.println("+------------------------------------------------------------------+");
	}

	public static void printBookingOrders(List<BookingOrder> bookingOrders) {
		int number = 1;
		System.out.println(
				"+----+-------------------+----------------+----------------+---------------+---------------+---------------------------+");
		System.out.printf("| %-2s | %-17s | %-14s | %-14s | %-13s | %-12s | %-25s |\n",
				"No", "BookingId", "Customer Name", "Payment Method", "Total Service", "Total Payment",
				"List of Services");
		System.out.println(
				"+----+-------------------+----------------+----------------+---------------+---------------+---------------------------+");
		for (BookingOrder bookingOrder : bookingOrders) {
			String listOfServices = bookingOrder.getServices().stream().map(ItemService::getServiceName)
					.collect(Collectors.joining(", "));
			System.out.printf("| %-2s | %-17s | %-14s | %-14s | %-13.2f | %-12.2f | %-25s |\n",
					number, bookingOrder.getBookingId(), bookingOrder.getCustomer().getName(),
					bookingOrder.getPaymentMethod(), bookingOrder.getTotalServicePrice(),
					bookingOrder.getTotalPayment(), listOfServices);
			number++;
		}
		System.out.println(
				"+----+-------------------+----------------+----------------+---------------+---------------+---------------------------+");

	}
	// Silahkan Tambahkan function print sesuai dengan kebutuhan.

}
