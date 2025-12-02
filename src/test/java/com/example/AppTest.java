package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    
    @Test
    public void testAddition() {
        assertEquals(4, 2 + 2);
    }
    
    @Test
    public void testAppHasMethod() {
        App app = new App();
        assertNotNull(app);
    }
}
