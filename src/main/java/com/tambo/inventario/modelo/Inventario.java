package com.tambo.inventario.modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private int stock;
    private final int stockMinimo;
    private final List<MovimientoInventario> historial = new ArrayList<>();

    public Inventario(int stockInicial, int stockMinimo) {
        if (stockInicial < 0 || stockMinimo < 0) {
            throw new IllegalArgumentException("El stock y el mínimo deben ser mayores o iguales a cero.");
        }
        this.stock = stockInicial;
        this.stockMinimo = stockMinimo;
    }

    public synchronized void registrarMovimiento(TipoMovimiento tipo, int cantidad) {
        if (tipo == null) {
            throw new IllegalArgumentException("Selecciona un tipo de movimiento.");
        }

        int nuevoStock;
        switch (tipo) {
            case ENTRADA:
                validarCantidadPositiva(cantidad);
                nuevoStock = Math.addExact(stock, cantidad);
                break;
            case SALIDA:
            case MERMA:
                validarCantidadPositiva(cantidad);
                if (cantidad > stock) {
                    throw new IllegalArgumentException("Stock insuficiente.");
                }
                nuevoStock = stock - cantidad;
                break;
            case AJUSTE:
                if (cantidad < 0) {
                    throw new IllegalArgumentException("El stock ajustado debe ser mayor o igual a cero.");
                }
                nuevoStock = cantidad;
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no reconocido.");
        }

        historial.add(new MovimientoInventario(tipo, stock, nuevoStock));
        stock = nuevoStock;
    }

    public synchronized int getStock() {
        return stock;
    }

    public synchronized boolean requiereReposicion() {
        return stock <= stockMinimo;
    }

    public synchronized List<MovimientoInventario> getHistorial() {
        return List.copyOf(historial);
    }

    private void validarCantidadPositiva(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
    }
}