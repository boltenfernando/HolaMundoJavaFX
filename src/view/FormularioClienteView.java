package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FormularioClienteView {

    private final VBox root;
    private final Button btnExportarCsv;
    private final Button btnImportarCsv;
    private final Button btnGuardar;

    public FormularioClienteView() {
        root = new VBox(10);
        root.setPadding(new Insets(10));

        btnExportarCsv = new Button("Exportar CSV");
        btnImportarCsv = new Button("Importar CSV");
        btnGuardar = new Button("Guardar");

        HBox filaBotones = new HBox(10, btnExportarCsv, btnImportarCsv);
        filaBotones.setPadding(new Insets(10));

        root.getChildren().addAll(filaBotones); // Podés sumar más nodos después si querés
    }

    public VBox getView() {
        return root;
    }

    public Button getBtnExportarCsv() {
        return btnExportarCsv;
    }

    public Button getBtnImportarCsv() {
        return btnImportarCsv;
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
