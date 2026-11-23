package com.tambo.abastecimiento.controlador;

import com.tambo.abastecimiento.servicio.CatalogoProveedorServicio;
import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.modelo.ProductoProveedor;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public final class CatalogoProveedorControlador {
    private final CatalogoProveedorServicio servicio =
            new CatalogoProveedorServicio();

    public DefaultTableModel consultar(int proveedorId) throws SQLException {
        return servicio.listar(proveedorId);
    }

    public void vincular(int proveedorId) throws SQLException {
        int productoId = Formulario.entero("ID del producto");
        int costo = Formulario.entero("Costo en céntimos. Ejemplo: 450 para S/ 4.50");

        int opcion = JOptionPane.showConfirmDialog(
                null,
                "¿Es el proveedor principal de este producto?",
                "Catálogo del proveedor",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (opcion == JOptionPane.YES_OPTION || opcion == JOptionPane.NO_OPTION) {
            boolean principal = opcion == JOptionPane.YES_OPTION;

            ProductoProveedor relacion = new ProductoProveedor(
                    proveedorId, productoId, costo, principal);

            servicio.vincular(relacion, Formulario.usuario());
        }
    }
}
