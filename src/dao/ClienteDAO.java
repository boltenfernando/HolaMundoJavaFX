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

    // Método para agregar un cliente
    public static void agregarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes(nombre, apellido, direccion, cumpleaños) VALUES(?, ?, ?, ?)";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, (cliente.getCumpleaños() != null) ? cliente.getCumpleaños().toString() : null);
            pstmt.executeUpdate();
            logger.info("Cliente agregado: " + cliente.getNombre() + " " + cliente.getApellido());
        } catch (SQLException e) {
            logger.severe("Error al agregar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Método para listar todos los clientes
    public static List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, direccion, cumpleaños FROM clientes";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String direccion = rs.getString("direccion");
                String fechaCumpleStr = rs.getString("cumpleaños");
                LocalDate fechaCumple = (fechaCumpleStr != null && !fechaCumpleStr.isEmpty())
                        ? LocalDate.parse(fechaCumpleStr)
                        : null;
                clientes.add(new Cliente(id, nombre, apellido, direccion, fechaCumple));
            }
            logger.info("Listados " + clientes.size() + " clientes.");
        } catch (SQLException e) {
            logger.severe("Error al listar clientes: " + e.getMessage());
            throw e;
        }
        return clientes;
    }

    // Método para modificar un cliente
    public static void modificarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, direccion = ?, cumpleaños = ? WHERE id = ?";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, (cliente.getCumpleaños() != null) ? cliente.getCumpleaños().toString() : null);
            pstmt.setInt(5, cliente.getId());
            pstmt.executeUpdate();
            logger.info("Cliente modificado: ID " + cliente.getId());
        } catch (SQLException e) {
            logger.severe("Error al modificar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Método para eliminar un cliente por su ID
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
