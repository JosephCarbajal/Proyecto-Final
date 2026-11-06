
package com.tambo.compartido.modelo;

public class Producto {
    // Datos del producto
    private int id;
    private String codigo, nombre, marca, presentacion;
    private int categoriaId, precioCentimos, stockMinimo;
    private boolean activo;

    // Constructor para guardar los datos
    public Producto(int id, String codigo, String nombre, String marca, int categoriaId, String presentacion, int precioCentimos, int stockMinimo, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.categoriaId = categoriaId;
        this.presentacion = presentacion;
        this.precioCentimos = precioCentimos;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
    }

    // Obtener datos del producto
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public int getCategoriaId() { return categoriaId; }
    public String getPresentacion() { return presentacion; }
    public int getPrecioCentimos() { return precioCentimos; }
    public int getStockMinimo() { return stockMinimo; }
    public boolean isActivo() { return activo; }

    // Modificar datos del producto
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    public void setPrecioCentimos(int precioCentimos) { this.precioCentimos = precioCentimos; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Mostrar los datos del producto
    @Override
    public String toString() {
        return codigo + " - " + nombre + " | Marca: " + marca +
                " | Precio: S/ " + String.format("%.2f", precioCentimos / 100.0);
    }
}
