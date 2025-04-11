package app;

import controller.ClienteController;
import db.TestSQLite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;

public class ClienteApp extends Application {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private ClienteController controller;

    @Override
    public void start(Stage primaryStage) {
        // 1) Crear tabla y verificar columnas antes de cualquier acceso al DAO
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnas();

        // 2) Mostrar recordatorios
        try {
            RecordatorioService.mostrarRecordatorios();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3) Instanciar el controlador y construir la UI
        controller = new ClienteController(clientes);

        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> {
            System.out.println("DEBUG: Nuevo Cliente clic");
            controller.mostrarFormularioModal(null);
        });

        VBox panelIzquierdo = new VBox(btnNuevo);
        panelIzquierdo.setPadding(new Insets(10));

        ChoiceBox<String> cbCategoria = new ChoiceBox<>();
        cbCategoria.getItems().addAll("", "A+", "A", "B", "C", "D");
        cbCategoria.setValue("");
        TextField tfNombre = new TextField(); tfNombre.setPromptText("Nombre");
        TextField tfApellido = new TextField(); tfApellido.setPromptText("Apellido");
        Button btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e -> controller.aplicarFiltros(cbCategoria.getValue(), tfNombre.getText(), tfApellido.getText()));
        Button btnLimpiar = new Button("Limpiar Filtro");
        btnLimpiar.setOnAction(e -> {
            cbCategoria.setValue("");
            tfNombre.clear();
            tfApellido.clear();
            controller.listarClientes();
        });

        HBox filtros = new HBox(10,
            new Label("Categoría:"), cbCategoria,
            new Label("Nombre:"), tfNombre,
            new Label("Apellido:"), tfApellido,
            btnFiltrar, btnLimpiar
        );
        filtros.setPadding(new Insets(10));

        ScrollPane scrollTabla = new ScrollPane(controller.obtenerTablaClientes());
        scrollTabla.setFitToWidth(true);

        VBox panelCentral = new VBox(filtros, scrollTabla);
        VBox.setVgrow(scrollTabla, Priority.ALWAYS);

        VBox panelDerecho = controller.obtenerPanelRecordatoriosConLupa();
        panelDerecho.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setLeft(panelIzquierdo);
        root.setCenter(panelCentral);
        root.setRight(panelDerecho);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setMaximized(true);
        primaryStage.show();

        // 4) Listar clientes
        controller.listarClientes();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
