package com.tambo.abastecimiento.reporte;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ReporteComprasPorProveedor {
    private final ConsultaReportesAbastecimientoSQLite consultas =
            new ConsultaReportesAbastecimientoSQLite();

    public DefaultTableModel generar(FiltroReporteAbastecimiento filtro)
            throws SQLException {

        String sql = """
                SELECT p.codigo AS Código,
                       p.nombre AS Proveedor,
                       COUNT(*) AS Órdenes,
                       SUM(o.total_centimos) AS "Total en céntimos"
                FROM ordenes_compra o
                JOIN proveedores p ON p.id = o.proveedor_id
                WHERE date(o.fecha_emision) BETWEEN ? AND ?
                GROUP BY p.id, p.codigo, p.nombre
                ORDER BY p.nombre
                """;

        return consultas.ejecutar(sql, filtro);
    }
}