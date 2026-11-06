package com.tambo.app;

import com.tambo.inventario.modelo.Inventario;
import com.tambo.inventario.modelo.TipoMovimiento;

public class PruebaInventario {
    public static void main(String[] args) {
        Inventario inventario = new Inventario(10, 5);
        System.out.println("Stock inicial: " + inventario.getStock());

        inventario.registrarMovimiento(TipoMovimiento.ENTRADA, 3);
        inventario.registrarMovimiento(TipoMovimiento.SALIDA, 6);
        inventario.registrarMovimiento(TipoMovimiento.MERMA, 2);

        System.out.println("Stock después de los movimientos: " + inventario.getStock());
        System.out.println("Alerta de stock mínimo: " + inventario.requiereReposicion());

        try {
            inventario.registrarMovimiento(TipoMovimiento.SALIDA, 6);
        } catch (IllegalArgumentException error) {
            System.out.println("Salida rechazada: " + error.getMessage());
        }

        inventario.registrarMovimiento(TipoMovimiento.AJUSTE, 8);
        System.out.println("Stock ajustado: " + inventario.getStock());
        System.out.println("Alerta después del ajuste: " + inventario.requiereReposicion());

        System.out.println("Historial:");
        inventario.getHistorial().forEach(System.out::println);
    }
}