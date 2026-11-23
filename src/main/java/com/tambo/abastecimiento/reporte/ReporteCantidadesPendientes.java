package com.tambo.abastecimiento.reporte;

import com.tambo.abastecimiento.repositorio.ConsultaOrdenesPendientes;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ReporteCantidadesPendientes {
    private final ConsultaReportesAbastecimientoSQLite consultas =
            new ConsultaReportesAbastecimientoSQLite();

    public DefaultTableModel generar(FiltroReporteAbastecimiento filtro)
            throws SQLException {

        String sql = """
                SELECT numero AS Orden,
                       producto_id AS "ID del producto",
                       nombre AS Producto,
                       solicitadas AS Solicitadas,
                       aceptadas AS Aceptadas,
                       solicitadas - aceptadas AS Pendientes
                FROM (
                """ + ConsultaOrdenesPendientes.SALDOS + """
                )
                WHERE solicitadas > aceptadas
                  AND date(fecha_emision) BETWEEN ? AND ?
                ORDER BY numero, nombre
                """;

        return consultas.ejecutar(sql, filtro);
    }
}