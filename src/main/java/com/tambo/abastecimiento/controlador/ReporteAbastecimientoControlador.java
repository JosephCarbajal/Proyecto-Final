package com.tambo.abastecimiento.controlador;

import com.tambo.abastecimiento.reporte.FiltroReporteAbastecimiento;
import com.tambo.abastecimiento.reporte.ReporteCantidadesPendientes;
import com.tambo.abastecimiento.reporte.ReporteComprasPorProveedor;
import com.tambo.abastecimiento.reporte.ReporteRecepcionesPorPeriodo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.table.DefaultTableModel;

public final class ReporteAbastecimientoControlador {

    public DefaultTableModel generar(int opcion, String desde, String hasta)
            throws SQLException {

        FiltroReporteAbastecimiento filtro;

        try {
            LocalDate fechaInicial = LocalDate.parse(desde.strip());
            LocalDate fechaFinal = LocalDate.parse(hasta.strip());

            filtro = new FiltroReporteAbastecimiento(fechaInicial, fechaFinal);
        } catch (DateTimeParseException error) {
            throw new IllegalArgumentException(
                    "Escribe las fechas en formato AAAA-MM-DD. Ejemplo: 2026-11-23.");
        }

        return switch (opcion) {
            case 0 -> new ReporteComprasPorProveedor().generar(filtro);
            case 1 -> new ReporteRecepcionesPorPeriodo().generar(filtro);
            case 2 -> new ReporteCantidadesPendientes().generar(filtro);
            default -> throw new IllegalArgumentException("Selecciona un reporte.");
        };
    }
}