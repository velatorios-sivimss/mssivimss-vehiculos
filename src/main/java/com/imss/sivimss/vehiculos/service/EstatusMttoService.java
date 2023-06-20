package com.imss.sivimss.vehiculos.service;

import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Date;

public interface EstatusMttoService {
    void validarEstatusMtto(Authentication authentication, Date fechaMtto) throws IOException;
}
