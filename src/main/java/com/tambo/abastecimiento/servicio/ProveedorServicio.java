package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.validacion.ProveedorValidador;
import com.tambo.abastecimiento.modelo.Proveedor;
import com.tambo.abastecimiento.repositorio.ProveedorSQLiteRepository;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ProveedorServicio {
    private final ProveedorValidador validador = new ProveedorValidador();
    private final ProveedorSQLiteRepository repositorio = new ProveedorSQLiteRepository();
    private final AuditoriaAbastecimientoServicio auditoria =
            new AuditoriaAbastecimientoServicio();

    public int guardar(Proveedor proveedor, String usuario) throws SQLException {
        validador.validar(proveedor);

        return AccesoDatos.operar(conexion -> {
            int id = repositorio.guardar(conexion, proveedor);
            String accion = proveedor.id() == 0 ? "REGISTRAR" : "MODIFICAR";

            auditoria.registrar(conexion, "proveedores", id,
                    accion, usuario, proveedor.nombre());

            return id;
        });
    }

    public DefaultTableModel buscar(String texto) throws SQLException {
        return repositorio.buscar(texto);
    }
}
