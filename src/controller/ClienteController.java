package controller;

import dao.ClienteDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private final Stage primaryStage;
    private final TableView<Cliente> tableClientes = new TableView<>();
    private final VBox panelRecordatorios = new VBox(10);
    private final Button btnExportarCsv = new Button("Exportar CSV");
    private final Button btnImportarCsv = new Button("Importar CSV");

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(Stage stage, ObservableList<Cliente> clientes) {
        this.primaryStage = stage;
        this.clientes = clientes;

        configurarTabla();
        configurarRecordatorios();

        HBox toolbar = crearToolbar();

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(tableClientes);
        root.setRight(panelRecordatorios);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.show();

        listarClientes();
    }

    private HBox crearToolbar() {
        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> mostrarFormularioModal(null));

        ChoiceBox<String> cbCat = new ChoiceBox<>(FXCollections.observableArrayList("", "A+", "A", "B", "C", "D"));
        cbCat.setValue("");
        TextField tfNom = new TextField(); tfNom.setPromptText("Nombre");
        TextField tfApe = new TextField(); tfApe.setPromptText("Apellido");
        Button btnFil = new Button("Filtrar");
        btnFil.setOnAction(e -> aplicarFiltros(cbCat.getValue(), tfNom.getText(), tfApe.getText()));
        Button btnLim = new Button("Limpiar Filtro");
        btnLim.setOnAction(e -> { cbCat.setValue(""); tfNom.clear(); tfApe.clear(); listarClientes(); });

        HBox filtros = new HBox(5,
            new Label("Categoría:"), cbCat,
            new Label("Nombre:"), tfNom,
            new Label("Apellido:"), tfApe,
            btnFil, btnLim
        );
        filtros.setPadding(new Insets(10));

        HBox toolbar = new HBox(10, btnNuevo, filtros);
        toolbar.setPadding(new Insets(10));
        return toolbar;
    }

    private void configurarTabla() {
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
                    mostrarDetalle(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEdit = new TableColumn<>("EDITAR");
        colEdit.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("EDITAR");
            {
                btn.setOnAction(e ->
                    mostrarFormularioModal(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colDel = new TableColumn<>("ELIMINAR");
        colDel.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("ELIMINAR");
            {
                btn.setOnAction(e ->
                    eliminarConfirmacion(getTableView().getItems().get(getIndex()))
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

        tableClientes.getColumns().setAll(colCat, colNom, colApe, colRef, colVer, colEdit, colDel, colRec);
        tableClientes.setItems(clientes);
    }

    private void configurarRecordatorios() {
        panelRecordatorios.getChildren().clear();

        Button btnLupa = new Button("🔍");
        btnLupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());

        btnExportarCsv.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Exportar CSV");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            chooser.setInitialFileName("clientes.csv");
            File file = chooser.showSaveDialog(primaryStage);
            if (file != null) exportarCsv(file);
        });

        btnImportarCsv.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Importar CSV");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) { importarCsv(file); listarClientes(); }
        });

        panelRecordatorios.getChildren().addAll(btnLupa, btnExportarCsv, btnImportarCsv);
        panelRecordatorios.setSpacing(10);
        panelRecordatorios.setPadding(new Insets(10));

        // Forzar ancho mínimo para que se vea
        panelRecordatorios.setMinWidth(120);
        BorderPane.setMargin(panelRecordatorios, new Insets(0, 0, 0, 10));
    }

    private void exportarCsv(File file) {
        // ... tu lógica existente ...
    }

    private void importarCsv(File file) {
        // ... tu lógica existente ...
    }

    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
        } catch (SQLException ex) {
            ErrorHandler.showError("Error al listar clientes", ex.getMessage());
        }
    }

    public void aplicarFiltros(String cat, String nom, String ape) {
        tableClientes.setItems(clientes.filtered(c ->
            (cat.isEmpty() || c.getCategoria().equals(cat)) &&
            (nom.isEmpty() || c.getNombre().toLowerCase().contains(nom.toLowerCase())) &&
            (ape.isEmpty() || c.getApellido().toLowerCase().contains(ape.toLowerCase()))
        ));
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
}
