package com.java.design.patterns.domain.User;

public class Attendant extends BaseUser {
    String employeeId;
    int floorNumber;
    int exitGateNumber;

    public Attendant(String name) {
        super(name);
    }
}
