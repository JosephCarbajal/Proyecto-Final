package com.tambo.inventario.repositorio;

import com.tambo.compartido.ConexionSQLite;
import com.tambo.compartido.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoSQLiteRepository {

    // Listar todos los productos
    public List<Producto> listarProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(obtenerProducto(rs));
            }
        }
        return productos;
    }

    // Buscar producto por codigo
    public Producto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM productos WHERE codigo = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return obtenerProducto(rs);
                }
            }
        }
        return null;
    }

    // Registrar un producto
    public void registrarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos " +
                "(codigo, nombre, marca, categoria_id, presentacion, " +
                "precio_centimos, costo_centimos, stock_minimo, " +
                "requiere_vencimiento, activo, origen_catalogo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getMarca());
            ps.setInt(4, producto.getCategoriaId());
            ps.setString(5, producto.getPresentacion());
            ps.setInt(6, producto.getPrecioCentimos());
            ps.setInt(7, producto.getCostoCentimos());
            ps.setInt(8, producto.getStockMinimo());
            ps.setInt(9, producto.isRequiereVencimiento() ? 1 : 0);
            ps.setInt(10, producto.isActivo() ? 1 : 0);
            ps.setString(11, "SIMULADO");

            ps.executeUpdate();
        }
    }

    // Actualizar un producto
    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, marca = ?, " +
                "categoria_id = ?, presentacion = ?, precio_centimos = ?, " +
                "costo_centimos = ?, stock_minimo = ?, " +
                "requiere_vencimiento = ?, activo = ? WHERE id = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getMarca());
            ps.setInt(3, producto.getCategoriaId());
            ps.setString(4, producto.getPresentacion());
            ps.setInt(5, producto.getPrecioCentimos());
            ps.setInt(6, producto.getCostoCentimos());
            ps.setInt(7, producto.getStockMinimo());
            ps.setInt(8, producto.isRequiereVencimiento() ? 1 : 0);
            ps.setInt(9, producto.isActivo() ? 1 : 0);
            ps.setInt(10, producto.getId());

            ps.executeUpdate();
        }
    }

    // Desactivar un producto
    public void desactivarProducto(int id) throws SQLException {
        String sql = "UPDATE productos SET activo = 0 WHERE id = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Comprobar si un codigo ya existe
    public boolean existeCodigo(String codigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Convertir los datos de SQLite a un producto
    private Producto obtenerProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("marca"),
                rs.getInt("categoria_id"),
                rs.getString("presentacion"),
                rs.getInt("precio_centimos"),
                rs.getInt("costo_centimos"),
                rs.getInt("stock_minimo"),
                rs.getInt("requiere_vencimiento") == 1,
                rs.getInt("activo") == 1
        );
    }
}
