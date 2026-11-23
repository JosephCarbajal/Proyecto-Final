package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.AccesoDatos;

import java.sql.Connection;
import java.sql.SQLException;

public final class AdaptadorEntradaInventario {

    public void ingresar(Connection conexion, int productoId, int cantidad,
                         String referencia, String usuario) throws SQLException {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de entrada debe ser mayor a cero.");
        }

        String consultarStock = """
                SELECT stock_actual
                FROM inventarios
                WHERE producto_id = ?
                """;

        int anterior = Math.toIntExact(
                AccesoDatos.numero(conexion, consultarStock, productoId));
        int posterior = Math.addExact(anterior, cantidad);

        String actualizarStock = """
                UPDATE inventarios
                SET stock_actual = ?, fecha_actualizacion = CURRENT_TIMESTAMP
                WHERE producto_id = ?
                """;

        AccesoDatos.escribir(conexion, actualizarStock, posterior, productoId);

        String guardarMovimiento = """
                INSERT INTO movimientos_stock
                    (producto_id, tipo, cantidad, stock_anterior, stock_posterior,
                     referencia, fecha, usuario)
                VALUES (?, 'ENTRADA', ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)
                """;

        AccesoDatos.escribir(conexion, guardarMovimiento,
                productoId, cantidad, anterior, posterior, referencia, usuario);
    }
}