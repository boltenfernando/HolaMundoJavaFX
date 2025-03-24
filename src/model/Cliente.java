package model;

import java.time.LocalDate;

public class Cliente {
    private final int id;
    private final String nombre;
    private final String apellido;
    private final String direccion;
    private LocalDate cumpleaños; // Nuevo atributo


    public Cliente(int id, String nombre, String apellido, String direccion, LocalDate cumpleaños) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.cumpleaños = cumpleaños;
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
 // Métodos getter y setter para 'cumpleaños'
    public LocalDate getCumpleaños() {
        return cumpleaños;
    }
    public void setCumpleaños(LocalDate cumpleaños) {
        this.cumpleaños = cumpleaños;
    }
}