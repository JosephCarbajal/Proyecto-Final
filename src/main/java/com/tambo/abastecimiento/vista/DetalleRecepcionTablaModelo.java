package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.modelo.DetalleRecepcion;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public final class DetalleRecepcionTablaModelo extends DefaultTableModel {

    public DetalleRecepcionTablaModelo() {
        super(new String[]{"ID del producto", "Recibidas", "Aceptadas"}, 0);
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    public void agregar(DetalleRecepcion detalle) {
        addRow(new Object[]{
                detalle.productoId(),
                detalle.recibidas(),
                detalle.aceptadas()
        });
    }

    public List<DetalleRecepcion> detalles() {
        List<DetalleRecepcion> lista = new ArrayList<>();

        for (int fila = 0; fila < getRowCount(); fila++) {
            int productoId = (int) getValueAt(fila, 0);
            int recibidas = (int) getValueAt(fila, 1);
            int aceptadas = (int) getValueAt(fila, 2);

            lista.add(new DetalleRecepcion(productoId, recibidas, aceptadas));
        }

        return lista;
    }
}