package com.digitaldentic.sistemaodontologico;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DigitalDenticApplicationTest {

    @Test
    void contextLoads() {
        DigitalDenticApplication.main(new String[]{}); // Si Spring levanta el contexto sin errores, el test pasa
    }
}