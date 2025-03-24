package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import db.TestSQLite;
import util.ErrorHandler;

public class RecordatorioService {

    /**
     * Obtiene y muestra un alert con los recordatorios de cumpleaños.
     */
    public static void mostrarRecordatorios() {
        LocalDate hoy = LocalDate.now();
        LocalDate semanaFutura = hoy.plusDays(7);

        String sql = "SELECT nombre, apellido, cumpleaños FROM clientes " +
                     "WHERE strftime('%m-%d', cumpleaños) BETWEEN strftime('%m-%d', ?) AND strftime('%m-%d', ?)";
        StringBuilder recordatoriosHoy = new StringBuilder();
        StringBuilder recordatoriosSemana = new StringBuilder();

        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hoy.toString());
            pstmt.setString(2, semanaFutura.toString());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String fechaCumpleañosStr = rs.getString("cumpleaños");
                LocalDate fechaCumpleaños = (fechaCumpleañosStr != null) ? LocalDate.parse(fechaCumpleañosStr) : null;

                if (fechaCumpleaños != null) {
                    if (fechaCumpleaños.equals(hoy)) {
                        recordatoriosHoy.append("🎉 Hoy es el cumpleaños de ")
                            .append(nombre).append(" ").append(apellido).append("!\n");
                    } else {
                        recordatoriosSemana.append("📅 Esta semana cumple ")
                            .append(nombre).append(" ").append(apellido)
                            .append(" el ").append(fechaCumpleaños.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .append(".\n");
                    }
                }
            }

            String mensajeFinal = "";
            if (recordatoriosHoy.length() > 0) {
                mensajeFinal += recordatoriosHoy.toString() + "\n";
            }
            if (recordatoriosSemana.length() > 0) {
                mensajeFinal += recordatoriosSemana.toString();
            }
            if (mensajeFinal.isEmpty()) {
                mensajeFinal = "Hoy no hay cumpleaños ni recordatorios pendientes.";
            }

            // Usamos el ErrorHandler para mostrar un alert informativo
            ErrorHandler.showInfo("🎂 Recordatorios de Cumpleaños", mensajeFinal);

        } catch (SQLException e) {
            ErrorHandler.showError("Error", "Error obteniendo recordatorios: " + e.getMessage());
        }
    }
}
