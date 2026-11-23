package com.tambo.abastecimiento.reporte;

import com.tambo.abastecimiento.AccesoDatos;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ConsultaReportesAbastecimientoSQLite {

    public DefaultTableModel ejecutar(String consulta, FiltroReporteAbastecimiento filtro)
            throws SQLException {

        filtro.validar();

        String desde = filtro.desde().toString();
        String hasta = filtro.hasta().toString();

        return AccesoDatos.operar(conexion ->
                AccesoDatos.tabla(conexion, consulta, desde, hasta));
    }
}