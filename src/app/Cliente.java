package app;

import java.time.LocalDate;

public class Cliente {
    private final int id;
    private final String nombre;
    private final String apellido;
    private final String direccion;
    private String cumpleaños;


    public Cliente(int id, String nombre, String apellido, String direccion, String string) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.cumpleaños = string;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDireccion() {
        return direccion;
    }
    public String getCumpleaños() {
        return apellido;
    }
}