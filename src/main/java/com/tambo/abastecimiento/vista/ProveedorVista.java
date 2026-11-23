package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.servicio.PublicadorEventosRecepcion;
import com.tambo.abastecimiento.controlador.ProveedorControlador;
import com.tambo.abastecimiento.modelo.Proveedor;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.*;

public final class ProveedorVista extends JPanel {
    private final ProveedorControlador controlador = new ProveedorControlador();
    private final JTable tabla = new JTable();
    private final JTextField campoBusqueda = new JTextField(18);

    public ProveedorVista() throws SQLException {
        super(new BorderLayout());

        JPanel barra = Formulario.barra();
        barra.add(new JLabel("Código o nombre"));
        barra.add(campoBusqueda);
        barra.add(Formulario.boton("Buscar", this::actualizar));
        barra.add(Formulario.boton("Nuevo proveedor", this::nuevoProveedor));
        barra.add(Formulario.boton("Editar seleccionado", this::editarSeleccionado));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        actualizar();
    }

    private void actualizar() throws SQLException {
        tabla.setModel(controlador.buscar(campoBusqueda.getText()));
    }

    private void nuevoProveedor() throws SQLException {
        Proveedor nuevo = new Proveedor(0, "", "", "", 1);
        controlador.editar(nuevo);
        actualizar();
    }

    private void editarSeleccionado() throws SQLException {
        int seleccion = tabla.getSelectedRow();

        if (seleccion < 0) {
            throw new IllegalArgumentException(
                    "Selecciona un proveedor de la tabla para editarlo.");
        }

        int fila = tabla.convertRowIndexToModel(seleccion);
        var modelo = tabla.getModel();

        Proveedor proveedor = new Proveedor(
                ((Number) modelo.getValueAt(fila, 0)).intValue(),
                modelo.getValueAt(fila, 1).toString(),
                modelo.getValueAt(fila, 2).toString(),
                modelo.getValueAt(fila, 3).toString(),
                ((Number) modelo.getValueAt(fila, 4)).intValue());

        controlador.editar(proveedor);
        actualizar();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                abrirVentana();
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(
                        null,
                        error.getMessage(),
                        "Error al abrir el módulo",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void abrirVentana() throws SQLException {
        PublicadorEventosRecepcion eventos = new PublicadorEventosRecepcion();
        OrdenesPendientesVista pendientes = new OrdenesPendientesVista();

        eventos.escuchar(evento -> {
            try {
                pendientes.actualizar();
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(
                        pendientes,
                        error.getMessage(),
                        "Error al actualizar pendientes",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Proveedores", new ProveedorVista());
        pestanas.addTab("Catálogo", new CatalogoProveedorVista());
        pestanas.addTab("Compras", new OrdenCompraVista());
        pestanas.addTab("Pendientes", pendientes);
        pestanas.addTab("Recepciones", new RecepcionVista(eventos));
        pestanas.addTab("Reportes", new ReporteAbastecimientoVista());
        pestanas.addTab("Auditoría", new AuditoriaAbastecimientoVista());

        JFrame ventana = new JFrame("Tambo+ | Proveedores y Abastecimiento");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.add(pestanas);
        ventana.setSize(1100, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
}