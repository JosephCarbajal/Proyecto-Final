
package com.tambo.inventario.repositorio;

import com.tambo.compartido.ConexionSQLite;
import com.tambo.inventario.modelo.Lote;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoteSQLiteRepository {

    public List<Lote> listarPorProducto(int productoId)
            throws SQLException {

        List<Lote> lotes = new ArrayList<>();

        String sql = "SELECT * FROM lotes " +
                "WHERE producto_id = ? " +
                "ORDER BY fecha_vencimiento ASC";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotes.add(new Lote(
                            rs.getInt("id"),
                            rs.getInt("producto_id"),
                            rs.getString("numero_lote"),
                            LocalDate.parse(rs.getString("fecha_vencimiento")),
                            rs.getInt("cantidad_actual")
                    ));
                }
            }
        }

        return lotes;
    }

    public List<Lote> listarDisponiblesFEFO(int productoId)
            throws SQLException {

        List<Lote> lotes = new ArrayList<>();

        String sql = "SELECT * FROM lotes " +
                "WHERE producto_id = ? AND cantidad_actual > 0 " +
                "ORDER BY fecha_vencimiento ASC, id ASC";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotes.add(new Lote(
                            rs.getInt("id"),
                            rs.getInt("producto_id"),
                            rs.getString("numero_lote"),
                            LocalDate.parse(rs.getString("fecha_vencimiento")),
                            rs.getInt("cantidad_actual")
                    ));
                }
            }
        }

        return lotes;
    }

    public void registrarLote(
            int productoId,
            String numeroLote,
            LocalDate fechaVencimiento,
            int cantidad) throws SQLException {

        if (productoId <= 0 || numeroLote == null
                || numeroLote.trim().isEmpty()
                || fechaVencimiento == null || cantidad <= 0) {
            throw new IllegalArgumentException("Datos del lote no válidos.");
        }

        String sql = "INSERT INTO lotes " +
                "(producto_id, numero_lote, fecha_vencimiento, " +
                "cantidad_actual, fecha_registro) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, productoId);
            ps.setString(2, numeroLote.trim());
            ps.setString(3, fechaVencimiento.toString());
            ps.setInt(4, cantidad);
            ps.setString(5, LocalDateTime.now().toString());

            ps.executeUpdate();
        }
    }

    public void actualizarCantidad(int loteId, int cantidad)
            throws SQLException {

        if (loteId <= 0 || cantidad < 0) {
            throw new IllegalArgumentException("Cantidad no válida.");
        }

        String sql = "UPDATE lotes SET cantidad_actual = ? WHERE id = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, loteId);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Lote no encontrado.");
            }
        }
    }

    public List<Lote> listarVencidos() throws SQLException {

        List<Lote> lotes = new ArrayList<>();

        String sql = "SELECT * FROM lotes " +
                "WHERE cantidad_actual > 0 " +
                "AND fecha_vencimiento < date('now') " +
                "ORDER BY fecha_vencimiento ASC";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lotes.add(new Lote(
                        rs.getInt("id"),
                        rs.getInt("producto_id"),
                        rs.getString("numero_lote"),
                        LocalDate.parse(rs.getString("fecha_vencimiento")),
                        rs.getInt("cantidad_actual")
                ));
            }
        }

        return lotes;
    }

    public List<Lote> listarProximosAVencer(int dias)
            throws SQLException {

        if (dias < 0) {
            throw new IllegalArgumentException("Los días no pueden ser negativos.");
        }

        List<Lote> lotes = new ArrayList<>();

        String sql = "SELECT * FROM lotes " +
                "WHERE cantidad_actual > 0 " +
                "AND fecha_vencimiento >= date('now') " +
                "AND fecha_vencimiento <= date('now', '+' || ? || ' days') " +
                "ORDER BY fecha_vencimiento ASC";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, dias);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotes.add(new Lote(
                            rs.getInt("id"),
                            rs.getInt("producto_id"),
                            rs.getString("numero_lote"),
                            LocalDate.parse(rs.getString("fecha_vencimiento")),
                            rs.getInt("cantidad_actual")
                    ));
                }
            }
        }

        return lotes;
    }
}
