package controller;

import dao.ClienteDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;
import util.ErrorHandler;
import util.LoggerUtil;
import view.FormularioClienteView;

import java.sql.SQLException;
import java.util.logging.Logger;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private TableView<Cliente> tableClientes;
    private VBox panelRecordatorios;
    private final FormularioClienteView formView;
    private final Stage primaryStage;

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(Stage stage, ObservableList<Cliente> clientes) {
        this.primaryStage = stage;
        this.clientes = clientes;

        formView = new FormularioClienteView();

        configurarTabla();
        configurarRecordatorios();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(crearToolbar());
        root.setCenter(tableClientes);
        root.setRight(panelRecordatorios);
        BorderPane.setMargin(tableClientes, new Insets(0, 10, 0, 0));

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
        tableClientes.getColumns().addAll(colCat, colNom, colApe, colRef);
    }

    private VBox crearToolbar() {
        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> mostrarFormularioModal(null));

        ChoiceBox<String> cbCategoria = new ChoiceBox<>();
        cbCategoria.getItems().addAll("", "A+", "A", "B", "C", "D");
        cbCategoria.setValue("");
        TextField tfNombre = new TextField();
        tfNombre.setPromptText("Nombre");
        TextField tfApellido = new TextField();
        tfApellido.setPromptText("Apellido");

        Button btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e -> aplicarFiltros(cbCategoria.getValue(), tfNombre.getText(), tfApellido.getText()));
        Button btnLimpiar = new Button("Limpiar Filtro");
        btnLimpiar.setOnAction(e -> {
            cbCategoria.setValue("");
            tfNombre.clear();
            tfApellido.clear();
            listarClientes();
        });

        HBox fila = new HBox(10,
            btnNuevo,
            new Separator(Orientation.VERTICAL),
            new Label("Categoría:"), cbCategoria,
            new Label("Nombre:"), tfNombre,
            new Label("Apellido:"), tfApellido,
            btnFiltrar,
            btnLimpiar
        );
        fila.setPadding(new Insets(10));
        fila.setSpacing(5);

        VBox toolbar = new VBox(fila);
        toolbar.setPadding(new Insets(0, 10, 0, 10));
        return toolbar;
    }

    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
            tableClientes.setItems(clientes);
        } catch (SQLException ex) {
            ErrorHandler.showError("Error al listar clientes", ex.getMessage());
        }
    }

    public void aplicarFiltros(String categoria, String nombre, String apellido) {
        tableClientes.setItems(clientes.filtered(c ->
            (categoria == null || categoria.isEmpty() || c.getCategoria().equals(categoria)) &&
            (nombre == null || nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
            (apellido == null || apellido.isEmpty() || c.getApellido().toLowerCase().contains(apellido.toLowerCase()))
        ));
    }

    public void eliminarConfirmacion(Cliente cliente) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar " + cliente.getNombre() + " " + cliente.getApellido() + "?", ButtonType.OK, ButtonType.CANCEL);
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

    private void mostrarFormularioModal(Cliente cliente) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(primaryStage);
        dialog.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");
        dialog.getDialogPane().setContent(formView.getView());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void configurarRecordatorios() {
        panelRecordatorios = RecordatorioService.crearPanelRecordatoriosPequeno();
        Button lupa = new Button("🔍");
        lupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        panelRecordatorios.getChildren().add(lupa);
    }
}
