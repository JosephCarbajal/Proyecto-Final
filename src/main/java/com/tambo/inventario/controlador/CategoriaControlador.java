package com.tambo.inventario.controlador;

import com.tambo.inventario.repositorio.CategoriaSQLiteRepository;
import com.tambo.inventario.vista.CategoriaVista;

import javax.swing.JOptionPane;
import java.sql.SQLException;

public class CategoriaControlador {

    private final CategoriaVista vista;
    private final CategoriaSQLiteRepository repositorio;

    public CategoriaControlador(CategoriaVista vista) {
        this.vista = vista;
        this.repositorio = new CategoriaSQLiteRepository();

        vista.eventoRegistrar(() -> registrarCategoria());
        vista.eventoEditar(() -> editarCategoria());
        vista.eventoEliminar(() -> eliminarCategoria());

        cargarCategorias();
    }

    private void cargarCategorias() {
        try {
            vista.mostrarCategorias(repositorio.listarCategorias());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al cargar categorías: " + e.getMessage());
        }
    }

    private void registrarCategoria() {
        String codigo = vista.getCodigo();
        String nombre = vista.getNombre();
        String descripcion = vista.getDescripcion();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Completa el código y el nombre.");
            return;
        }

        try {
            if (repositorio.existeCodigoONombre(codigo, nombre, -1)) {
                JOptionPane.showMessageDialog(vista,
                        "El código o nombre ya existe.");
                return;
            }

            repositorio.registrarCategoria(codigo, nombre, descripcion);
            JOptionPane.showMessageDialog(vista,
                    "Categoría registrada correctamente.");

            vista.limpiarCampos();
            cargarCategorias();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al registrar: " + e.getMessage());
        }
    }

    private void editarCategoria() {
        int id = vista.getIdSeleccionado();

        if (id < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona una categoría.");
            return;
        }

        String codigo = vista.getCodigo();
        String nombre = vista.getNombre();
        String descripcion = vista.getDescripcion();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Completa el código y el nombre.");
            return;
        }

        try {
            if (repositorio.existeCodigoONombre(codigo, nombre, id)) {
                JOptionPane.showMessageDialog(vista,
                        "El código o nombre ya existe.");
                return;
            }

            repositorio.actualizarCategoria(id, codigo, nombre, descripcion);
            JOptionPane.showMessageDialog(vista,
                    "Categoría actualizada correctamente.");

            vista.limpiarCampos();
            cargarCategorias();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al editar: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        int id = vista.getIdSeleccionado();

        if (id < 0) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona una categoría.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas eliminar esta categoría?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            repositorio.eliminarCategoria(id);
            JOptionPane.showMessageDialog(vista,
                    "Categoría eliminada correctamente.");

            vista.limpiarCampos();
            cargarCategorias();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista,
                    e.getMessage());
        }
    }
}
