package model;

import java.time.LocalDate;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String referencia;         // Ej: “vecino de Apiola”, “hija de Norma”
    private LocalDate cumpleaños;
    private boolean esPadreOMadre;       // Indica si es padre o madre
    private String gustosMusicales;
    private String gustosFutbol;        // Ej: equipo o club favorito
    private String gustosComidas;       // Bebidas y comidas favoritas
    private String redesSociales;       // Ej: Facebook, Instagram, etc.
    private String telefono;
    private String email;
    private String direccion;
    private String ocupacion;           // Ej: contador, abogado, etc.
    private boolean fueCliente;         // Si ya fue cliente
    private LocalDate fechaCompraVenta; // Fecha de compra o venta
    private String categoria;           // A+, A, B, C, D
    private boolean deseaContacto;      // Quiere mantener contacto
    private LocalDate proximoContacto;  // Fecha para retomar el vínculo
    private String temasConversacion;   // Checklist o notas
    private String lugaresVisita;       // Casa, barrio, café, etc.
    private String datosAdicionales;    // Datos laborales, familiares, etc.
    private String referidoPor;         // Quién lo refirió
    private String refirioA;            // A quién refirió

    // Constructor completo
    public Cliente(int id, String nombre, String apellido, String referencia, LocalDate cumpleaños, boolean esPadreOMadre,
                   String gustosMusicales, String gustosFutbol, String gustosComidas, String redesSociales,
                   String telefono, String email, String direccion, String ocupacion, boolean fueCliente,
                   LocalDate fechaCompraVenta, String categoria, boolean deseaContacto, LocalDate proximoContacto,
                   String temasConversacion, String lugaresVisita, String datosAdicionales, String referidoPor,
                   String refirioA) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.referencia = referencia;
        this.cumpleaños = cumpleaños;
        this.esPadreOMadre = esPadreOMadre;
        this.gustosMusicales = gustosMusicales;
        this.gustosFutbol = gustosFutbol;
        this.gustosComidas = gustosComidas;
        this.redesSociales = redesSociales;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.fueCliente = fueCliente;
        this.fechaCompraVenta = fechaCompraVenta;
        this.categoria = categoria;
        this.deseaContacto = deseaContacto;
        this.proximoContacto = proximoContacto;
        this.temasConversacion = temasConversacion;
        this.lugaresVisita = lugaresVisita;
        this.datosAdicionales = datosAdicionales;
        this.referidoPor = referidoPor;
        this.refirioA = refirioA;
    }

    // Constructor básico para compatibilidad (se usan valores por defecto para los campos extendidos)
    public Cliente(int id, String nombre, String apellido, String direccion, LocalDate cumpleaños) {
        this(id, nombre, apellido, "", cumpleaños, false, "", "", "", "", "", "", direccion, "", false, null, "", false, null, "", "", "", "", "");
    }

    // Getters y setters para cada campo
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    
    public LocalDate getCumpleaños() { return cumpleaños; }
    public void setCumpleaños(LocalDate cumpleaños) { this.cumpleaños = cumpleaños; }
    
    public boolean isEsPadreOMadre() { return esPadreOMadre; }
    public void setEsPadreOMadre(boolean esPadreOMadre) { this.esPadreOMadre = esPadreOMadre; }
    
    public String getGustosMusicales() { return gustosMusicales; }
    public void setGustosMusicales(String gustosMusicales) { this.gustosMusicales = gustosMusicales; }
    
    public String getGustosFutbol() { return gustosFutbol; }
    public void setGustosFutbol(String gustosFutbol) { this.gustosFutbol = gustosFutbol; }
    
    public String getGustosComidas() { return gustosComidas; }
    public void setGustosComidas(String gustosComidas) { this.gustosComidas = gustosComidas; }
    
    public String getRedesSociales() { return redesSociales; }
    public void setRedesSociales(String redesSociales) { this.redesSociales = redesSociales; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    
    public boolean isFueCliente() { return fueCliente; }
    public void setFueCliente(boolean fueCliente) { this.fueCliente = fueCliente; }
    
    public LocalDate getFechaCompraVenta() { return fechaCompraVenta; }
    public void setFechaCompraVenta(LocalDate fechaCompraVenta) { this.fechaCompraVenta = fechaCompraVenta; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public boolean isDeseaContacto() { return deseaContacto; }
    public void setDeseaContacto(boolean deseaContacto) { this.deseaContacto = deseaContacto; }
    
    public LocalDate getProximoContacto() { return proximoContacto; }
    public void setProximoContacto(LocalDate proximoContacto) { this.proximoContacto = proximoContacto; }
    
    public String getTemasConversacion() { return temasConversacion; }
    public void setTemasConversacion(String temasConversacion) { this.temasConversacion = temasConversacion; }
    
    public String getLugaresVisita() { return lugaresVisita; }
    public void setLugaresVisita(String lugaresVisita) { this.lugaresVisita = lugaresVisita; }
    
    public String getDatosAdicionales() { return datosAdicionales; }
    public void setDatosAdicionales(String datosAdicionales) { this.datosAdicionales = datosAdicionales; }
    
    public String getReferidoPor() { return referidoPor; }
    public void setReferidoPor(String referidoPor) { this.referidoPor = referidoPor; }
    
    public String getRefirioA() { return refirioA; }
    public void setRefirioA(String refirioA) { this.refirioA = refirioA; }
}
