package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.ProductoProveedor;
import com.tambo.abastecimiento.repositorio.ProductoProveedorSQLiteRepository;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class CatalogoProveedorServicio {
    private final ProductoProveedorSQLiteRepository repositorio =
            new ProductoProveedorSQLiteRepository();

    private final AuditoriaAbastecimientoServicio auditoria =
            new AuditoriaAbastecimientoServicio();

    public void vincular(ProductoProveedor producto, String usuario)
            throws SQLException {

        if (producto.costoCentimos() < 0) {
            throw new IllegalArgumentException("El costo debe ser cero o mayor.");
        }

        AccesoDatos.operar(conexion -> {
            repositorio.guardar(conexion, producto);

            String detalle = "Proveedor " + producto.proveedorId()
                    + ", costo en céntimos " + producto.costoCentimos();

            auditoria.registrar(conexion, "producto_proveedor",
                    producto.productoId(), "VINCULAR", usuario, detalle);

            return null;
        });
    }

    public DefaultTableModel listar(int proveedorId) throws SQLException {
        return repositorio.listar(proveedorId);
    }
}