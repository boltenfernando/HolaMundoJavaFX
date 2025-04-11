package controller;

import dao.ClienteDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;
import util.ErrorHandler;
import util.LoggerUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private TableView<Cliente> tableClientes;
    private VBox panelRecordatorios;

    // Form controls
    private ComboBox<String> cmbCategoria;
    private TextField txtNombre, txtApellido, txtReferencia;
    private DatePicker dpProximoContacto;
    private Separator sep1;
    private TextField txtDireccion, txtLocalidad;
    private DatePicker dpCumpleaños;
    private Separator sep2;
    private TextArea taDatosPersonales, taDatosLaborales, taDatosVenta, taDatosCompra;
    private Separator sep3;
    private CheckBox cbDeseaContacto, cbFueCliente;
    private DatePicker dpFechaCompraVenta;
    private Separator sep4;
    private CheckBox cbEsReferidor;
    private TextField txtRefirioA, txtReferidoPor;
    private Separator sep5;
    private CheckBox cbEsPadre, cbEsMadre;
    private TextField txtHijos;
    private Separator sep6;
    private TextField txtTelefono, txtRedes, txtEmail;
    private ComboBox<String> cmbOcupacion;
    private TextField txtGustosMusicales, txtClubFutbol, txtBebidas, txtComida;

    private Button btnGuardar;
    private VBox panelFormulario;

    private static final Logger logger = LoggerUtil.getLogger();
    private static final List<String> CATEGORIAS = Arrays.asList("A+", "A", "B", "C", "D");
    private static final List<String> OCUPACIONES = Arrays.asList("Abogado", "Contador", "Médico", "Escribano");

    public ClienteController(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
        inicializarControles();
        armarPanelFormulario();
        configurarTabla();
        configurarRecordatorios();
    }

    private void inicializarControles() {
        cmbCategoria = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(CATEGORIAS));
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");
        txtReferencia = new TextField(); txtReferencia.setPromptText("Referencia");
        dpProximoContacto = new DatePicker();

        sep1 = new Separator();

        txtDireccion = new TextField(); txtDireccion.setPromptText("Dirección");
        txtLocalidad = new TextField(); txtLocalidad.setPromptText("Localidad");
        dpCumpleaños = new DatePicker();

        sep2 = new Separator();

        taDatosPersonales = new TextArea(); taDatosPersonales.setPromptText("Datos Personales / Familiares");
        taDatosLaborales = new TextArea(); taDatosLaborales.setPromptText("Datos Laborales");
        taDatosVenta = new TextArea(); taDatosVenta.setPromptText("Datos de Venta");
        taDatosCompra = new TextArea(); taDatosCompra.setPromptText("Datos de Compra");

        sep3 = new Separator();

        cbDeseaContacto = new CheckBox("Quiero tener contacto");
        cbFueCliente = new CheckBox("Es cliente");
        dpFechaCompraVenta = new DatePicker();

        sep4 = new Separator();

        cbEsReferidor = new CheckBox("Es referidor?");
        txtRefirioA = new TextField(); txtRefirioA.setPromptText("Refirió a");
        txtReferidoPor = new TextField(); txtReferidoPor.setPromptText("Fue referido por");

        sep5 = new Separator();

        cbEsPadre = new CheckBox("Es padre");
        cbEsMadre = new CheckBox("Es madre");
        txtHijos = new TextField(); txtHijos.setPromptText("Nombre de los hijos");

        sep6 = new Separator();

        txtTelefono = new TextField(); txtTelefono.setPromptText("Teléfono");
        txtRedes = new TextField(); txtRedes.setPromptText("Redes sociales");
        txtEmail = new TextField(); txtEmail.setPromptText("Email");
        cmbOcupacion = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(OCUPACIONES));
        cmbOcupacion.setPromptText("Ocupación");

        txtGustosMusicales = new TextField(); txtGustosMusicales.setPromptText("Gustos musicales");
        txtClubFutbol = new TextField(); txtClubFutbol.setPromptText("Club de fútbol");
        txtBebidas = new TextField(); txtBebidas.setPromptText("Gusto de bebidas");
        txtComida = new TextField(); txtComida.setPromptText("Preferencias de comida");

        btnGuardar = new Button();
    }

    private void armarPanelFormulario() {
        panelFormulario = new VBox(8,
            new Label("Categoría:"), cmbCategoria,
            new Label("Nombre:"), txtNombre,
            new Label("Apellido:"), txtApellido,
            new Label("Referencia:"), txtReferencia,
            new Label("Próximo contacto:"), dpProximoContacto,
            sep1,
            new Label("Dirección:"), txtDireccion,
            new Label("Localidad:"), txtLocalidad,
            new Label("Fecha cumpleaños:"), dpCumpleaños,
            sep2,
            new Label("Datos Personales / Familiares:"), taDatosPersonales,
            new Label("Datos Laborales:"), taDatosLaborales,
            new Label("Datos de Venta:"), taDatosVenta,
            new Label("Datos de Compra:"), taDatosCompra,
            sep3,
            cbDeseaContacto, cbFueCliente,
            new Label("Fecha compra/venta (opcional):"), dpFechaCompraVenta,
            sep4,
            cbEsReferidor,
            new Label("Refirió a:"), txtRefirioA,
            new Label("Fue referido por:"), txtReferidoPor,
            sep5,
            cbEsPadre, cbEsMadre,
            new Label("Nombre de los hijos:"), txtHijos,
            sep6,
            new Label("Teléfono:"), txtTelefono,
            new Label("Redes sociales:"), txtRedes,
            new Label("Email:"), txtEmail,
            new Label("Ocupación:"), cmbOcupacion,
            new Label("Gustos musicales:"), txtGustosMusicales,
            new Label("Club de fútbol:"), txtClubFutbol,
            new Label("Gusto de bebidas:"), txtBebidas,
            new Label("Preferencias de comida:"), txtComida,
            btnGuardar
        );
        panelFormulario.setPadding(new Insets(10));
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
            { btn.setOnAction(e -> mostrarDetalle(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEditar = new TableColumn<>("EDITAR");
        colEditar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("EDITAR");
            { btn.setOnAction(e -> mostrarFormularioModal(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEliminar = new TableColumn<>("ELIMINAR");
        colEliminar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("ELIMINAR");
            { btn.setOnAction(e -> eliminarConfirmacion(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, String> colRec = new TableColumn<>("Recordatorios");
        colRec.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(String.join(", ", RecordatorioService.getRecordatorios(c.getValue())))
        );

        tableClientes.getColumns().addAll(colCat, colNom, colApe, colRef, colVer, colEditar, colEliminar, colRec);

        tableClientes.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !RecordatorioService.getRecordatorios(item).isEmpty()) {
                    setStyle("-fx-background-color: rgba(255,255,0,0.3);");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void configurarRecordatorios() {
        panelRecordatorios = RecordatorioService.crearPanelRecordatoriosPequeno();
        Button btnLupa = new Button("🔍");
        btnLupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        panelRecordatorios.getChildren().add(btnLupa);
    }

    public TableView<Cliente> obtenerTablaClientes() { return tableClientes; }
    public VBox obtenerPanelRecordatoriosConLupa() { return panelRecordatorios; }

    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
            tableClientes.setItems(clientes);
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", ex.getMessage());
        }
    }

    public void aplicarFiltros(String categoria, String nombre, String apellido) {
        tableClientes.setItems(clientes.filtered(c ->
            (categoria.isEmpty() || c.getCategoria().equals(categoria)) &&
            (nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
            (apellido.isEmpty() || c.getApellido().toLowerCase().contains(apellido.toLowerCase()))
        ));
    }

    public void mostrarFormularioModal(Cliente cliente) {
        // ... mismo que antes ...
    }

    private boolean guardarCliente() {
        // ... mismo que antes ...
        return true;
    }

    private boolean actualizarCliente(Cliente cliente) {
        // ... mismo que antes ...
        return true;
    }

    private Cliente buildClienteFromForm(int id) {
        // ... mismo que antes ...
        return new Cliente(/* todos los parámetros */);
    }

    public void limpiarCampos() {
        // ... mismo que antes ...
    }

    private void eliminarConfirmacion(Cliente cliente) {
        // ... mismo que antes ...
    }

    private void mostrarDetalle(Cliente cliente) {
        // ... mismo que antes ...
    }
}
