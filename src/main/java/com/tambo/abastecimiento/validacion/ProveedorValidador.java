package com.tambo.abastecimiento.validacion;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.Proveedor;

public final class ProveedorValidador {

    public void validar(Proveedor proveedor) {
        AccesoDatos.requerido(proveedor.codigo(), "el código del proveedor");
        AccesoDatos.requerido(proveedor.nombre(), "el nombre del proveedor");

        String correo = AccesoDatos.requerido(proveedor.correo(), "el correo");

        if (!correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("Ingresa un correo válido.");
        }

        if (proveedor.plazoDias() < 1) {
            throw new IllegalArgumentException(
                    "El plazo de entrega debe ser de al menos un día.");
        }
    }
}