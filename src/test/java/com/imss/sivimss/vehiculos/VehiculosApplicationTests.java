package com.imss.sivimss.vehiculos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class VehiculosApplicationTests {
    @Test
    void contextLoads() {
        String result="test";
        VehiculosApplication.main(new String[]{});
        assertNotNull(result);
    }
}
