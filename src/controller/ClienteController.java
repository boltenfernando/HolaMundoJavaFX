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

        HBox toolbar = crearToolbar();

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(tableClientes);
        root.setRight(panelRecordatorios);
        root.setPadding(new Insets(10));
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

    private HBox crearToolbar() {
        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> mostrarFormularioModal(null));

        ChoiceBox<String> cbCategoria = new ChoiceBox<>();
        cbCategoria.getItems().addAll("", "A+", "A", "B", "C", "D");
        cbCategoria.setValue("");
        TextField tfNombre = new TextField(); tfNombre.setPromptText("Nombre");
        TextField tfApellido = new TextField(); tfApellido.setPromptText("Apellido");

        Button btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e -> aplicarFiltros(cbCategoria.getValue(), tfNombre.getText(), tfApellido.getText()));
        Button btnLimpiar = new Button("Limpiar Filtro");
        btnLimpiar.setOnAction(e -> {
            cbCategoria.setValue(""); tfNombre.clear(); tfApellido.clear(); listarClientes();
        });

        HBox filtros = new HBox(5, new Label("Categoría:"), cbCategoria, new Label("Nombre:"), tfNombre, new Label("Apellido:"), tfApellido, btnFiltrar, btnLimpiar);
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox toolbar = new HBox(10, btnNuevo, btnExportarCsv, btnImportarCsv, filtros, spacer, panelRecordatorios);
        toolbar.setPadding(new Insets(10));
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

    private void mostrarFormularioModal(Cliente cliente) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(primaryStage);
        dialog.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");
        dialog.getDialogPane().setContent(new Label("Aquí iría el formulario para: " + (cliente != null ? cliente.getNombre() : "nuevo")));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void configurarRecordatorios() {
        panelRecordatorios = RecordatorioService.crearPanelRecordatoriosPequeno();
        Button lupa = new Button("🔍");
        lupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        panelRecordatorios.getChildren().add(lupa);
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
        try {
            List<Cliente> lista = ClienteDAO.listarClientes();
            try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
                pw.println("id,nombre,apellido,referencia");
                for (Cliente c : lista) {
                    pw.printf("%d,%s,%s,%s%n",
                        c.getId(), c.getNombre(), c.getApellido(), c.getReferencia());
                }
            }
        } catch (Exception ex) {
            ErrorHandler.showError("Error exportando CSV", ex.getMessage());
        }
    }

    private void importarCsv(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1);
                Cliente c = new Cliente();
                if (!cols[0].isEmpty()) c.setId(Integer.parseInt(cols[0]));
                c.setNombre(cols[1]);
                c.setApellido(cols[2]);
                c.setReferencia(cols[3]);
                ClienteDAO.guardarOActualizar(c);
            }
        } catch (Exception ex) {
            ErrorHandler.showError("Error importando CSV", ex.getMessage());
        }
    }

    public Button getBtnExportarCsv() { return btnExportarCsv; }
    public Button getBtnImportarCsv() { return btnImportarCsv; }
}
