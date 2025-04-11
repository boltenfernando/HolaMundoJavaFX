package controller;

import dao.ClienteDAO;
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
import java.util.logging.Logger;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private TableView<Cliente> tableClientes;
    private VBox panelRecordatorios;

    // Controles del formulario (reutilizables)
    private TextField txtNombre, txtApellido, txtReferencia, txtCategoria;
    private DatePicker dpCumpleaños;
    private CheckBox cbEsPadreOMadre;
    private TextField txtGustosMusicales, txtGustosFutbol, txtGustosComidas, txtRedesSociales;
    private TextField txtTelefono, txtEmail, txtDireccion, txtOcupacion;
    private CheckBox cbFueCliente, cbDeseaContacto;
    private DatePicker dpFechaCompraVenta, dpProximoContacto;
    private TextField txtTemasConversacion, txtLugaresVisita, txtDatosAdicionales, txtReferidoPor, txtRefirioA;
    private Button btnAgregar, btnBuscarRecordatorios;

    private VBox panelFormulario; // se arma una vez

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
        inicializarControles();
        configurarTabla();
        configurarRecordatorios();
        armarPanelFormulario();
    }

    private void inicializarControles() {
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");
        txtCategoria = new TextField(); txtCategoria.setPromptText("Categoría");
        txtReferencia = new TextField(); txtReferencia.setPromptText("Referencia");
        dpCumpleaños = new DatePicker();
        cbEsPadreOMadre = new CheckBox("Es padre/madre");
        txtGustosMusicales = new TextField(); txtGustosMusicales.setPromptText("Gustos musicales");
        txtGustosFutbol = new TextField(); txtGustosFutbol.setPromptText("Gustos fútbol");
        txtGustosComidas = new TextField(); txtGustosComidas.setPromptText("Gustos comidas");
        txtRedesSociales = new TextField(); txtRedesSociales.setPromptText("Redes sociales");
        txtTelefono = new TextField(); txtTelefono.setPromptText("Teléfono");
        txtEmail = new TextField(); txtEmail.setPromptText("Email");
        txtDireccion = new TextField(); txtDireccion.setPromptText("Dirección");
        txtOcupacion = new TextField(); txtOcupacion.setPromptText("Ocupación");
        cbFueCliente = new CheckBox("Fue cliente");
        dpFechaCompraVenta = new DatePicker();
        cbDeseaContacto = new CheckBox("Desea contacto");
        dpProximoContacto = new DatePicker();
        txtTemasConversacion = new TextField(); txtTemasConversacion.setPromptText("Temas conversación");
        txtLugaresVisita = new TextField(); txtLugaresVisita.setPromptText("Lugares visita");
        txtDatosAdicionales = new TextField(); txtDatosAdicionales.setPromptText("Datos adicionales");
        txtReferidoPor = new TextField(); txtReferidoPor.setPromptText("Referido por");
        txtRefirioA = new TextField(); txtRefirioA.setPromptText("Refirió a");

        btnAgregar = new Button("Guardar");
        btnAgregar.setOnAction(e -> {
            guardarCliente();
        });
    }

    private void armarPanelFormulario() {
        panelFormulario = new VBox(10,
            new Label("Formulario Cliente"),
            txtCategoria, txtNombre, txtApellido, txtReferencia, dpCumpleaños, cbEsPadreOMadre,
            txtGustosMusicales, txtGustosFutbol, txtGustosComidas, txtRedesSociales,
            txtTelefono, txtEmail, txtDireccion, txtOcupacion, cbFueCliente,
            dpFechaCompraVenta, cbDeseaContacto, dpProximoContacto,
            txtTemasConversacion, txtLugaresVisita, txtDatosAdicionales, txtReferidoPor, txtRefirioA,
            btnAgregar
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
            { btn.setOnAction(e -> cargarParaEdicion(getTableView().getItems().get(getIndex()))); }
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

        tableClientes.getColumns().addAll(colCat, colNom, colApe, colRef, colVer, colEditar, colEliminar);
    }

    private void configurarRecordatorios() {
        panelRecordatorios = RecordatorioService.crearPanelRecordatoriosPequeno();
        Button btnLupa = new Button("🔍");
        btnLupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        panelRecordatorios.getChildren().add(btnLupa);
    }

    public TableView<Cliente> obtenerTablaClientes() {
        return tableClientes;
    }

    public VBox obtenerPanelRecordatoriosConLupa() {
        return panelRecordatorios;
    }

    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
            tableClientes.setItems(clientes);
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", "No se pudo listar los clientes: " + ex.getMessage());
        }
    }

    public void aplicarFiltros(String categoria, String nombre, String apellido) {
        tableClientes.setItems(clientes.filtered(c ->
            (categoria.isEmpty() || c.getCategoria().equals(categoria)) &&
            (nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
            (apellido.isEmpty() || c.getApellido().toLowerCase().contains(apellido.toLowerCase()))
        ));
    }

    public void mostrarFormularioModal() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nuevo Cliente");

        ScrollPane scroll = new ScrollPane(panelFormulario);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Scene scene = new Scene(scroll, 400, 600);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void guardarCliente() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String categoria = txtCategoria.getText();
        if (nombre.isEmpty() || apellido.isEmpty() || categoria.isEmpty()) {
            ErrorHandler.showError("Error", "Nombre, Apellido y Categoría son obligatorios.");
            return;
        }
        Cliente c = new Cliente(0, nombre, apellido, txtReferencia.getText(), dpCumpleaños.getValue(),
            cbEsPadreOMadre.isSelected(), txtGustosMusicales.getText(), txtGustosFutbol.getText(),
            txtGustosComidas.getText(), txtRedesSociales.getText(), txtTelefono.getText(),
            txtEmail.getText(), txtDireccion.getText(), txtOcupacion.getText(),
            cbFueCliente.isSelected(), dpFechaCompraVenta.getValue(), categoria,
            cbDeseaContacto.isSelected(), dpProximoContacto.getValue(), txtTemasConversacion.getText(),
            txtLugaresVisita.getText(), txtDatosAdicionales.getText(),
            txtReferidoPor.getText(), txtRefirioA.getText()
        );
        try {
            ClienteDAO.agregarCliente(c);
            limpiarCampos();
            listarClientes();
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", ex.getMessage());
        }
    }

    public void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtReferencia.clear();
        txtCategoria.clear();
        dpCumpleaños.setValue(null);
        cbEsPadreOMadre.setSelected(false);
        txtGustosMusicales.clear();
        txtGustosFutbol.clear();
        txtGustosComidas.clear();
        txtRedesSociales.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtDireccion.clear();
        txtOcupacion.clear();
        cbFueCliente.setSelected(false);
        dpFechaCompraVenta.setValue(null);
        cbDeseaContacto.setSelected(false);
        dpProximoContacto.setValue(null);
        txtTemasConversacion.clear();
        txtLugaresVisita.clear();
        txtDatosAdicionales.clear();
        txtReferidoPor.clear();
        txtRefirioA.clear();
    }

    private void cargarParaEdicion(Cliente cliente) {
        // cargar datos en formulario...
    }

    private void eliminarConfirmacion(Cliente cliente) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar " + cliente.getNombre() + "?",
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

    private void mostrarDetalle(Cliente cliente) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Detalle Cliente");
        VBox v = new VBox(10,
            new Label("ID: " + cliente.getId()),
            new Label("Nombre: " + cliente.getNombre()),
            new Label("Apellido: " + cliente.getApellido()),
            new Label("Categoría: " + cliente.getCategoria()),
            new Label("Referencia: " + cliente.getReferencia()),
            new Label("Cumpleaños: " + cliente.getCumpleaños()),
            new Label("Es padre/madre: " + cliente.isEsPadreOMadre()),
            new Label("Gustos musicales: " + cliente.getGustosMusicales()),
            new Label("Gustos fútbol: " + cliente.getGustosFutbol()),
            new Label("Gustos comidas: " + cliente.getGustosComidas()),
            new Label("Redes sociales: " + cliente.getRedesSociales()),
            new Label("Teléfono: " + cliente.getTelefono()),
            new Label("Email: " + cliente.getEmail()),
            new Label("Dirección: " + cliente.getDireccion()),
            new Label("Ocupación: " + cliente.getOcupacion()),
            new Label("Fue cliente: " + cliente.isFueCliente()),
            new Label("Fecha compra/venta: " + cliente.getFechaCompraVenta()),
            new Label("Desea contacto: " + cliente.isDeseaContacto()),
            new Label("Próximo contacto: " + cliente.getProximoContacto()),
            new Label("Temas: " + cliente.getTemasConversacion()),
            new Label("Lugares visita: " + cliente.getLugaresVisita()),
            new Label("Datos adicionales: " + cliente.getDatosAdicionales()),
            new Label("Referido por: " + cliente.getReferidoPor()),
            new Label("Refirió a: " + cliente.getRefirioA())
        );
        v.setPadding(new Insets(10));
        Scene sc = new Scene(v, 400, 600);
        s.setScene(sc);
        s.showAndWait();
    }
}
