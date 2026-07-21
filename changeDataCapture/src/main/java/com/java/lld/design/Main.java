package com.java.lld.design;

public class Main {
    public static void main(String[] args) {
        ChangeCaptureService captureService = new ChangeCaptureService();
        DemoConsumer consumer = new DemoConsumer();
        captureService.subscribe(consumer);

        DataStore dataStore = new DataStore(captureService);

        System.out.println("Starting CDC demo...");
        dataStore.create(new Customer("1001", "Alice", "alice@example.com"));
        dataStore.update("1001", "Alice Updated", "alice.updated@example.com");
        dataStore.delete("1001");

        System.out.println("Total captured changes: " + captureService.getChangeLog().size());
    }
}
