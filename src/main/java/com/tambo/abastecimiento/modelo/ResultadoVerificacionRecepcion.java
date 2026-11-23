package com.tambo.abastecimiento.modelo;

public final class ResultadoVerificacionRecepcion {
    private final int aceptadas;
    private final int rechazadas;
    private final int saldoRestante;

    public ResultadoVerificacionRecepcion(int aceptadas, int rechazadas,
                                          int saldoRestante) {
        this.aceptadas = aceptadas;
        this.rechazadas = rechazadas;
        this.saldoRestante = saldoRestante;
    }

    public int aceptadas() {
        return aceptadas;
    }

    public int rechazadas() {
        return rechazadas;
    }

    public int saldoRestante() {
        return saldoRestante;
    }
}