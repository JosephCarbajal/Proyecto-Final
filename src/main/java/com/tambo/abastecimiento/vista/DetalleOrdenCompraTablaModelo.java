package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.modelo.DetalleOrdenCompra;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public final class DetalleOrdenCompraTablaModelo extends DefaultTableModel {

    public DetalleOrdenCompraTablaModelo() {
        super(new String[]{"ID del producto", "Cantidad solicitada"}, 0);
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    public void agregar(DetalleOrdenCompra detalle) {
        addRow(new Object[]{detalle.productoId(), detalle.cantidad()});
    }

    public List<DetalleOrdenCompra> detalles() {
        List<DetalleOrdenCompra> lista = new ArrayList<>();

        for (int fila = 0; fila < getRowCount(); fila++) {
            int productoId = (int) getValueAt(fila, 0);
            int cantidad = (int) getValueAt(fila, 1);

            lista.add(new DetalleOrdenCompra(productoId, cantidad));
        }

        return lista;
    }
}