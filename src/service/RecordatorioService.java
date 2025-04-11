package service;

import db.TestSQLite;
import util.ErrorHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioService {

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
            ErrorHandler.showInfo("🎂 Recordatorios de Cumpleaños", mensajeFinal);
        } catch (SQLException e) {
            ErrorHandler.showError("Error", "Error obteniendo recordatorios: " + e.getMessage());
        }
    }

    private static List<String> obtenerRecordatoriosComoLista() {
        List<String> lista = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate semanaFutura = hoy.plusDays(7);
        String sql = "SELECT nombre, apellido, cumpleaños FROM clientes " +
                     "WHERE strftime('%m-%d', cumpleaños) BETWEEN strftime('%m-%d', ?) AND strftime('%m-%d', ?)";
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
                        lista.add("🎉 Hoy cumple: " + nombre + " " + apellido);
                    } else {
                        String fechaFormateada = fechaCumpleaños.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        lista.add("📅 Cumple " + nombre + " " + apellido + " el " + fechaFormateada);
                    }
                }
            }
            if (lista.isEmpty()) {
                lista.add("Hoy no hay cumpleaños ni recordatorios pendientes.");
            }
        } catch (SQLException e) {
            lista.clear();
            lista.add("Error obteniendo recordatorios: " + e.getMessage());
        }
        return lista;
    }

    public static VBox crearPanelRecordatoriosPequeno() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));
        List<String> recordatorios = obtenerRecordatoriosComoLista();
        for (String rec : recordatorios) {
            Label lbl = new Label(rec);
            vbox.getChildren().add(lbl);
        }
        vbox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
        return vbox;
    }
}
