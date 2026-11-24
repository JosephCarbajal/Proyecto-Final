package com.tambo.inventario.repositorio;

import com.tambo.compartido.ConexionSQLite;
import com.tambo.compartido.modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaSQLiteRepository {

    public List<Categoria> listarCategorias() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, codigo, nombre FROM categorias ORDER BY nombre";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre")
                ));
            }
        }

        return categorias;
    }

    public boolean existeCodigoONombre(String codigo, String nombre, int idExcluir)
            throws SQLException {

        String sql = "SELECT COUNT(*) FROM categorias " +
                "WHERE (codigo = ? OR nombre = ?) AND id != ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setInt(3, idExcluir);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void registrarCategoria(String codigo, String nombre, String descripcion)
            throws SQLException {

        String sql = "INSERT INTO categorias (codigo, nombre, descripcion) " +
                "VALUES (?, ?, ?)";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.executeUpdate();
        }
    }

    public void actualizarCategoria(int id, String codigo, String nombre,
                                    String descripcion) throws SQLException {

        String sql = "UPDATE categorias SET codigo = ?, nombre = ?, " +
                "descripcion = ? WHERE id = ?";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setInt(4, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("La categoría no existe.");
            }
        }
    }

    public void eliminarCategoria(int id) throws SQLException {

        String sqlProductos =
                "SELECT COUNT(*) FROM productos WHERE categoria_id = ?";

        try (Connection conexion = ConexionSQLite.conectar()) {

            try (PreparedStatement ps = conexion.prepareStatement(sqlProductos)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException(
                                "No se puede eliminar: existen productos asociados."
                        );
                    }
                }
            }

            String sqlEliminar = "DELETE FROM categorias WHERE id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sqlEliminar)) {
                ps.setInt(1, id);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("La categoría no existe.");
                }
            }
        }
    }
}
