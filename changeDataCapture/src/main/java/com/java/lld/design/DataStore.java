package com.java.lld.design;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private final Map<String, Customer> customers = new HashMap<>();
    private final ChangeCaptureService changeCaptureService;

    public DataStore(ChangeCaptureService changeCaptureService) {
        this.changeCaptureService = changeCaptureService;
    }

    public void create(Customer customer) {
        customers.put(customer.getId(), customer);
        changeCaptureService.publish(new ChangeEvent(customer.getId(), ChangeType.INSERT, null, cloneCustomer(customer)));
    }

    public void update(String id, String name, String email) {
        Customer existing = customers.get(id);
        if (existing == null) {
            throw new IllegalArgumentException("Customer not found: " + id);
        }

        Customer before = cloneCustomer(existing);
        existing.setName(name);
        existing.setEmail(email);
        Customer after = cloneCustomer(existing);
        changeCaptureService.publish(new ChangeEvent(id, ChangeType.UPDATE, before, after));
    }

    public void delete(String id) {
        Customer existing = customers.remove(id);
        if (existing == null) {
            throw new IllegalArgumentException("Customer not found: " + id);
        }
        changeCaptureService.publish(new ChangeEvent(id, ChangeType.DELETE, cloneCustomer(existing), null));
    }

    private Customer cloneCustomer(Customer customer) {
        return new Customer(customer.getId(), customer.getName(), customer.getEmail());
    }
}
