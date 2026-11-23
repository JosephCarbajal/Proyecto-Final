package com.tambo.compartido.modelo;

public class Producto {

    private int id;
    private String codigo, nombre, marca, presentacion;
    private int categoriaId, precioCentimos, costoCentimos, stockMinimo;
    private boolean requiereVencimiento, activo;

    // Constructor anterior para mantener compatibilidad
    public Producto(int id, String codigo, String nombre, String marca,
                    int categoriaId, String presentacion, int precioCentimos,
                    int stockMinimo, boolean activo) {
        this(id, codigo, nombre, marca, categoriaId, presentacion,
                precioCentimos, 0, stockMinimo, true, activo);
    }

    // Constructor completo
    public Producto(int id, String codigo, String nombre, String marca,
                    int categoriaId, String presentacion, int precioCentimos,
                    int costoCentimos, int stockMinimo,
                    boolean requiereVencimiento, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.categoriaId = categoriaId;
        this.presentacion = presentacion;
        this.precioCentimos = precioCentimos;
        this.costoCentimos = costoCentimos;
        this.stockMinimo = stockMinimo;
        this.requiereVencimiento = requiereVencimiento;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public int getCategoriaId() { return categoriaId; }
    public String getPresentacion() { return presentacion; }
    public int getPrecioCentimos() { return precioCentimos; }
    public int getCostoCentimos() { return costoCentimos; }
    public int getStockMinimo() { return stockMinimo; }
    public boolean isRequiereVencimiento() { return requiereVencimiento; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    public void setPrecioCentimos(int precioCentimos) { this.precioCentimos = precioCentimos; }
    public void setCostoCentimos(int costoCentimos) { this.costoCentimos = costoCentimos; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setRequiereVencimiento(boolean requiereVencimiento) {
        this.requiereVencimiento = requiereVencimiento;
    }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | Marca: " + marca +
                " | Precio: S/ " + String.format("%.2f", precioCentimos / 100.0);
    }
}
