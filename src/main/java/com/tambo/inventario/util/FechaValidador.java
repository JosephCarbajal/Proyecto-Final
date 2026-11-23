package com.tambo.inventario.util;

import java.time.LocalDate;

public class FechaValidador {

    public boolean validarFecha(LocalDate fecha) {
        return fecha != null;
    }

    public boolean fechaVencida(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            return false;
        }

        return fechaVencimiento.isBefore(LocalDate.now());
    }

    public boolean venceHoy(LocalDate fechaVencimiento) {
        return fechaVencimiento != null
                && fechaVencimiento.isEqual(LocalDate.now());
    }

    public boolean fechaEsFutura(LocalDate fecha) {
        return fecha != null && fecha.isAfter(LocalDate.now());
    }
}
