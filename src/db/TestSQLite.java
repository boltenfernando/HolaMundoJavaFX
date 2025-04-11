package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSQLite {
    private static final String URL = "jdbc:sqlite:clientes.db";

    public static Connection connect() throws SQLException {
        return java.sql.DriverManager.getConnection(URL);
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT," +
            "apellido TEXT," +
            "referencia TEXT," +
            "proximoContacto DATE," +
            "direccion TEXT," +
            "localidad TEXT," +
            "cumpleaños DATE," +
            "datosPersonales TEXT," +
            "datosLaborales TEXT," +
            "datosVenta TEXT," +
            "datosCompra TEXT," +
            "deseaContacto BOOLEAN," +
            "fueCliente BOOLEAN," +
            "fechaCompraVenta DATE," +
            "esReferidor BOOLEAN," +
            "refirioA TEXT," +
            "referidoPor TEXT," +
            "esPadre BOOLEAN," +
            "esMadre BOOLEAN," +
            "nombreHijos TEXT," +
            "telefono TEXT," +
            "redesSociales TEXT," +
            "email TEXT," +
            "ocupacion TEXT," +
            "gustosMusicales TEXT," +
            "clubFutbol TEXT," +
            "gustoBebidas TEXT," +
            "preferenciasComida TEXT," +
            "categoria TEXT" +
        ");";
        try (Connection conn = connect(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Añade una columna si no existe ya en la tabla */
    public static void addColumnIfNotExists(String columnName, String columnDef) {
        try (Connection conn = connect();
             Statement st = conn.createStatement()) {
            // SQLite no soporta ALTER TABLE ADD COLUMN IF NOT EXISTS, así que comprobamos manualmente
            ResultSet rs = st.executeQuery("PRAGMA table_info(clientes);");
            boolean found = false;
            while (rs.next()) {
                if (columnName.equalsIgnoreCase(rs.getString("name"))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                st.execute("ALTER TABLE clientes ADD COLUMN " + columnName + " " + columnDef + ";");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void verificarYAgregarColumnas() {
        // columnas existentes
        addColumnIfNotExists("proximoContacto", "DATE");
        addColumnIfNotExists("direccion", "TEXT");
        addColumnIfNotExists("localidad", "TEXT");
        addColumnIfNotExists("cumpleaños", "DATE");
        addColumnIfNotExists("datosPersonales", "TEXT");
        addColumnIfNotExists("datosLaborales", "TEXT");
        addColumnIfNotExists("datosVenta", "TEXT");
        addColumnIfNotExists("datosCompra", "TEXT");
        addColumnIfNotExists("deseaContacto", "BOOLEAN");
        addColumnIfNotExists("fueCliente", "BOOLEAN");
        addColumnIfNotExists("fechaCompraVenta", "DATE");
        addColumnIfNotExists("esReferidor", "BOOLEAN");
        addColumnIfNotExists("refirioA", "TEXT");
        addColumnIfNotExists("referidoPor", "TEXT");
        addColumnIfNotExists("esPadre", "BOOLEAN");
        addColumnIfNotExists("esMadre", "BOOLEAN");
        addColumnIfNotExists("nombreHijos", "TEXT");
        addColumnIfNotExists("telefono", "TEXT");
        addColumnIfNotExists("redesSociales", "TEXT");
        addColumnIfNotExists("email", "TEXT");
        addColumnIfNotExists("ocupacion", "TEXT");
        addColumnIfNotExists("gustosMusicales", "TEXT");
        addColumnIfNotExists("clubFutbol", "TEXT");
        addColumnIfNotExists("gustoBebidas", "TEXT");
        addColumnIfNotExists("preferenciasComida", "TEXT");
        addColumnIfNotExists("categoria", "TEXT");
    }
}
