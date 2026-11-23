package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.ProductoProveedor;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ProductoProveedorSQLiteRepository {

    public void guardar(Connection conexion, ProductoProveedor producto)
            throws SQLException {

        String sql = """
                INSERT INTO producto_proveedor
                    (proveedor_id, producto_id, costo_centimos, principal)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(producto_id, proveedor_id) DO UPDATE SET
                    costo_centimos = excluded.costo_centimos,
                    principal = excluded.principal
                """;

        AccesoDatos.escribir(conexion, sql,
                producto.proveedorId(),
                producto.productoId(),
                producto.costoCentimos(),
                producto.principal() ? 1 : 0);
    }

    public DefaultTableModel listar(int proveedorId) throws SQLException {
        String sql = """
                SELECT p.id, p.codigo, p.nombre, v.costo_centimos, v.principal
                FROM producto_proveedor v
                JOIN productos p ON p.id = v.producto_id
                WHERE v.proveedor_id = ?
                ORDER BY p.nombre
                """;

        return AccesoDatos.operar(conexion ->
                AccesoDatos.tabla(conexion, sql, proveedorId));
    }

    public long costo(Connection conexion, int proveedorId, int productoId)
            throws SQLException {

        String sql = """
                SELECT v.costo_centimos
                FROM producto_proveedor v
                JOIN productos p ON p.id = v.producto_id
                WHERE v.proveedor_id = ? AND v.producto_id = ? AND p.activo = 1
                """;

        return AccesoDatos.numero(conexion, sql, proveedorId, productoId);
    }
}