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
                rs.getInt("stock_minimo"),
                rs.getInt("activo") == 1
        );
    }
}
