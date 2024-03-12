package com.example.integration;

import com.example.service.MessageService;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MainIT {

    @Test
    public void testMessageServiceIntegration() {
        MessageService service = new MessageService();
        String message = service.fetchMessage();

        assertNotNull("Message should not be null", message);
        assertTrue("Message should contain 'Hello from MessageService'", message.contains("Hello from MessageService"));
    }
}
