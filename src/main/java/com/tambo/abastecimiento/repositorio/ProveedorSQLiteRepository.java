package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.Proveedor;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class ProveedorSQLiteRepository {

    public int guardar(Connection conexion, Proveedor proveedor) throws SQLException {
        if (proveedor.id() == 0) {
            String sql = """
                    INSERT INTO proveedores (codigo, nombre, correo, plazo_entrega_dias)
                    VALUES (?, ?, ?, ?)
                    """;

            AccesoDatos.escribir(conexion, sql,
                    proveedor.codigo(), proveedor.nombre(),
                    proveedor.correo(), proveedor.plazoDias());

            return AccesoDatos.nuevoId(conexion);
        }

        String sql = """
                UPDATE proveedores
                SET codigo = ?, nombre = ?, correo = ?, plazo_entrega_dias = ?
                WHERE id = ?
                """;

        int filas = AccesoDatos.escribir(conexion, sql,
                proveedor.codigo(), proveedor.nombre(),
                proveedor.correo(), proveedor.plazoDias(), proveedor.id());

        if (filas != 1) {
            throw new SQLException("No se encontró el proveedor que deseas modificar.");
        }

        return proveedor.id();
    }

    public DefaultTableModel buscar(String texto) throws SQLException {
        String patron = "%" + texto.strip() + "%";

        String sql = """
                SELECT id AS ID, codigo AS Código, nombre AS Nombre,
                       correo AS Correo, plazo_entrega_dias AS Días
                FROM proveedores
                WHERE nombre LIKE ? OR codigo LIKE ?
                ORDER BY nombre
                """;

        return AccesoDatos.operar(conexion ->
                AccesoDatos.tabla(conexion, sql, patron, patron));
    }
}