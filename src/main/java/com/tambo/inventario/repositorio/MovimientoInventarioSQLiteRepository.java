
package com.tambo.inventario.repositorio;

import com.tambo.compartido.ConexionSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientoInventarioSQLiteRepository {

    public void registrarMovimiento(
            int productoId,
            String tipo,
            int cantidad,
            String referencia,
            String usuario) throws SQLException {

        if (!tipo.equals("ENTRADA") && !tipo.equals("SALIDA")) {
            throw new IllegalArgumentException("Tipo de movimiento no permitido.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        try (Connection conexion = ConexionSQLite.conectar()) {

            conexion.setAutoCommit(false);

            try {
                String sqlStock = "SELECT stock_actual FROM inventarios WHERE producto_id = ?";

                int stockAnterior;

                try (PreparedStatement ps = conexion.prepareStatement(sqlStock)) {
                    ps.setInt(1, productoId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("No existe inventario para este producto.");
                        }

                        stockAnterior = rs.getInt("stock_actual");
                    }
                }

                int stockPosterior;

                if (tipo.equals("ENTRADA")) {
                    stockPosterior = Math.addExact(stockAnterior, cantidad);
                } else {
                    if (cantidad > stockAnterior) {
                        throw new SQLException("Stock insuficiente.");
                    }

                    stockPosterior = stockAnterior - cantidad;
                }

                String sqlActualizar = "UPDATE inventarios SET stock_actual = ?, " +
                        "fecha_actualizacion = ? WHERE producto_id = ?";

                try (PreparedStatement ps = conexion.prepareStatement(sqlActualizar)) {
                    ps.setInt(1, stockPosterior);
                    ps.setString(2, LocalDateTime.now().toString());
                    ps.setInt(3, productoId);
                    ps.executeUpdate();
                }

                String sqlMovimiento = "INSERT INTO movimientos_stock " +
                        "(producto_id, tipo, cantidad, stock_anterior, " +
                        "stock_posterior, referencia, fecha, usuario) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = conexion.prepareStatement(sqlMovimiento)) {
                    ps.setInt(1, productoId);
                    ps.setString(2, tipo);
                    ps.setInt(3, cantidad);
                    ps.setInt(4, stockAnterior);
                    ps.setInt(5, stockPosterior);
                    ps.setString(6, referencia);
                    ps.setString(7, LocalDateTime.now().toString());
                    ps.setString(8, usuario);
                    ps.executeUpdate();
                }

                conexion.commit();

            } catch (Exception e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    public List<Map<String, Object>> listarMovimientos() throws SQLException {
        return listarMovimientos("", "Todos");
    }

    public List<Map<String, Object>> listarMovimientos(
            String codigo, String tipo) throws SQLException {

        List<Map<String, Object>> movimientos = new ArrayList<>();

        String sql = "SELECT m.id, p.codigo, p.nombre, m.tipo, " +
                "m.cantidad, m.stock_anterior, m.stock_posterior, " +
                "m.referencia, m.fecha, m.usuario " +
                "FROM movimientos_stock m " +
                "INNER JOIN productos p ON m.producto_id = p.id " +
                "WHERE p.codigo LIKE ? " +
                "AND (? = 'Todos' OR m.tipo = ?) " +
                "ORDER BY m.fecha DESC";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + codigo + "%");
            ps.setString(2, tipo);
            ps.setString(3, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> movimiento = new HashMap<>();

                    movimiento.put("id", rs.getInt("id"));
                    movimiento.put("codigo", rs.getString("codigo"));
                    movimiento.put("nombre", rs.getString("nombre"));
                    movimiento.put("tipo", rs.getString("tipo"));
                    movimiento.put("cantidad", rs.getInt("cantidad"));
                    movimiento.put("stockAnterior", rs.getInt("stock_anterior"));
                    movimiento.put("stockPosterior", rs.getInt("stock_posterior"));
                    movimiento.put("referencia", rs.getString("referencia"));
                    movimiento.put("fecha", rs.getString("fecha"));
                    movimiento.put("usuario", rs.getString("usuario"));

                    movimientos.add(movimiento);
                }
            }
        }

        return movimientos;
    }
}
