package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTest {
    
    @Test
    public void testBasic() {
        assertEquals(2 + 2, 4);
    }
    
    @Test 
    public void testString() {
        String expected = "test";
        assertEquals(expected, "test");
    }
}
