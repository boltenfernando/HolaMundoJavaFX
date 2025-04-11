package service;

import dao.ClienteDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.Cliente;
import util.ErrorHandler;
import db.TestSQLite;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioService {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM");

    public static void mostrarRecordatorios() {
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(7);

        List<String> mensajes = new ArrayList<>();
        mensajes.addAll(fetchMensajes("cumpleaños", "🎂 Hoy cumple años %s %s!", "📅 Cumpleaños de %s %s el %s.", hoy, fin));
        mensajes.addAll(fetchMensajes("fechaCompraVenta", "🏡 Hoy aniversario de %s %s!", "📅 Aniversario de %s %s el %s.", hoy, fin));
        mensajes.addAll(fetchMensajesFecha("proximoContacto", "📞 Hoy contactar a %s %s!", "⏰ Contactar a %s %s el %s.", hoy, fin));

        String cuerpo = mensajes.isEmpty()
            ? "No hay recordatorios en los próximos 7 días."
            : String.join("\n", mensajes);

        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("🔔 Recordatorios próximos");
        a.setHeaderText(null);
        a.setContentText(cuerpo);
        a.showAndWait();
    }

    private static List<String> fetchMensajes(String campo, String hoyFmt, String proxFmt, LocalDate hoy, LocalDate fin) {
        List<String> res = new ArrayList<>();
        String sql = String.format(
            "SELECT nombre, apellido, %s FROM clientes " +
            "WHERE %s IS NOT NULL AND strftime('%%m-%%d', %s) BETWEEN strftime('%%m-%%d', ?) AND strftime('%%m-%%d', ?)",
            campo, campo, campo);
        try (Connection conn = TestSQLite.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hoy.toString());
            ps.setString(2, fin.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                LocalDate fecha = LocalDate.parse(rs.getString(campo));
                String fechaFmt = fecha.format(FORMAT);
                if (fecha.equals(hoy)) {
                    res.add(String.format(hoyFmt, nom, ape));
                } else {
                    res.add(String.format(proxFmt, nom, ape, fechaFmt));
                }
            }
        } catch (SQLException e) {
            ErrorHandler.showError("Error", "Error obteniendo recordatorios: " + e.getMessage());
        }
        return res;
    }

    private static List<String> fetchMensajesFecha(String campo, String hoyFmt, String proxFmt, LocalDate hoy, LocalDate fin) {
        List<String> res = new ArrayList<>();
        String sql = String.format(
            "SELECT nombre, apellido, %s FROM clientes " +
            "WHERE %s IS NOT NULL AND date(%s) BETWEEN date(?) AND date(?)",
            campo, campo, campo);
        try (Connection conn = TestSQLite.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hoy.toString());
            ps.setString(2, fin.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                LocalDate fecha = LocalDate.parse(rs.getString(campo));
                String fechaFmt = fecha.format(FORMAT);
                if (fecha.equals(hoy)) {
                    res.add(String.format(hoyFmt, nom, ape));
                } else {
                    res.add(String.format(proxFmt, nom, ape, fechaFmt));
                }
            }
        } catch (SQLException e) {
            ErrorHandler.showError("Error", "Error obteniendo recordatorios: " + e.getMessage());
        }
        return res;
    }

    public static List<String> getRecordatorios(Cliente c) {
        List<String> recs = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(7);

        if (c.getCumpleaños() != null) {
            LocalDate d = c.getCumpleaños().withYear(hoy.getYear());
            if (!d.isBefore(hoy) && !d.isAfter(fin)) {
                recs.add("🎂 " + d.format(FORMAT));
            }
        }
        if (c.getFechaCompraVenta() != null) {
            LocalDate d = c.getFechaCompraVenta().withYear(hoy.getYear());
            if (!d.isBefore(hoy) && !d.isAfter(fin)) {
                recs.add("🏡 " + d.format(FORMAT));
            }
        }
        if (c.getProximoContacto() != null) {
            LocalDate d = c.getProximoContacto();
            if (!d.isBefore(hoy) && !d.isAfter(fin)) {
                recs.add("📅 " + d.format(FORMAT));
            }
        }
        return recs;
    }

    public static VBox crearPanelRecordatoriosPequeno() {
        VBox vbox = new VBox(5);
        try {
            for (Cliente c : ClienteDAO.listarClientes()) {
                List<String> recs = getRecordatorios(c);
                if (!recs.isEmpty()) {
                    Label label = new Label(c.getNombre() + " " + c.getApellido() + ": " + String.join(", ", recs));
                    vbox.getChildren().add(label);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("⚠️ Recordatorios");
            a.setHeaderText("No se pudo cargar el panel de recordatorios");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
        return vbox;
    }
}
