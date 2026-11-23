
package com.tambo.inventario;

import com.tambo.inventario.repositorio.MovimientoInventarioSQLiteRepository;

public class PruebaMovimientoInventario {

    public static void main(String[] args) {

        MovimientoInventarioSQLiteRepository repositorio =
                new MovimientoInventarioSQLiteRepository();

        try {
            repositorio.registrarMovimiento(
                    1,
                    "ENTRADA",
                    5,
                    "Prueba del sistema",
                    "Administrador"
            );

            System.out.println("Movimiento registrado correctamente.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
