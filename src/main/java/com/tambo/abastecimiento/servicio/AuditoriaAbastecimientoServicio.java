package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.modelo.EventoAuditoriaAbastecimiento;
import com.tambo.abastecimiento.repositorio.AuditoriaAbastecimientoSQLiteRepository;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class AuditoriaAbastecimientoServicio {
    private final AuditoriaAbastecimientoSQLiteRepository repositorio =
            new AuditoriaAbastecimientoSQLiteRepository();

    public void registrar(Connection conexion, String entidad, int id,
                          String accion, String usuario, String detalle)
            throws SQLException {

        EventoAuditoriaAbastecimiento evento =
                new EventoAuditoriaAbastecimiento(entidad, id, accion, usuario, detalle);

        repositorio.guardar(conexion, evento);
    }

    public DefaultTableModel consultar() throws SQLException {
        return repositorio.consultar();
    }
}