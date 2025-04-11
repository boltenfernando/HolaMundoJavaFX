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
    private DatePicker dpCumpleaños;
    private CheckBox cbEsPadreOMadre;
    private TextField txtGustosMusicales, txtGustosFutbol, txtGustosComidas, txtRedesSociales;
    private TextField txtTelefono, txtEmail, txtDireccion, txtOcupacion;
    private CheckBox cbFueCliente, cbDeseaContacto;
    private DatePicker dpFechaCompraVenta, dpProximoContacto;
    private TextField txtTemasConversacion, txtLugaresVisita, txtDatosAdicionales, txtReferidoPor, txtRefirioA;
    private Button btnGuardar;

    private VBox panelFormulario; // panel usado en modal

    private static final Logger logger = LoggerUtil.getLogger();
    private static final List<String> CATEGORIAS = Arrays.asList("A+", "A", "B", "C", "D");

    public ClienteController(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
        inicializarControles();
        configurarTabla();
        configurarRecordatorios();
        armarPanelFormulario();
    }

    private void inicializarControles() {
        cmbCategoria = new ComboBox<>();
        cmbCategoria.getItems().addAll(CATEGORIAS);

        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");
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

        btnGuardar = new Button();
    }

    private void armarPanelFormulario() {
        panelFormulario = new VBox(10,
            new Label("Formulario Cliente"),
            new Label("Categoría:"), cmbCategoria,
            new Label("Nombre:"), txtNombre,
            new Label("Apellido:"), txtApellido,
            new Label("Referencia:"), txtReferencia,
            new Label("Cumpleaños:"), dpCumpleaños,
            cbEsPadreOMadre,
            new Label("Gustos musicales:"), txtGustosMusicales,
            new Label("Gustos fútbol:"), txtGustosFutbol,
            new Label("Gustos comidas:"), txtGustosComidas,
            new Label("Redes sociales:"), txtRedesSociales,
            new Label("Teléfono:"), txtTelefono,
            new Label("Email:"), txtEmail,
            new Label("Dirección:"), txtDireccion,
            new Label("Ocupación:"), txtOcupacion,
            cbFueCliente,
            new Label("Fecha compra/venta:"), dpFechaCompraVenta,
            cbDeseaContacto,
            new Label("Próximo contacto:"), dpProximoContacto,
            new Label("Temas conversación:"), txtTemasConversacion,
            new Label("Lugares visita:"), txtLugaresVisita,
            new Label("Datos adicionales:"), txtDatosAdicionales,
            new Label("Referido por:"), txtReferidoPor,
            new Label("Refirió a:"), txtRefirioA,
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

    /** 
     * Abre el formulario modal. Si cliente==null -> nuevo, else edición. 
     */
    public void mostrarFormularioModal(Cliente cliente) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");

        // Precarga si es edición
        if (cliente != null) {
            cmbCategoria.setValue(cliente.getCategoria());
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
            txtReferencia.setText(cliente.getReferencia());
            dpCumpleaños.setValue(cliente.getCumpleaños());
            cbEsPadreOMadre.setSelected(cliente.isEsPadreOMadre());
            txtGustosMusicales.setText(cliente.getGustosMusicales());
            txtGustosFutbol.setText(cliente.getGustosFutbol());
            txtGustosComidas.setText(cliente.getGustosComidas());
            txtRedesSociales.setText(cliente.getRedesSociales());
            txtTelefono.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getEmail());
            txtDireccion.setText(cliente.getDireccion());
            txtOcupacion.setText(cliente.getOcupacion());
            cbFueCliente.setSelected(cliente.isFueCliente());
            dpFechaCompraVenta.setValue(cliente.getFechaCompraVenta());
            cbDeseaContacto.setSelected(cliente.isDeseaContacto());
            dpProximoContacto.setValue(cliente.getProximoContacto());
            txtTemasConversacion.setText(cliente.getTemasConversacion());
            txtLugaresVisita.setText(cliente.getLugaresVisita());
            txtDatosAdicionales.setText(cliente.getDatosAdicionales());
            txtReferidoPor.setText(cliente.getReferidoPor());
            txtRefirioA.setText(cliente.getRefirioA());
        } else {
            limpiarCampos();
        }

        btnGuardar.setText(cliente == null ? "Guardar" : "Actualizar");
        btnGuardar.setOnAction(e -> {
            boolean ok;
            if (cliente == null) {
                ok = guardarCliente();
            } else {
                ok = actualizarCliente(cliente);
            }
            if (ok) {
                dialog.close();
            }
        });

        ScrollPane scroll = new ScrollPane(panelFormulario);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Scene scene = new Scene(scroll, 400, 600);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /** @return true si guardó correctamente */
    private boolean guardarCliente() {
        Cliente c = buildClienteFromForm(0);
        if (c == null) return false;
        try {
            ClienteDAO.agregarCliente(c);
            listarClientes();
            return true;
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", ex.getMessage());
            return false;
        }
    }

    /** @return true si actualizó correctamente */
    private boolean actualizarCliente(Cliente cliente) {
        Cliente c = buildClienteFromForm(cliente.getId());
        if (c == null) return false;
        try {
            ClienteDAO.modificarCliente(c);
            listarClientes();
            return true;
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", ex.getMessage());
            return false;
        }
    }

    private Cliente buildClienteFromForm(int id) {
        String categoria = cmbCategoria.getValue();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        if (categoria == null || nombre.isEmpty() || apellido.isEmpty()) {
            ErrorHandler.showError("Error", "Categoría, Nombre y Apellido son obligatorios.");
            return null;
        }
        return new Cliente(id, nombre, apellido, txtReferencia.getText(), dpCumpleaños.getValue(),
            cbEsPadreOMadre.isSelected(), txtGustosMusicales.getText(), txtGustosFutbol.getText(),
            txtGustosComidas.getText(), txtRedesSociales.getText(), txtTelefono.getText(),
            txtEmail.getText(), txtDireccion.getText(), txtOcupacion.getText(),
            cbFueCliente.isSelected(), dpFechaCompraVenta.getValue(), categoria,
            cbDeseaContacto.isSelected(), dpProximoContacto.getValue(), txtTemasConversacion.getText(),
            txtLugaresVisita.getText(), txtDatosAdicionales.getText(),
            txtReferidoPor.getText(), txtRefirioA.getText()
        );
    }

    public void limpiarCampos() {
        cmbCategoria.setValue(null);
        txtNombre.clear();
        txtApellido.clear();
        txtReferencia.clear();
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

    private void eliminarConfirmacion(Cliente cliente) {
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

    private void mostrarDetalle(Cliente cliente) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Detalle Cliente");
        VBox v = new VBox(10,
            new Label("ID: " + cliente.getId()),
            new Label("Categoría: " + cliente.getCategoria()),
            new Label("Nombre: " + cliente.getNombre()),
            new Label("Apellido: " + cliente.getApellido()),
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
            new Label("Temas conversación: " + cliente.getTemasConversacion()),
            new Label("Lugares visita: " + cliente.getLugaresVisita()),
            new Label("Datos adicionales: " + cliente.getDatosAdicionales()),
            new Label("Referido por: " + cliente.getReferidoPor()),
            new Label("Refirió a: " + cliente.getRefirioA())
        );
        v.setPadding(new Insets(10));
        Scene sc = new Scene(new ScrollPane(v), 400, 600);
        s.setScene(sc);
        s.showAndWait();
    }
}
