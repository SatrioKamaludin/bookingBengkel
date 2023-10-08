package com.bengkel.booking;

import com.bengkel.booking.services.MenuService;

public class App 
{
    public static void main( String[] args )
    {
        MenuService menuService = new MenuService();
        menuService.run();
    }
}
