package com.java.lld.design;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangeDataCaptureFlowTest {

    @Test
    public void shouldCaptureInsertUpdateAndDeleteEvents() {
        ChangeCaptureService captureService = new ChangeCaptureService();
        DemoConsumer consumer = new DemoConsumer();
        captureService.subscribe(consumer);

        DataStore dataStore = new DataStore(captureService);
        Customer customer = new Customer("1", "Alice", "alice@example.com");

        dataStore.create(customer);
        dataStore.update("1", "Alice Updated", "alice.updated@example.com");
        dataStore.delete("1");

        assertEquals(3, captureService.getChangeLog().size());
        assertEquals(ChangeType.INSERT, captureService.getChangeLog().get(0).getChangeType());
        assertEquals(ChangeType.UPDATE, captureService.getChangeLog().get(1).getChangeType());
        assertEquals(ChangeType.DELETE, captureService.getChangeLog().get(2).getChangeType());
        assertEquals(3, consumer.getReceivedEvents().size());
        assertNotNull(captureService.getChangeLog().get(2).getBeforeState());
        assertEquals(null, captureService.getChangeLog().get(2).getAfterState());
    }
}
