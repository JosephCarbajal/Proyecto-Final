package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.DetalleOrdenCompra;

import java.sql.Connection;
import java.sql.SQLException;

public final class DetalleOrdenCompraSQLiteRepository {

    public long insertar(Connection conexion, int ordenId,
                         DetalleOrdenCompra detalle, long costo) throws SQLException {

        long subtotal = Math.multiplyExact(costo, detalle.cantidad());

        String sql = """
                INSERT INTO detalle_ordenes_compra
                    (orden_id, producto_id, cantidad,
                     costo_unitario_centimos, subtotal_centimos)
                VALUES (?, ?, ?, ?, ?)
                """;

        AccesoDatos.escribir(conexion, sql,
                ordenId,
                detalle.productoId(),
                detalle.cantidad(),
                costo,
                subtotal);

        return subtotal;
    }
}