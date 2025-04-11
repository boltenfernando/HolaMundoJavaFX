package dao;

import model.Cliente;
import db.TestSQLite;
import util.LoggerUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClienteDAO {
    private static final Logger logger = LoggerUtil.getLogger();

    // Método para agregar un cliente con todos los campos
    public static void agregarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes(nombre, apellido, referencia, cumpleaños, esPadreOMadre, gustosMusicales, gustosFutbol, gustosComidas, redesSociales, telefono, email, direccion, ocupacion, fueCliente, fechaCompraVenta, categoria, deseaContacto, proximoContacto, temasConversacion, lugaresVisita, datosAdicionales, referidoPor, refirioA) "
                   + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getReferencia());
            pstmt.setString(4, (cliente.getCumpleaños() != null) ? cliente.getCumpleaños().toString() : null);
            pstmt.setInt(5, cliente.isEsPadreOMadre() ? 1 : 0);
            pstmt.setString(6, cliente.getGustosMusicales());
            pstmt.setString(7, cliente.getGustosFutbol());
            pstmt.setString(8, cliente.getGustosComidas());
            pstmt.setString(9, cliente.getRedesSociales());
            pstmt.setString(10, cliente.getTelefono());
            pstmt.setString(11, cliente.getEmail());
            pstmt.setString(12, cliente.getDireccion());
            pstmt.setString(13, cliente.getOcupacion());
            pstmt.setInt(14, cliente.isFueCliente() ? 1 : 0);
            pstmt.setString(15, (cliente.getFechaCompraVenta() != null) ? cliente.getFechaCompraVenta().toString() : null);
            pstmt.setString(16, cliente.getCategoria());
            pstmt.setInt(17, cliente.isDeseaContacto() ? 1 : 0);
            pstmt.setString(18, (cliente.getProximoContacto() != null) ? cliente.getProximoContacto().toString() : null);
            pstmt.setString(19, cliente.getTemasConversacion());
            pstmt.setString(20, cliente.getLugaresVisita());
            pstmt.setString(21, cliente.getDatosAdicionales());
            pstmt.setString(22, cliente.getReferidoPor());
            pstmt.setString(23, cliente.getRefirioA());
            pstmt.executeUpdate();
            logger.info("Cliente agregado: " + cliente.getNombre() + " " + cliente.getApellido());
        } catch (SQLException e) {
            logger.severe("Error al agregar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Método para listar todos los clientes (usando el constructor extendido)
    public static List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String referencia = rs.getString("referencia");
                String direccion = rs.getString("direccion");
                String cumpleañosStr = rs.getString("cumpleaños");
                LocalDate cumpleaños = (cumpleañosStr != null && !cumpleañosStr.isEmpty())
                        ? LocalDate.parse(cumpleañosStr) : null;
                boolean esPadreOMadre = rs.getInt("esPadreOMadre") == 1;
                String gustosMusicales = rs.getString("gustosMusicales");
                String gustosFutbol = rs.getString("gustosFutbol");
                String gustosComidas = rs.getString("gustosComidas");
                String redesSociales = rs.getString("redesSociales");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                String ocupacion = rs.getString("ocupacion");
                boolean fueCliente = rs.getInt("fueCliente") == 1;
                String fechaCompraVentaStr = rs.getString("fechaCompraVenta");
                LocalDate fechaCompraVenta = (fechaCompraVentaStr != null && !fechaCompraVentaStr.isEmpty())
                        ? LocalDate.parse(fechaCompraVentaStr) : null;
                String categoria = rs.getString("categoria");
                boolean deseaContacto = rs.getInt("deseaContacto") == 1;
                String proximoContactoStr = rs.getString("proximoContacto");
                LocalDate proximoContacto = (proximoContactoStr != null && !proximoContactoStr.isEmpty())
                        ? LocalDate.parse(proximoContactoStr) : null;
                String temasConversacion = rs.getString("temasConversacion");
                String lugaresVisita = rs.getString("lugaresVisita");
                String datosAdicionales = rs.getString("datosAdicionales");
                String referidoPor = rs.getString("referidoPor");
                String refirioA = rs.getString("refirioA");

                clientes.add(new Cliente(id, nombre, apellido, referencia, cumpleaños, esPadreOMadre,
                        gustosMusicales, gustosFutbol, gustosComidas, redesSociales, telefono, email,
                        direccion, ocupacion, fueCliente, fechaCompraVenta, categoria, deseaContacto,
                        proximoContacto, temasConversacion, lugaresVisita, datosAdicionales, referidoPor, refirioA));
            }
            logger.info("Listados " + clientes.size() + " clientes.");
        } catch (SQLException e) {
            logger.severe("Error al listar clientes: " + e.getMessage());
            throw e;
        }
        return clientes;
    }

    // Método para modificar un cliente (actualiza todos los campos)
    public static void modificarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, referencia = ?, cumpleaños = ?, esPadreOMadre = ?, gustosMusicales = ?, gustosFutbol = ?, gustosComidas = ?, redesSociales = ?, telefono = ?, email = ?, direccion = ?, ocupacion = ?, fueCliente = ?, fechaCompraVenta = ?, categoria = ?, deseaContacto = ?, proximoContacto = ?, temasConversacion = ?, lugaresVisita = ?, datosAdicionales = ?, referidoPor = ?, refirioA = ? "
                   + "WHERE id = ?";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getReferencia());
            pstmt.setString(4, (cliente.getCumpleaños() != null) ? cliente.getCumpleaños().toString() : null);
            pstmt.setInt(5, cliente.isEsPadreOMadre() ? 1 : 0);
            pstmt.setString(6, cliente.getGustosMusicales());
            pstmt.setString(7, cliente.getGustosFutbol());
            pstmt.setString(8, cliente.getGustosComidas());
            pstmt.setString(9, cliente.getRedesSociales());
            pstmt.setString(10, cliente.getTelefono());
            pstmt.setString(11, cliente.getEmail());
            pstmt.setString(12, cliente.getDireccion());
            pstmt.setString(13, cliente.getOcupacion());
            pstmt.setInt(14, cliente.isFueCliente() ? 1 : 0);
            pstmt.setString(15, (cliente.getFechaCompraVenta() != null) ? cliente.getFechaCompraVenta().toString() : null);
            pstmt.setString(16, cliente.getCategoria());
            pstmt.setInt(17, cliente.isDeseaContacto() ? 1 : 0);
            pstmt.setString(18, (cliente.getProximoContacto() != null) ? cliente.getProximoContacto().toString() : null);
            pstmt.setString(19, cliente.getTemasConversacion());
            pstmt.setString(20, cliente.getLugaresVisita());
            pstmt.setString(21, cliente.getDatosAdicionales());
            pstmt.setString(22, cliente.getReferidoPor());
            pstmt.setString(23, cliente.getRefirioA());
            pstmt.setInt(24, cliente.getId());
            pstmt.executeUpdate();
            logger.info("Cliente modificado: ID " + cliente.getId());
        } catch (SQLException e) {
            logger.severe("Error al modificar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Método para eliminar un cliente por su ID (se agregará confirmación en el controlador)
    public static void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.info("Cliente eliminado: ID " + id);
        } catch (SQLException e) {
            logger.severe("Error al eliminar cliente: " + e.getMessage());
            throw e;
        }
    }
}
