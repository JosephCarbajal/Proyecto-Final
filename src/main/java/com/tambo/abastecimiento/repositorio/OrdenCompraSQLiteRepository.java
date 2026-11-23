package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.OrdenCompra;

import java.sql.Connection;
import java.sql.SQLException;

public final class OrdenCompraSQLiteRepository {

    public int crear(Connection conexion, OrdenCompra orden) throws SQLException {
        String sql = """
                INSERT INTO ordenes_compra
                    (numero, proveedor_id, fecha_emision, estado, total_centimos)
                VALUES (?, ?, CURRENT_TIMESTAMP, 'PENDIENTE', 0)
                """;

        AccesoDatos.escribir(conexion, sql, orden.numero(), orden.proveedorId());

        return AccesoDatos.nuevoId(conexion);
    }

    public void fijarTotal(Connection conexion, int ordenId, long total)
            throws SQLException {

        String sql = """
                UPDATE ordenes_compra
                SET total_centimos = ?
                WHERE id = ?
                """;

        AccesoDatos.escribir(conexion, sql, total, ordenId);
    }
}