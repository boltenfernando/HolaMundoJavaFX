package app;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Cliente;

public class ClienteAppHelper {
    public static TableColumn<Cliente, String> crearColumna(String titulo, String propiedad) {
        TableColumn<Cliente, String> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        return columna;
    }
}
