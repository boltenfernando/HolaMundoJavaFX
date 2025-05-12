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

        btnGuardar = new Button("Guardar");

        HBox filaBotones = new HBox(10, btnGuardar);
        filaBotones.setPadding(new Insets(10));

        root.getChildren().add(filaBotones);
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
        System.out.println("Precargando cliente...");
    }

    public boolean isNew() {
        return true;
    }
}