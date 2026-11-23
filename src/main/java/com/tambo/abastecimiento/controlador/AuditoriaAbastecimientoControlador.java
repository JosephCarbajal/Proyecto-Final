package com.tambo.abastecimiento.controlador;

import com.tambo.abastecimiento.servicio.AuditoriaAbastecimientoServicio;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class AuditoriaAbastecimientoControlador {
    private final AuditoriaAbastecimientoServicio servicio =
            new AuditoriaAbastecimientoServicio();

    public DefaultTableModel historial() throws SQLException {
        return servicio.consultar();
    }
}
