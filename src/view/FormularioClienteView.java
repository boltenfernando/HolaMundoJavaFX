package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FormularioClienteView {

    private final VBox root;
    private final Button btnGuardar;

    public FormularioClienteView() {
        root = new VBox(10);
        root.setPadding(new Insets(10));

        // Botón de guardado
        btnGuardar = new Button("Guardar");

        // Fila de botones
        HBox filaBotones = new HBox(10, btnGuardar);
        filaBotones.setPadding(new Insets(10));

        root.getChildren().addAll(filaBotones);
    }

    public VBox getView() {
        return root;
    }

    public Button getBtnGuardar() {
        return btnGuardar;
    }

    public boolean guardar() {
        System.out.println("Guardando cliente...");
        return true;
    }

    public boolean actualizar() {
        System.out.println("Actualizando cliente...");
        return true;
    }

    public void precargar(model.Cliente cliente) {
        // Aquí cargás los datos del cliente si viene uno
        System.out.println("Precargando cliente...");
    }

    public boolean isNew() {
        // Lógica temporal de ejemplo
        return true;
    }
}
