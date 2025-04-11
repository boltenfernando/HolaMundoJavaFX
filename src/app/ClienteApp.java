package app;

import controller.ClienteController;
import db.TestSQLite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;

public class ClienteApp extends Application {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    // Controles para el formulario y la tabla
    private TextField txtNombre = new TextField();
    private TextField txtApellido = new TextField();
    private TextField txtDireccion = new TextField();
    private DatePicker dpCumpleaños = new DatePicker();
    private Button btnAgregar = new Button("Agregar");
    private TableView<Cliente> tableClientes = new TableView<>();

    // Botón temporal para resetear contactos (opcional)
    private Button btnResetear = new Button("Resetear Contactos");

    // Botón con ícono de lupa para reabrir el popup de recordatorios
    private Button btnRecordatorios = new Button("🔍");

    // Declaración del controlador
    private ClienteController controller;

    @Override
    public void start(Stage primaryStage) {
        // Configurar base de datos
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnaCumpleaños();

        // Mostrar recordatorios al iniciar (popup)
        RecordatorioService.mostrarRecordatorios();

        // Configurar prompts para los TextField
        txtNombre.setPromptText("Nombre");
        txtApellido.setPromptText("Apellido");
        txtDireccion.setPromptText("Dirección");

        // Instanciar el controlador pasando controles válidos
        controller = new ClienteController(txtNombre, txtApellido, txtDireccion, dpCumpleaños, btnAgregar, tableClientes, clientes, new HBox());

        // Configurar el botón para resetear contactos (temporal)
        btnResetear.setOnAction(e -> {
            TestSQLite.clearTable(); // Borra todos los registros
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resetear Contactos");
            alert.setHeaderText(null);
            alert.setContentText("Se han borrado todos los contactos. Reinicia la aplicación para reconstruir la base de datos con la nueva estructura.");
            alert.showAndWait();
            controller.listarClientes();
        });

        // Configurar el botón de recordatorios (lupa) para volver a mostrar el popup completo
        btnRecordatorios.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        btnRecordatorios.setStyle("-fx-font-size: 14px; -fx-background-color: transparent;");

        // Crear el MenuBar
        MenuBar menuBar = new MenuBar();
        Menu menuOpciones = new Menu("Opciones");
        MenuItem miCrear = new MenuItem("Crear nuevo contacto");
        MenuItem miBuscar = new MenuItem("Buscar contacto");
        MenuItem miVerBase = new MenuItem("Ver base");
        menuOpciones.getItems().addAll(miCrear, miBuscar, miVerBase);
        menuBar.getMenus().add(menuOpciones);

        // Acciones del menú
        miCrear.setOnAction(e -> {
            // Limpiar el formulario y poner foco en el primer campo
            controller.limpiarCampos();
            txtNombre.requestFocus();
        });

        miBuscar.setOnAction(e -> mostrarBusqueda());
        miVerBase.setOnAction(e -> mostrarContactosPorCategoria());

        // Nuevo layout utilizando BorderPane
        BorderPane root = new BorderPane();

        // Panel superior: MenuBar
        root.setTop(menuBar);

        // Panel izquierdo: Panel extendido del formulario (obtenido desde el controlador) y la tabla
        VBox panelIzquierdo = new VBox(10);
        panelIzquierdo.getChildren().addAll(controller.obtenerPanelFormularioExtendido(), btnResetear, tableClientes);
        panelIzquierdo.setPadding(new Insets(10));
        root.setLeft(panelIzquierdo);

        // Panel derecho: Panel de recordatorios reducido junto con el botón de lupa
        VBox panelDerecho = new VBox(10);
        VBox recordatoriosPeq = RecordatorioService.crearPanelRecordatoriosPequeno();
        recordatoriosPeq.setPadding(new Insets(10));
        panelDerecho.getChildren().addAll(recordatoriosPeq, btnRecordatorios);
        panelDerecho.setPadding(new Insets(10));
        root.setRight(panelDerecho);

        Scene scene = new Scene(root, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Listar clientes al iniciar
        controller.listarClientes();
    }

    // Método para mostrar la ventana de búsqueda de contactos
    private void mostrarBusqueda() {
        Stage stageBusqueda = new Stage();
        stageBusqueda.initModality(Modality.APPLICATION_MODAL);
        stageBusqueda.setTitle("Buscar Contacto");

        TextField txtBusqueda = new TextField();
        txtBusqueda.setPromptText("Buscar por nombre, apellido o referencia");
        Button btnBuscar = new Button("Buscar");

        TableView<Cliente> tablaBusqueda = new TableView<>();
        // Configura las columnas mínimas (por ejemplo, nombre, apellido y referencia)
        tablaBusqueda.getColumns().addAll(
            ClienteAppHelper.crearColumna("Nombre", "nombre"),
            ClienteAppHelper.crearColumna("Apellido", "apellido"),
            ClienteAppHelper.crearColumna("Referencia", "referencia")
        );

        btnBuscar.setOnAction(e -> {
            // Filtrar la lista de clientes según el término de búsqueda
            String termino = txtBusqueda.getText().toLowerCase();
            ObservableList<Cliente> filtrados = clientes.filtered(c ->
                c.getNombre().toLowerCase().contains(termino) ||
                c.getApellido().toLowerCase().contains(termino) ||
                (c.getReferencia() != null && c.getReferencia().toLowerCase().contains(termino))
            );
            tablaBusqueda.setItems(filtrados);
        });

        VBox vboxBusqueda = new VBox(10, txtBusqueda, btnBuscar, tablaBusqueda);
        vboxBusqueda.setPadding(new Insets(10));
        Scene sceneBusqueda = new Scene(vboxBusqueda, 600, 400);
        stageBusqueda.setScene(sceneBusqueda);
        stageBusqueda.showAndWait();
    }

    // Método para mostrar la ventana de "Ver base" (contactos por categoría)
    private void mostrarContactosPorCategoria() {
        Stage stageCategoria = new Stage();
        stageCategoria.initModality(Modality.APPLICATION_MODAL);
        stageCategoria.setTitle("Contactos por Categoría");

        // Se crea una tabla para mostrar contactos con sus categorías
        TableView<Cliente> tablaCategoria = new TableView<>();
        tablaCategoria.getColumns().addAll(
            ClienteAppHelper.crearColumna("Nombre", "nombre"),
            ClienteAppHelper.crearColumna("Apellido", "apellido"),
            ClienteAppHelper.crearColumna("Categoría", "categoria")
        );

        // Se filtra la lista para mostrar solo aquellos con categoría asignada
        ObservableList<Cliente> filtrados = clientes.filtered(c -> c.getCategoria() != null && !c.getCategoria().isEmpty());
        tablaCategoria.setItems(filtrados);

        VBox vboxCategoria = new VBox(10, tablaCategoria);
        vboxCategoria.setPadding(new Insets(10));
        Scene sceneCategoria = new Scene(vboxCategoria, 600, 400);
        stageCategoria.setScene(sceneCategoria);
        stageCategoria.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
