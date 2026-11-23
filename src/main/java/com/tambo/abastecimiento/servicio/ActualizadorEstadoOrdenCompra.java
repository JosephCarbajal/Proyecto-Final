package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.repositorio.ConsultaOrdenesPendientes;

import java.sql.Connection;
import java.sql.SQLException;

public final class ActualizadorEstadoOrdenCompra {

    public boolean actualizar(Connection conexion, int ordenId) throws SQLException {
        String consultarPendientes = """
                SELECT COUNT(*)
                FROM (
                """ + ConsultaOrdenesPendientes.SALDOS + """
                )
                WHERE orden_id = ? AND solicitadas > aceptadas
                """;

        long productosPendientes = AccesoDatos.numero(
                conexion, consultarPendientes, ordenId);

        if (productosPendientes > 0) {
            return false;
        }

        String cerrarOrden = """
                UPDATE ordenes_compra
                SET estado = 'RECIBIDA'
                WHERE id = ? AND estado = 'PENDIENTE'
                """;

        return AccesoDatos.escribir(conexion, cerrarOrden, ordenId) == 1;
    }
}
