package com.food.kart.foodkart.service;

import com.food.kart.foodkart.dao.RestaurantDao;
import com.food.kart.foodkart.dao.UserDao;
import com.food.kart.foodkart.model.Restaurant;
import com.food.kart.foodkart.strategy.PriceStrategy;
import com.food.kart.foodkart.strategy.RatingStrategy;
import com.food.kart.foodkart.strategy.RestaurantDisplayStrategy;

import java.util.ArrayList;
import java.util.List;

public class FoodKartRestaurantServiceImpl implements FoodKartRestaurantService {
    RestaurantDao restaurantDao;
    UserDao userDao;

    public FoodKartRestaurantServiceImpl() {
        this.restaurantDao = RestaurantDao.getInstance();
        this.userDao = UserDao.getInstance();
    }

    @Override
    public void registerRestaurant(String restaurantName, String listOfPincodes, String foodName, double price,
        int quantity) {
        Restaurant restaurant = new Restaurant(restaurantName, listOfPincodes.split("[/]"), foodName, price, quantity);
        this.restaurantDao.addRestaurant(restaurant);
    }

    @Override
    public void updateQuantity(String restaurantName, int quantityToAdd) {
        Restaurant restaurant = restaurantDao.getRestaurant(restaurantName);
        if (restaurant != null) {
            restaurant.updateQuantity(quantityToAdd);
        }
    }

    @Override
    public void rateRestaurant(String restaurantName, int rating, String comment) {
        Restaurant restaurant = restaurantDao.getRestaurant(restaurantName);
        restaurant.addComments(rating, comment);
    }

    @Override
    public List<Restaurant> showRestaurant(String sortBy) {
        RestaurantDisplayStrategy strategy = null;
        if (sortBy.equals("rating")) {
            strategy = new RatingStrategy();
        }
        if (sortBy.equals("price")) {
            strategy = new PriceStrategy();
        }
        if (strategy != null)
            return strategy.findRestaurants(restaurantDao.getListOfRestaurants(), userDao.getCurrentLoginUser());
        return new ArrayList<>();
    }

    @Override
    public boolean placeOrder(String restaurantName, int quantity) {
        Restaurant restaurant = restaurantDao.getRestaurant(restaurantName);
        if (restaurant != null) {
            return restaurant.placeOrder(quantity);
        }
        return false;
    }
}
