package model;

import java.time.LocalDate;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String referencia;
    private LocalDate proximoContacto;
    private String direccion;
    private String localidad;
    private LocalDate cumpleaños;
    private String datosPersonales;
    private String datosLaborales;
    private String datosVenta;
    private String datosCompra;
    private boolean deseaContacto;
    private boolean fueCliente;
    private LocalDate fechaCompraVenta;
    private boolean esReferidor;
    private String refirioA;
    private String referidoPor;
    private boolean esPadre;
    private boolean esMadre;
    private String nombreHijos;
    private String telefono;
    private String redesSociales;
    private String email;
    private String ocupacion;
    private String gustosMusicales;
    private String clubFutbol;
    private String gustoBebidas;
    private String preferenciasComida;
    private String categoria;

    public Cliente() {}

    public Cliente(int id, String nombre, String apellido, String referencia, LocalDate proximoContacto,
                   String direccion, String localidad, LocalDate cumpleaños, String datosPersonales,
                   String datosLaborales, String datosVenta, String datosCompra, boolean deseaContacto,
                   boolean fueCliente, LocalDate fechaCompraVenta, boolean esReferidor, String refirioA,
                   String referidoPor, boolean esPadre, boolean esMadre, String nombreHijos, String telefono,
                   String redesSociales, String email, String ocupacion, String gustosMusicales,
                   String clubFutbol, String gustoBebidas, String preferenciasComida, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.referencia = referencia;
        this.proximoContacto = proximoContacto;
        this.direccion = direccion;
        this.localidad = localidad;
        this.cumpleaños = cumpleaños;
        this.datosPersonales = datosPersonales;
        this.datosLaborales = datosLaborales;
        this.datosVenta = datosVenta;
        this.datosCompra = datosCompra;
        this.deseaContacto = deseaContacto;
        this.fueCliente = fueCliente;
        this.fechaCompraVenta = fechaCompraVenta;
        this.esReferidor = esReferidor;
        this.refirioA = refirioA;
        this.referidoPor = referidoPor;
        this.esPadre = esPadre;
        this.esMadre = esMadre;
        this.nombreHijos = nombreHijos;
        this.telefono = telefono;
        this.redesSociales = redesSociales;
        this.email = email;
        this.ocupacion = ocupacion;
        this.gustosMusicales = gustosMusicales;
        this.clubFutbol = clubFutbol;
        this.gustoBebidas = gustoBebidas;
        this.preferenciasComida = preferenciasComida;
        this.categoria = categoria;
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public LocalDate getProximoContacto() { return proximoContacto; }
    public void setProximoContacto(LocalDate proximoContacto) { this.proximoContacto = proximoContacto; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public LocalDate getCumpleaños() { return cumpleaños; }
    public void setCumpleaños(LocalDate cumpleaños) { this.cumpleaños = cumpleaños; }

    public String getDatosPersonales() { return datosPersonales; }
    public void setDatosPersonales(String datosPersonales) { this.datosPersonales = datosPersonales; }

    public String getDatosLaborales() { return datosLaborales; }
    public void setDatosLaborales(String datosLaborales) { this.datosLaborales = datosLaborales; }

    public String getDatosVenta() { return datosVenta; }
    public void setDatosVenta(String datosVenta) { this.datosVenta = datosVenta; }

    public String getDatosCompra() { return datosCompra; }
    public void setDatosCompra(String datosCompra) { this.datosCompra = datosCompra; }

    public boolean isDeseaContacto() { return deseaContacto; }
    public void setDeseaContacto(boolean deseaContacto) { this.deseaContacto = deseaContacto; }

    public boolean isFueCliente() { return fueCliente; }
    public void setFueCliente(boolean fueCliente) { this.fueCliente = fueCliente; }

    public LocalDate getFechaCompraVenta() { return fechaCompraVenta; }
    public void setFechaCompraVenta(LocalDate fechaCompraVenta) { this.fechaCompraVenta = fechaCompraVenta; }

    public boolean isEsReferidor() { return esReferidor; }
    public void setEsReferidor(boolean esReferidor) { this.esReferidor = esReferidor; }

    public String getRefirioA() { return refirioA; }
    public void setRefirioA(String refirioA) { this.refirioA = refirioA; }

    public String getReferidoPor() { return referidoPor; }
    public void setReferidoPor(String referidoPor) { this.referidoPor = referidoPor; }

    public boolean isEsPadre() { return esPadre; }
    public void setEsPadre(boolean esPadre) { this.esPadre = esPadre; }

    public boolean isEsMadre() { return esMadre; }
    public void setEsMadre(boolean esMadre) { this.esMadre = esMadre; }

    public String getNombreHijos() { return nombreHijos; }
    public void setNombreHijos(String nombreHijos) { this.nombreHijos = nombreHijos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRedesSociales() { return redesSociales; }
    public void setRedesSociales(String redesSociales) { this.redesSociales = redesSociales; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getGustosMusicales() { return gustosMusicales; }
    public void setGustosMusicales(String gustosMusicales) { this.gustosMusicales = gustosMusicales; }

    public String getClubFutbol() { return clubFutbol; }
    public void setClubFutbol(String clubFutbol) { this.clubFutbol = clubFutbol; }

    public String getGustoBebidas() { return gustoBebidas; }
    public void setGustoBebidas(String gustoBebidas) { this.gustoBebidas = gustoBebidas; }

    public String getPreferenciasComida() { return preferenciasComida; }
    public void setPreferenciasComida(String preferenciasComida) { this.preferenciasComida = preferenciasComida; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
