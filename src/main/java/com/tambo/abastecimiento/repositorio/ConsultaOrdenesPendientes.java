package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.SaldoPendientePorProducto;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ConsultaOrdenesPendientes {

    public static final String SALDOS = """
            SELECT o.id AS orden_id, o.numero, o.proveedor_id, o.fecha_emision,
                   d.producto_id, p.nombre,
                   SUM(d.cantidad) AS solicitadas,
                   COALESCE((
                       SELECT SUM(dr.cantidad)
                       FROM detalle_recepciones dr
                       JOIN recepciones r ON r.id = dr.recepcion_id
                       WHERE r.orden_id = o.id
                         AND dr.producto_id = d.producto_id
                   ), 0) AS aceptadas,
                   MIN(d.costo_unitario_centimos) AS costo
            FROM ordenes_compra o
            JOIN detalle_ordenes_compra d ON d.orden_id = o.id
            JOIN productos p ON p.id = d.producto_id
            WHERE o.estado = 'PENDIENTE'
            GROUP BY o.id, d.producto_id
            """;

    public DefaultTableModel listar() throws SQLException {
        String sql = """
                SELECT orden_id, numero, proveedor_id, producto_id, nombre,
                       solicitadas, aceptadas, solicitadas - aceptadas AS pendientes
                FROM (
                """ + SALDOS + """
                )
                WHERE solicitadas > aceptadas
                ORDER BY orden_id, producto_id
                """;

        return AccesoDatos.operar(conexion -> AccesoDatos.tabla(conexion, sql));
    }

    public SaldoPendientePorProducto buscar(Connection conexion, int ordenId,
                                            int productoId) throws SQLException {
        String sql = "SELECT solicitadas, aceptadas, costo FROM (" + SALDOS
                + ") WHERE orden_id = ? AND producto_id = ?";

        try (var consulta = AccesoDatos.preparar(conexion, sql, ordenId, productoId);
             var resultado = consulta.executeQuery()) {

            if (!resultado.next()) {
                throw new SQLException(
                        "Verifica que la orden esté pendiente y que incluya el producto.");
            }

            return new SaldoPendientePorProducto(
                    productoId,
                    resultado.getInt("solicitadas"),
                    resultado.getInt("aceptadas"),
                    resultado.getLong("costo"));
        }
    }
}