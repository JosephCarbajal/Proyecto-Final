package com.tambo.abastecimiento.reporte;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ReporteRecepcionesPorPeriodo {
    private final ConsultaReportesAbastecimientoSQLite consultas =
            new ConsultaReportesAbastecimientoSQLite();

    public DefaultTableModel generar(FiltroReporteAbastecimiento filtro)
            throws SQLException {

        String sql = """
                SELECT r.numero AS Recepción,
                       o.numero AS Orden,
                       p.nombre AS Proveedor,
                       r.fecha AS Fecha,
                       r.estado AS Estado,
                       COALESCE(SUM(d.cantidad), 0) AS "Unidades aceptadas"
                FROM recepciones r
                JOIN ordenes_compra o ON o.id = r.orden_id
                JOIN proveedores p ON p.id = o.proveedor_id
                LEFT JOIN detalle_recepciones d ON d.recepcion_id = r.id
                WHERE date(r.fecha) BETWEEN ? AND ?
                GROUP BY r.id, r.numero, o.numero, p.nombre, r.fecha, r.estado
                ORDER BY r.fecha DESC, r.id DESC
                """;

        return consultas.ejecutar(sql, filtro);
    }
}
