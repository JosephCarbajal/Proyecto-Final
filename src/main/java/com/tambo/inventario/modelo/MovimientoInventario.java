package com.tambo.inventario.modelo;

import java.time.LocalDateTime;

public class MovimientoInventario {
    private final TipoMovimiento tipo;
    private final int stockAnterior;
    private final int stockPosterior;
    private final LocalDateTime fechaHora;

    MovimientoInventario(TipoMovimiento tipo, int stockAnterior, int stockPosterior) {
        this.tipo = tipo;
        this.stockAnterior = stockAnterior;
        this.stockPosterior = stockPosterior;
        this.fechaHora = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return fechaHora.withNano(0) + " | " + tipo
                + " | stock: " + stockAnterior + " → " + stockPosterior;
    }
}