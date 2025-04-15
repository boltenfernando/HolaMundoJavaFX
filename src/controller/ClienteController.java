package controller;

import dao.ClienteDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;
import util.ErrorHandler;
import util.LoggerUtil;
import view.FormularioClienteView;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private TableView<Cliente> tableClientes;
    private VBox panelRecordatorios;
    private final FormularioClienteView formView;
    private final Stage primaryStage;
    private final Button btnExportarCsv;
    private final Button btnImportarCsv;

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(Stage stage, ObservableList<Cliente> clientes) {
        this.primaryStage = stage;
        this.clientes = clientes;

        formView = new FormularioClienteView();
        btnExportarCsv = formView.getBtnExportarCsv();
        btnImportarCsv = formView.getBtnImportarCsv();

        configurarTabla();
        configurarRecordatorios();
        configurarEventosCsv();

        // Botón "Nuevo Cliente"
        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> mostrarFormularioModal(null));

        // Filtros
        ChoiceBox<String> cbCategoria = new ChoiceBox<>();
        cbCategoria.getItems().addAll("", "A+", "A", "B", "C", "D");
        cbCategoria.setValue("");
        TextField tfNombre = new TextField(); tfNombre.setPromptText("Nombre");
        TextField tfApellido = new TextField(); tfApellido.setPromptText("Apellido");
        Button btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e ->
            aplicarFiltros(cbCategoria.getValue(), tfNombre.getText(), tfApellido.getText())
        );
        Button btnLimpiar = new Button("Limpiar Filtro");
        btnLimpiar.setOnAction(e -> {
            cbCategoria.setValue("");
            tfNombre.clear();
            tfApellido.clear();
            listarClientes();
        });
        HBox filtros = new HBox(5,
            new Label("Categoría:"), cbCategoria,
            new Label("Nombre:"), tfNombre,
            new Label("Apellido:"), tfApellido,
            btnFiltrar, btnLimpiar
        );

        // Barra superior completa
        HBox topBar = new HBox(10,
            btnNuevo,
            btnExportarCsv, btnImportarCsv,
            filtros
        );
        topBar.setPadding(new Insets(10));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, panelRecordatorios);

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(tableClientes);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.show();

        listarClientes();
    }


    private void configurarTabla() {
        tableClientes = new TableView<>();
        TableColumn<Cliente, String> colCat = new TableColumn<>("Categoría");
        colCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        TableColumn<Cliente, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Cliente, String> colApe = new TableColumn<>("Apellido");
        colApe.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        TableColumn<Cliente, String> colRef = new TableColumn<>("Referencia");
        colRef.setCellValueFactory(new PropertyValueFactory<>("referencia"));

        TableColumn<Cliente, Void> colVer = new TableColumn<>("VER");
        colVer.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("VER");
            {
                btn.setOnAction(e ->
                    ClienteController.this.mostrarDetalle(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEditar = new TableColumn<>("EDITAR");
        colEditar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("EDITAR");
            {
                btn.setOnAction(e ->
                    ClienteController.this.mostrarFormularioModal(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEliminar = new TableColumn<>("ELIMINAR");
        colEliminar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("ELIMINAR");
            {
                btn.setOnAction(e ->
                    ClienteController.this.eliminarConfirmacion(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, String> colRec = new TableColumn<>("Recordatorios");
        colRec.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(String.join(", ", RecordatorioService.getRecordatorios(c.getValue())))
        );

        tableClientes.getColumns().addAll(
            colCat, colNom, colApe, colRef,
            colVer, colEditar, colEliminar, colRec
        );
    }

    private void configurarRecordatorios() {
        panelRecordatorios = RecordatorioService.crearPanelRecordatoriosPequeno();
        Button btnLupa = new Button("🔍");
        btnLupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        panelRecordatorios.getChildren().add(btnLupa);
    }

    private void configurarEventosCsv() {
        btnExportarCsv.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Guardar CSV de Clientes");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            chooser.setInitialFileName("clientes.csv");
            File file = chooser.showSaveDialog(primaryStage);
            if (file != null) exportarCsv(file);
        });

        btnImportarCsv.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Seleccionar CSV de Clientes");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                importarCsv(file);
                listarClientes();
            }
        });
    }

    private void exportarCsv(File file) {
        // ... tu lógica existente ...
    }

    private void importarCsv(File file) {
        // ... tu lógica existente ...
    }

    public void listarClientes() {
        // ... tu lógica existente ...
    }

    public void mostrarFormularioModal(Cliente cliente) {
        // ... tu lógica existente ...
    }

    public void mostrarDetalle(Cliente cliente) {
        // ... tu lógica existente ...
    }

    public void eliminarConfirmacion(Cliente cliente) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar " + cliente.getNombre() + " " + cliente.getApellido() + "?",
            ButtonType.OK, ButtonType.CANCEL);
        a.showAndWait().ifPresent(b -> {
            if (b == ButtonType.OK) {
                try {
                    ClienteDAO.eliminarCliente(cliente.getId());
                    listarClientes();
                } catch (SQLException ex) {
                    ErrorHandler.showError("Error", ex.getMessage());
                }
            }
        });
    }

    /**
     * Filtra la tabla de clientes según categoría, nombre y apellido.
     */
    public void aplicarFiltros(String categoria, String nombre, String apellido) {
        tableClientes.setItems(clientes.filtered(c ->
            (categoria == null || categoria.isEmpty() || c.getCategoria().equals(categoria)) &&
            (nombre == null || nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
            (apellido == null || apellido.isEmpty() || c.getApellido().toLowerCase().contains(apellido.toLowerCase()))
        ));
    }
    
    public Button getBtnExportarCsv() {
        return btnExportarCsv;
    }

    public Button getBtnImportarCsv() {
        return btnImportarCsv;
    }
}
