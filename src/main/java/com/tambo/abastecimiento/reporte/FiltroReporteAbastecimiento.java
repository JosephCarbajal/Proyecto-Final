package com.tambo.abastecimiento.reporte;

import java.time.LocalDate;

public final class FiltroReporteAbastecimiento {
    private final LocalDate desde;
    private final LocalDate hasta;

    public FiltroReporteAbastecimiento(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public LocalDate desde() {
        return desde;
    }

    public LocalDate hasta() {
        return hasta;
    }

    public void validar() {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Ingresa la fecha inicial y la fecha final.");
        }

        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException(
                    "La fecha final debe ser igual o posterior a la fecha inicial.");
        }
    }
}