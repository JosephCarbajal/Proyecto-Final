package com.tambo.inventario.util;

import com.tambo.inventario.modelo.TipoMovimiento;

public class MovimientoValidador {

    public boolean validarTipo(TipoMovimiento tipo) {
        return tipo != null;
    }

    public boolean validarCantidad(int cantidad) {
        return cantidad > 0;
    }

    public boolean validarMovimiento(TipoMovimiento tipo, int cantidad) {
        return validarTipo(tipo) && validarCantidad(cantidad);
    }
}
