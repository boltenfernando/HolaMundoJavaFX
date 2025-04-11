package app;

import controller.ClienteController;
import db.TestSQLite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;

public class ClienteApp extends Application {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private ClienteController controller;

    @Override
    public void start(Stage primaryStage) {
        // Inicialización DB y recordatorios
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnaCumpleaños();
        RecordatorioService.mostrarRecordatorios();

        controller = new ClienteController(clientes);

        // Botón Nuevo Cliente (izquierda)
        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> controller.mostrarFormularioModal(null));

        VBox panelIzquierdo = new VBox(btnNuevo);
        panelIzquierdo.setPadding(new Insets(10));
        panelIzquierdo.setSpacing(10);

        // Panel central: filtros + tabla
        ChoiceBox<String> cbCategoria = new ChoiceBox<>();
        cbCategoria.getItems().addAll("", "A+", "A", "B", "C", "D");
        cbCategoria.setValue("");
        TextField tfNombre = new TextField(); tfNombre.setPromptText("Nombre");
        TextField tfApellido = new TextField(); tfApellido.setPromptText("Apellido");
        Button btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e -> controller.aplicarFiltros(cbCategoria.getValue(), tfNombre.getText(), tfApellido.getText()));

        HBox filtros = new HBox(10,
            new Label("Categoría:"), cbCategoria,
            new Label("Nombre:"), tfNombre,
            new Label("Apellido:"), tfApellido,
            btnFiltrar
        );
        filtros.setPadding(new Insets(10));

        ScrollPane scrollTabla = new ScrollPane(controller.obtenerTablaClientes());
        scrollTabla.setFitToWidth(true);
        scrollTabla.setFitToHeight(true);

        VBox panelCentral = new VBox(filtros, scrollTabla);
        VBox.setVgrow(scrollTabla, Priority.ALWAYS);

        // Panel derecho: recordatorios
        VBox panelDerecho = controller.obtenerPanelRecordatoriosConLupa();
        panelDerecho.setPadding(new Insets(10));

        // Layout principal
        BorderPane root = new BorderPane();
        root.setLeft(panelIzquierdo);
        root.setCenter(panelCentral);
        root.setRight(panelDerecho);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setMaximized(true);
        primaryStage.show();

        controller.listarClientes();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
