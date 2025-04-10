package com.hotel.managment.system.models;

import com.hotel.managment.system.enums.Amenity;
import com.hotel.managment.system.utils.Currency;

import java.util.HashMap;
import java.util.Map;

public class CatalogDetails {

    Map<Amenity, Currency> amenityToCurrency;

    public CatalogDetails() {
        this.amenityToCurrency = new HashMap<>();
        populate();
    }

    private void populate() {
        amenityToCurrency.put(Amenity.DRINK, new Currency(10));
        amenityToCurrency.put(Amenity.FOOD, new Currency(20));
        amenityToCurrency.put(Amenity.ROOM_CLEANING, new Currency(30));
    }
}
