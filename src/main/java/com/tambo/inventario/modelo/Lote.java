
package com.tambo.inventario.modelo;

import java.time.LocalDate;

public class Lote {

    private int id;
    private int productoId;
    private String numeroLote;
    private LocalDate fechaVencimiento;
    private int cantidadActual;

    public Lote(int id, int productoId, String numeroLote,
                LocalDate fechaVencimiento, int cantidadActual) {
        this.id = id;
        this.productoId = productoId;
        this.numeroLote = numeroLote;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadActual = cantidadActual;
    }

    public int getId() {
        return id;
    }

    public int getProductoId() {
        return productoId;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }
}
