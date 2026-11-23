package com.tambo.inventario.repositorio;

import com.tambo.compartido.ConexionSQLite;
import com.tambo.compartido.modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaSQLiteRepository {

    // Listar todas las categorias
    public List<Categoria> listarCategorias() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, codigo, nombre FROM categorias ORDER BY nombre";

        try (Connection conexion = ConexionSQLite.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre")
                );

                categorias.add(categoria);
            }
        }

        return categorias;
    }
}
