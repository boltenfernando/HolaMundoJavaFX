package app;

import controller.ClienteController;
import db.TestSQLite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;

public class ClienteApp extends Application {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private ClienteController controller;

    @Override
    public void start(Stage primaryStage) {
        // Inicialización de base de datos y recordatorios
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnaCumpleaños();
        RecordatorioService.mostrarRecordatorios();

        // Crear el controlador
        controller = new ClienteController(clientes);

        // Menu superior
        MenuBar menuBar = new MenuBar();
        Menu menuOpciones = new Menu("Opciones");
        MenuItem miCrear = new MenuItem("Crear nuevo contacto");
        MenuItem miBuscar = new MenuItem("Buscar contacto");
        MenuItem miVerBase = new MenuItem("Ver base");
        menuOpciones.getItems().addAll(miCrear, miBuscar, miVerBase);
        menuBar.getMenus().add(menuOpciones);

        miCrear.setOnAction(e -> controller.mostrarFormularioNuevo());
        miBuscar.setOnAction(e -> controller.mostrarBusqueda());
        miVerBase.setOnAction(e -> controller.mostrarContactosPorCategoria());

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(menuBar);

        // Panel izquierdo con scroll
        VBox contenidoIzquierdo = new VBox(10,
            controller.obtenerPanelFormulario(),
            controller.obtenerTablaClientes()
        );
        contenidoIzquierdo.setPadding(new Insets(10));

        ScrollPane scrollIzquierdo = new ScrollPane(contenidoIzquierdo);
        scrollIzquierdo.setFitToWidth(true);
        scrollIzquierdo.setFitToHeight(true);

        root.setLeft(scrollIzquierdo);

        // Panel derecho (recordatorios + lupa)
        VBox panelDerecho = controller.obtenerPanelRecordatoriosConLupa();
        panelDerecho.setPadding(new Insets(10));
        root.setRight(panelDerecho);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Listar clientes al iniciar
        controller.listarClientes();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
