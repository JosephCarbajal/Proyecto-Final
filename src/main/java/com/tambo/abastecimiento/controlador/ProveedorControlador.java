package com.tambo.abastecimiento.controlador;

import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.vista.ProveedorEdicionDialogo;
import com.tambo.abastecimiento.servicio.ProveedorServicio;
import com.tambo.abastecimiento.modelo.Proveedor;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ProveedorControlador {
    private final ProveedorServicio servicio = new ProveedorServicio();
    private final ProveedorEdicionDialogo dialogo = new ProveedorEdicionDialogo();

    public DefaultTableModel buscar(String filtro) throws SQLException {
        return servicio.buscar(filtro);
    }

    public void editar(Proveedor proveedor) throws SQLException {
        Proveedor editado = dialogo.editar(proveedor);
        servicio.guardar(editado, Formulario.usuario());
    }
}