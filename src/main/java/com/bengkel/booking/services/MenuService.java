package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private List<BookingOrder> bookingOrders = new ArrayList<>();
	private Scanner input = new Scanner(System.in);
	private int loginAttempts = 3;
	private Customer loggedInCustomer = null;

	public void run() {
        boolean isLooping = true;
        do {
            int choice = loginMenu();
            switch (choice) {
                case 1:
                    if (login()) {
                        mainMenu();
                    } else {
                        loginAttempts--;
                        System.out.println("Username dan Password tidak cocok, anda memiliki kesempatan login "
                                + loginAttempts + " kali");
                        if (loginAttempts == 0) {
                            System.out.println(
                                    "Anda telah menghabiskan semua kesempatan login, mengeluarkan anda dari program...");
                            System.exit(0);
                        }
                    }
                    break;
                case 0:
                    System.out.println("Exiting program");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Pilihan invalid, mohon input 1 untuk login atau 0 untuk exit");
            }
        } while (isLooping);
    }

	public int loginMenu() {
        int choice = -1;
        boolean isOptionValid = false;
        do {
            try {
                System.out.println("Aplikasi Booking Bengkel");
                System.out.println("1. Login");
                System.out.println("0. Exit");
                System.out.print("Masukan Pilihan: ");
                choice = input.nextInt();
                isOptionValid = true;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Pilihan invalid, mohon input 1 untuk login atau 0 untuk exit");
                input.nextLine();
            }
        } while (!isOptionValid);
        return choice;
    }

	public boolean login() {
        System.out.print("\nMasukkan Customer Id: ");
        String customerId = input.next();
        System.out.print("Masukkan Password: ");
        String password = input.next();
        for (Customer customer : listAllCustomers) {
            if (customer.getCustomerId().equals(customerId) && customer.getPassword().equals(password)) {
                loggedInCustomer = customer;
                loginAttempts = 3;
                return true;
            }
        }
        return false;
    }

	public void mainMenu() {
        System.out.println("Selamat datang di Aplikasi Booking Bengkel, " + loggedInCustomer.getName() + "!");
        String[] listMenu = { "Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking",
                "List servis", "Logout" };
        int menuChoice = 0;
        boolean isLooping = true;

        do {
            PrintService.printMenu(listMenu, "Booking Bengkel Menu");
            menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu: ", "Input Harus Berupa Angka!",
                    "^[0-9]+$", listMenu.length - 1, 0);
            System.out.println(menuChoice);

            switch (menuChoice) {
                case 1:
                    // panggil fitur Informasi Customer
                    BengkelService.printCustomerData(loggedInCustomer);
                    break;
                case 2:
                    // panggil fitur Booking Bengkel
                    BengkelService.booking(loggedInCustomer, bookingOrders);
                    break;
                case 3:
                    // panggil fitur Top Up Saldo Coin
                    BengkelService.addSaldoCoin(loggedInCustomer);
                    break;
                case 4:
                    // panggil fitur Informasi Booking Order
                    BengkelService.printBookingOrders(bookingOrders);
                    break;
                case 5:
                    // panggil fitur List Service
                    PrintService.printServices(listAllItemService);
                    break;
                default:
                    System.out.println("Logout");
                    isLooping = false;
                    break;
            }
        } while (isLooping);
    }

	// Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}