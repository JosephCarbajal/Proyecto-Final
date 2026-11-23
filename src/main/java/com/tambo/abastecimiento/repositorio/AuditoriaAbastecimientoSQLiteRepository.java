package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.EventoAuditoriaAbastecimiento;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class AuditoriaAbastecimientoSQLiteRepository {

    public void guardar(Connection conexion,
                        EventoAuditoriaAbastecimiento evento) throws SQLException {
        String sql = """
                INSERT INTO auditoria
                    (entidad, entidad_id, accion, fecha, usuario, detalle)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                """;

        AccesoDatos.escribir(conexion, sql,
                evento.entidad(),
                evento.entidadId(),
                evento.accion(),
                AccesoDatos.requerido(evento.usuario(), "el usuario"),
                evento.detalle());
    }

    public DefaultTableModel consultar() throws SQLException {
        String sql = """
                SELECT fecha, usuario, entidad, entidad_id, accion, detalle
                FROM auditoria
                WHERE entidad IN (
                    'proveedores', 'producto_proveedor',
                    'ordenes_compra', 'recepciones'
                )
                ORDER BY fecha DESC, id DESC
                """;

        return AccesoDatos.operar(conexion -> AccesoDatos.tabla(conexion, sql));
    }
}
