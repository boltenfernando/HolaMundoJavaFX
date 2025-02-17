package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSQLite {
    // URL de la base de datos (en el directorio actual)
    private static final String URL = "jdbc:sqlite:mi_base.db";

    // Método para establecer conexión con la base de datos
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC"); // Cargar el driver JDBC
            return DriverManager.getConnection(URL); // Conexión a SQLite
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Error al conectar con SQLite: " + e.getMessage());
            return null;
        }
    }

 // Método para crear la tabla 'clientes'
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "nombre TEXT NOT NULL, "
                   + "apellido TEXT NOT NULL, "
                   + "direccion TEXT NOT NULL, "
                   + "cumpleaños DATE);"; // Agregamos la columna 'cumpleaños'

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'clientes' creada o ya existe.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    // Método para limpiar la tabla (opcional, por si querés reiniciar datos)
    public static void clearTable() {
        String sql = "DELETE FROM clientes";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Se eliminaron " + rowsAffected + " registros de la tabla 'clientes'.");
        } catch (SQLException e) {
            System.out.println("Error al limpiar la tabla: " + e.getMessage());
        }
    }

    // Método para eliminar la tabla (opcional)
    public static void dropTable() {
        String sql = "DROP TABLE IF EXISTS clientes";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'clientes' eliminada.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar la tabla: " + e.getMessage());
        }
    }
    
    public static void verificarYAgregarColumnaCumpleaños() {
        String checkColumnSQL = "PRAGMA table_info(clientes);";
        boolean columnaExiste = false;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkColumnSQL)) {

            while (rs.next()) {
                String columnName = rs.getString("name");
                if ("cumpleaños".equals(columnName)) {
                    columnaExiste = true;
                    break;
                }
            }

            if (!columnaExiste) {
                System.out.println("La columna 'cumpleaños' no existe. Se agregará ahora...");
                String alterTableSQL = "ALTER TABLE clientes ADD COLUMN cumpleaños DATE;";
                stmt.execute(alterTableSQL);
                System.out.println("Columna 'cumpleaños' agregada correctamente.");
            } else {
                System.out.println("La columna 'cumpleaños' ya existe.");
            }

        } catch (SQLException e) {
            System.out.println("Error verificando/agregando la columna 'cumpleaños': " + e.getMessage());
        }
    }

}