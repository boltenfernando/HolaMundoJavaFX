package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDAO {
    private static final String URL = "jdbc:sqlite:clientes.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static List<Cliente> listarClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setReferencia(rs.getString("referencia"));
                String pc = rs.getString("proximoContacto");
                c.setProximoContacto(pc != null ? LocalDate.parse(pc) : null);
                c.setDireccion(rs.getString("direccion"));
                c.setLocalidad(rs.getString("localidad"));
                String cum = rs.getString("cumpleaños");
                c.setCumpleaños(cum != null ? LocalDate.parse(cum) : null);
                c.setDatosPersonales(rs.getString("datosPersonales"));
                c.setDatosLaborales(rs.getString("datosLaborales"));
                c.setDatosVenta(rs.getString("datosVenta"));
                c.setDatosCompra(rs.getString("datosCompra"));
                c.setDeseaContacto(rs.getBoolean("deseaContacto"));
                c.setFueCliente(rs.getBoolean("fueCliente"));
                String fcv = rs.getString("fechaCompraVenta");
                c.setFechaCompraVenta(fcv != null ? LocalDate.parse(fcv) : null);
                c.setEsReferidor(rs.getBoolean("esReferidor"));
                c.setRefirioA(rs.getString("refirioA"));
                c.setReferidoPor(rs.getString("referidoPor"));
                c.setEsPadre(rs.getBoolean("esPadre"));
                c.setEsMadre(rs.getBoolean("esMadre"));
                c.setNombreHijos(rs.getString("nombreHijos"));
                c.setTelefono(rs.getString("telefono"));
                c.setRedesSociales(rs.getString("redesSociales"));
                c.setEmail(rs.getString("email"));
                c.setOcupacion(rs.getString("ocupacion"));
                c.setGustosMusicales(rs.getString("gustosMusicales"));
                c.setClubFutbol(rs.getString("clubFutbol"));
                c.setGustoBebidas(rs.getString("gustoBebidas"));
                c.setPreferenciasComida(rs.getString("preferenciasComida"));
                c.setCategoria(rs.getString("categoria"));
                lista.add(c);
            }
        }
        return lista;
    }

    public static void agregarCliente(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes(" +
            "nombre,apellido,referencia,proximoContacto,direccion,localidad,cumpleaños," +
            "datosPersonales,datosLaborales,datosVenta,datosCompra,deseaContacto,fueCliente," +
            "fechaCompraVenta,esReferidor,refirioA,referidoPor,esPadre,esMadre,nombreHijos," +
            "telefono,redesSociales,email,ocupacion,gustosMusicales,clubFutbol,gustoBebidas," +
            "preferenciasComida,categoria) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getReferencia());
            ps.setString(4, c.getProximoContacto() != null ? c.getProximoContacto().toString() : null);
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getLocalidad());
            ps.setString(7, c.getCumpleaños() != null ? c.getCumpleaños().toString() : null);
            ps.setString(8, c.getDatosPersonales());
            ps.setString(9, c.getDatosLaborales());
            ps.setString(10, c.getDatosVenta());
            ps.setString(11, c.getDatosCompra());
            ps.setBoolean(12, c.isDeseaContacto());
            ps.setBoolean(13, c.isFueCliente());
            ps.setString(14, c.getFechaCompraVenta() != null ? c.getFechaCompraVenta().toString() : null);
            ps.setBoolean(15, c.isEsReferidor());
            ps.setString(16, c.getRefirioA());
            ps.setString(17, c.getReferidoPor());
            ps.setBoolean(18, c.isEsPadre());
            ps.setBoolean(19, c.isEsMadre());
            ps.setString(20, c.getNombreHijos());
            ps.setString(21, c.getTelefono());
            ps.setString(22, c.getRedesSociales());
            ps.setString(23, c.getEmail());
            ps.setString(24, c.getOcupacion());
            ps.setString(25, c.getGustosMusicales());
            ps.setString(26, c.getClubFutbol());
            ps.setString(27, c.getGustoBebidas());
            ps.setString(28, c.getPreferenciasComida());
            ps.setString(29, c.getCategoria());
            ps.executeUpdate();
        }
    }

    public static void modificarCliente(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET " +
            "nombre=?,apellido=?,referencia=?,proximoContacto=?,direccion=?,localidad=?,cumpleaños=?," +
            "datosPersonales=?,datosLaborales=?,datosVenta=?,datosCompra=?,deseaContacto=?,fueCliente=?," +
            "fechaCompraVenta=?,esReferidor=?,refirioA=?,referidoPor=?,esPadre=?,esMadre=?,nombreHijos=?," +
            "telefono=?,redesSociales=?,email=?,ocupacion=?,gustosMusicales=?,clubFutbol=?,gustoBebidas=?," +
            "preferenciasComida=?,categoria=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getReferencia());
            ps.setString(4, c.getProximoContacto() != null ? c.getProximoContacto().toString() : null);
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getLocalidad());
            ps.setString(7, c.getCumpleaños() != null ? c.getCumpleaños().toString() : null);
            ps.setString(8, c.getDatosPersonales());
            ps.setString(9, c.getDatosLaborales());
            ps.setString(10, c.getDatosVenta());
            ps.setString(11, c.getDatosCompra());
            ps.setBoolean(12, c.isDeseaContacto());
            ps.setBoolean(13, c.isFueCliente());
            ps.setString(14, c.getFechaCompraVenta() != null ? c.getFechaCompraVenta().toString() : null);
            ps.setBoolean(15, c.isEsReferidor());
            ps.setString(16, c.getRefirioA());
            ps.setString(17, c.getReferidoPor());
            ps.setBoolean(18, c.isEsPadre());
            ps.setBoolean(19, c.isEsMadre());
            ps.setString(20, c.getNombreHijos());
            ps.setString(21, c.getTelefono());
            ps.setString(22, c.getRedesSociales());
            ps.setString(23, c.getEmail());
            ps.setString(24, c.getOcupacion());
            ps.setString(25, c.getGustosMusicales());
            ps.setString(26, c.getClubFutbol());
            ps.setString(27, c.getGustoBebidas());
            ps.setString(28, c.getPreferenciasComida());
            ps.setString(29, c.getCategoria());
            ps.setInt(30, c.getId());
            ps.executeUpdate();
        }
    }

    public static void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void guardarOActualizar(Cliente c) throws SQLException {
        if (c.getId() == 0) {
            agregarCliente(c);
        } else {
            modificarCliente(c);
        }
    }
}
