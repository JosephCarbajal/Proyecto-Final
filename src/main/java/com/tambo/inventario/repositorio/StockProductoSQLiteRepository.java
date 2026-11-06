package com.tambo.inventario.repositorio;

import com.tambo.compartido.ConexionSQLite;
import com.tambo.inventario.modelo.StockProducto;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StockProductoSQLiteRepository {

    // Buscar el stock de un producto
    public StockProducto buscarPorProducto(int productoId) throws SQLException {

        String sql = "SELECT COALESCE(SUM(stock_actual), 0) AS stock_total " +
                "FROM inventarios WHERE producto_id = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int stockActual = rs.getInt("stock_total");
                    return new StockProducto(productoId, stockActual);
                }
            }
        }

        return null;
    }

    // Obtener el stock de todos los productos
    public Map<Integer, Integer> listarStocks() throws SQLException {

        Map<Integer, Integer> stocks = new HashMap<>();

        String sql = "SELECT producto_id, SUM(stock_actual) AS stock_total " +
                "FROM inventarios GROUP BY producto_id";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int productoId = rs.getInt("producto_id");
                int stockActual = rs.getInt("stock_total");

                stocks.put(productoId, stockActual);
            }
        }

        return stocks;
    }
}
