package com.bengkel.booking.services;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.ItemServiceRepository;
import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Motorcyle;

public class BengkelService {
	private static Scanner input = new Scanner(System.in);

	// Silahkan tambahkan fitur-fitur utama aplikasi disini

	// Login

	// Info Customer
	public static void printCustomerData(Customer loggedInCustomer) {
		PrintService.printCustomerData(loggedInCustomer);
	}

	// Booking atau Reservation
	public static void booking(Customer loggedInCustomer, List<BookingOrder> bookingOrders) {
		PrintService.printVechicle(loggedInCustomer.getVehicles());
		String vehicleId;
		boolean validVehicleId = false;

		List<ItemService> selectedServices = new ArrayList<>();

		while (!validVehicleId) {
			System.out.print("Masukkan Vehicle Id: ");
			vehicleId = input.nextLine();
			validVehicleId = false; // Reset the flag for each new input

			for (Vehicle vehicle : loggedInCustomer.getVehicles()) {
				if (vehicleId.equals(vehicle.getVehiclesId())) {
					System.out.println("Vehicle Id ditemukan!");
					validVehicleId = true;
					List<ItemService> allItemServices = ItemServiceRepository.getAllItemService();
					String vehicleType = (vehicle instanceof Car) ? "Mobil" : "Motor";
					List<ItemService> availableServices = getItemServicesByVehicleType(vehicleType, allItemServices);
					PrintService.printServices(availableServices);

					// Determine the maximum number of services allowed based on membership status
					int maxServicesAllowed = (loggedInCustomer instanceof MemberCustomer) ? 2 : 1;
					int servicesAdded = 0;
					boolean addMoreServices = true;
					double totalBiaya = 0.0;

					while (addMoreServices && servicesAdded < maxServicesAllowed) {
						System.out.print("Silahkan masukan Service Id: ");
						String serviceId = input.nextLine();

						// Check if the entered serviceId exists in availableServices
						boolean validServiceId = false;
						for (ItemService service : availableServices) {
							if (serviceId.equals(service.getServiceId())) {
								validServiceId = true;
								// Perform booking logic here
								System.out.println("Service " + service.getServiceName() + " berhasil ditambahkan.");
								totalBiaya += service.getPrice();
								servicesAdded++;
								selectedServices.add(service);
								break;
							}
						}

						if (!validServiceId) {
							System.out.println("Service Id yang dimasukkan tidak valid.");
						}

						if (servicesAdded < maxServicesAllowed) {
							System.out.print("Apakah anda ingin menambahkan Service Lainnya? (Y/T): ");
							String addMoreInput = input.nextLine();
							addMoreServices = addMoreInput.equalsIgnoreCase("Y");
						}
					}

					if (servicesAdded == maxServicesAllowed) {
						System.out.println("Anda telah mencapai batas maksimum jumlah service.");
					}

					boolean validPaymentMethod = false;
					while (!validPaymentMethod) {
						System.out.println("Silahkan Pilih Metode Pembayaran (Saldo Coin atau Cash)");
						String paymentMethod = input.nextLine();
						double totalPembayaran = 0;
						if (paymentMethod.equalsIgnoreCase("Saldo Coin")) {
							if (loggedInCustomer instanceof MemberCustomer) {
								MemberCustomer memberCustomer = (MemberCustomer) loggedInCustomer;
								double saldoCoin = memberCustomer.getSaldoCoin();
								

								System.out.println(
										"Selamat, anda mendapatkan diskon sebesar 10% dari penggunaan saldo coin!!!");
								totalPembayaran = totalBiaya * 0.9;
								System.out.println("Total Biaya: " + totalPembayaran);

								if (saldoCoin < totalBiaya) {
									System.out.println("Saldo Coin anda kurang dari total biaya booking.");
									break;
								}

								saldoCoin -= totalPembayaran;
								memberCustomer.setSaldoCoin(saldoCoin);
								System.out.println("Pembayaran Berhasil menggunakan saldo coin");
								System.out.println("Saldo Coin anda: " + memberCustomer.getSaldoCoin());
								validPaymentMethod = true;

								int bookingIdCounter = bookingOrders.size() + 1;
								String bookingId = "Book-" + loggedInCustomer.getCustomerId() + "-"
										+ String.format("%03d", bookingIdCounter);
								BookingOrder bookingOrder = new BookingOrder(bookingId, loggedInCustomer,
										selectedServices, paymentMethod, totalBiaya, totalPembayaran);
								bookingOrders.add(bookingOrder);
								System.out.println("Booking Order berhasil ditambahkan dengan ID: " + bookingId);

							} else {
								System.out.println("Maaf, fitur ini hanya untuk member saja!");
							}
						} else if (paymentMethod.equalsIgnoreCase("Cash")) {
							totalPembayaran = totalBiaya;
							System.out.println("Total Biaya: " + totalPembayaran);
							System.out.println("Pembayaran Berhasil menggunakan Cash");
							validPaymentMethod = true;
							int bookingIdCounter = bookingOrders.size() + 1;
							String bookingId = "Book-" + loggedInCustomer.getCustomerId() + "-"
									+ String.format("%03d", bookingIdCounter);

							// Create a BookingOrder object and add it to the list
							BookingOrder bookingOrder = new BookingOrder(bookingId, loggedInCustomer, selectedServices,
									paymentMethod, totalBiaya, totalPembayaran);
							bookingOrders.add(bookingOrder);
							System.out.println("Booking Order berhasil ditambahkan dengan ID: " + bookingId);
						} else {
							System.out.println("Metode Pembayaran tidak valid.");
						}
					}
					break;
				}
			}

			if (!validVehicleId) {
				System.out.println("Vehicle Id yang dicari tidak tersedia.");
			}
		}
	}

	// Top Up Saldo Coin Untuk Member Customer
	public static void addSaldoCoin(Customer loggedInCustomer) {
		if (loggedInCustomer instanceof MemberCustomer) {
			boolean isInputValid = false;
			do {
				System.out.println("Masukkan besaran top up: ");
				try {
					double topUpCoin = input.nextDouble();
					if (topUpCoin < 0) {
						throw new java.util.InputMismatchException();
					}
					MemberCustomer memberCustomer = (MemberCustomer) loggedInCustomer;
					memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() + topUpCoin);
					System.out.println("Top up berhasil!");
					System.out.println("Saldo Coin anda: " + ((MemberCustomer) loggedInCustomer).getSaldoCoin());
					isInputValid = true;
				} catch (java.util.InputMismatchException e) {
					System.out.println("Input Harus Berupa Angka positif!");
					input.nextLine();
				}
			} while (!isInputValid);

		} else {
			System.out.println("Maaf, fitur ini hanya untuk member saja!");
		}
	}

	public static void printBookingOrders(List<BookingOrder> bookingOrders) {
		PrintService.printBookingOrders(bookingOrders);
	}

	public static List<ItemService> getItemServicesByVehicleType(String vehicleType,
			List<ItemService> allItemServices) {
		List<ItemService> filteredServices = new ArrayList<>();

		for (ItemService service : allItemServices) {
			if (service.getVehicleType().equalsIgnoreCase(vehicleType)) {
				filteredServices.add(service); // Add the matching service to the filtered list
			}
		}

		return filteredServices;
	}

	public static ItemService findServiceById(String serviceId, List<ItemService> services) {
		for (ItemService service : services) {
			if (service.getServiceId().equalsIgnoreCase(serviceId.trim())) {
				return service;
			}
		}
		return null; // Service not found
	}

	// Logout
}
