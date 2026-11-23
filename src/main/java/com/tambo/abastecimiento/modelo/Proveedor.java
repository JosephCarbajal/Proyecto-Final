package com.tambo.abastecimiento.modelo;

public final class Proveedor {
    private final int id;
    private final String codigo;
    private final String nombre;
    private final String correo;
    private final int plazoDias;

    public Proveedor(int id, String codigo, String nombre, String correo, int plazoDias) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.correo = correo;
        this.plazoDias = plazoDias;
    }

    public int id() { return id; }
    public String codigo() { return codigo; }
    public String nombre() { return nombre; }
    public String correo() { return correo; }
    public int plazoDias() { return plazoDias; }
}