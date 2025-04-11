package controller;

import dao.ClienteDAO;
import model.Cliente;
import util.ErrorHandler;
import util.LoggerUtil;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Logger;

public class ClienteController {
    // Controles existentes
    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtDireccion;
    private DatePicker dpCumpleaños;
    public TableView<Cliente> tableClientes;
    private ObservableList<Cliente> clientes;
    private Button btnAgregar;
    private HBox formContainer;
    
    // Controles adicionales para datos extendidos
    private TextField txtReferencia;
    private CheckBox cbEsPadreOMadre;
    private TextField txtTelefono;
    private TextField txtEmail;
    private TextField txtOcupacion;
    private TextField txtCategoria;      // Para clasificar: A+, A, B, C o D
    private TextField txtReferidoPor;
    private TextField txtRefirioA;
    
    // Botón para ver contactos por categoría
    private Button btnVerPorCategoria;

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(TextField txtNombre, TextField txtApellido, TextField txtDireccion,
                             DatePicker dpCumpleaños, Button btnAgregar, TableView<Cliente> tableClientes,
                             ObservableList<Cliente> clientes, HBox formContainer) {
        this.txtNombre = txtNombre;
        this.txtApellido = txtApellido;
        this.txtDireccion = txtDireccion;
        this.dpCumpleaños = dpCumpleaños;
        this.btnAgregar = btnAgregar;
        this.tableClientes = tableClientes;
        this.clientes = clientes;
        this.formContainer = formContainer;
        inicializarControlesAdicionales();
        setupEventHandlers();
        applyStyles();
    }

    // Inicializa controles extra para datos extendidos y el botón de ver contactos por categoría
    private void inicializarControlesAdicionales() {
        txtReferencia = new TextField();
        txtReferencia.setPromptText("Referencia");
        cbEsPadreOMadre = new CheckBox("Es padre/madre");
        txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");
        txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtOcupacion = new TextField();
        txtOcupacion.setPromptText("Ocupación");
        txtCategoria = new TextField();
        txtCategoria.setPromptText("Categoría (A+, A, B, C, D)");
        txtReferidoPor = new TextField();
        txtReferidoPor.setPromptText("Referido por");
        txtRefirioA = new TextField();
        txtRefirioA.setPromptText("Refirió a");

        btnVerPorCategoria = new Button("Ver contactos por Categoría");
        btnVerPorCategoria.setOnAction(e -> mostrarContactosPorCategoria());
    }

    private void setupEventHandlers() {
        btnAgregar.setOnAction(e -> agregarCliente());
    }

    private void applyStyles() {
        txtNombre.getStyleClass().add("custom-text-field");
        txtApellido.getStyleClass().add("custom-text-field");
        txtDireccion.getStyleClass().add("custom-text-field");
        dpCumpleaños.getStyleClass().add("custom-date-picker");
        btnAgregar.getStyleClass().add("custom-button");

        // Estilos para controles adicionales
        txtReferencia.getStyleClass().add("custom-text-field");
        txtTelefono.getStyleClass().add("custom-text-field");
        txtEmail.getStyleClass().add("custom-text-field");
        txtOcupacion.getStyleClass().add("custom-text-field");
        txtCategoria.getStyleClass().add("custom-text-field");
        txtReferidoPor.getStyleClass().add("custom-text-field");
        txtRefirioA.getStyleClass().add("custom-text-field");
        btnVerPorCategoria.getStyleClass().add("custom-button");
    }

    // Agregar cliente: se recogen los datos extendidos y se utiliza el constructor completo
    public void agregarCliente() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();
        LocalDate cumpleaños = dpCumpleaños.getValue();

        // Validar campos básicos
        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            ErrorHandler.showError("Error", "Nombre, Apellido y Dirección son obligatorios.");
            return;
        }
        // Recoger datos extendidos (se pueden agregar validaciones extra según convenga)
        String referencia = txtReferencia.getText();
        boolean esPadreOMadre = cbEsPadreOMadre.isSelected();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();
        String ocupacion = txtOcupacion.getText();
        String categoria = txtCategoria.getText();
        String referidoPor = txtReferidoPor.getText();
        String refirioA = txtRefirioA.getText();

        // Los demás campos se dejan en blanco o nulos por ahora
        Cliente cliente = new Cliente(0, nombre, apellido, referencia, cumpleaños, esPadreOMadre,
                "", "", "", "", telefono, email, direccion, ocupacion, false, null, categoria, false,
                null, "", "", "", referidoPor, refirioA);
        try {
            ClienteDAO.agregarCliente(cliente);
            logger.info("Cliente agregado: " + nombre + " " + apellido);
            limpiarCampos();
            listarClientes();
        } catch (SQLException ex) {
            logger.severe("Error al agregar cliente: " + ex.getMessage());
            ErrorHandler.showError("Error", "No se pudo agregar el cliente: " + ex.getMessage());
        }
    }

    // Listar clientes
    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
            tableClientes.setItems(clientes);
            logger.info("Listado de clientes actualizado. Total: " + clientes.size());
        } catch (SQLException ex) {
            logger.severe("Error al listar clientes: " + ex.getMessage());
            ErrorHandler.showError("Error", "No se pudo listar los clientes: " + ex.getMessage());
        }
    }

    // Modificar cliente (actualiza los campos básicos; se podría extender para incluir los nuevos campos en la edición)
    public void modificarCliente(Cliente cliente) {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();
        LocalDate cumpleaños = dpCumpleaños.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            ErrorHandler.showError("Error", "Nombre, Apellido y Dirección son obligatorios.");
            return;
        }
        // Para la modificación se pueden recoger también los nuevos campos (se omiten aquí por simplicidad)
        Cliente clienteActualizado = new Cliente(cliente.getId(), nombre, apellido, cliente.getReferencia(), cumpleaños, cliente.isEsPadreOMadre(),
                cliente.getGustosMusicales(), cliente.getGustosFutbol(), cliente.getGustosComidas(), cliente.getRedesSociales(),
                cliente.getTelefono(), cliente.getEmail(), direccion, cliente.getOcupacion(), cliente.isFueCliente(),
                cliente.getFechaCompraVenta(), cliente.getCategoria(), cliente.isDeseaContacto(), cliente.getProximoContacto(),
                cliente.getTemasConversacion(), cliente.getLugaresVisita(), cliente.getDatosAdicionales(), cliente.getReferidoPor(), cliente.getRefirioA());
        try {
            ClienteDAO.modificarCliente(clienteActualizado);
            logger.info("Cliente modificado: ID " + cliente.getId());
            limpiarCampos();
            listarClientes();
            btnAgregar.setText("Agregar");
            btnAgregar.setOnAction(e -> agregarCliente());
        } catch (SQLException ex) {
            logger.severe("Error al modificar cliente: " + ex.getMessage());
            ErrorHandler.showError("Error", "No se pudo modificar el cliente: " + ex.getMessage());
        }
    }

    // Eliminar cliente con confirmación
    public void eliminarCliente(Cliente cliente) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Estás seguro de eliminar a " + cliente.getNombre() + " " + cliente.getApellido() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ClienteDAO.eliminarCliente(cliente.getId());
                    logger.info("Cliente eliminado: ID " + cliente.getId());
                    listarClientes();
                } catch (SQLException ex) {
                    logger.severe("Error al eliminar cliente: " + ex.getMessage());
                    ErrorHandler.showError("Error", "No se pudo eliminar el cliente: " + ex.getMessage());
                }
            }
        });
    }

    // Cargar datos de un cliente en el formulario para editar
    public void cargarClienteEnFormulario(Cliente cliente) {
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDireccion.setText(cliente.getDireccion());
        dpCumpleaños.setValue(cliente.getCumpleaños());
        txtReferencia.setText(cliente.getReferencia());
        cbEsPadreOMadre.setSelected(cliente.isEsPadreOMadre());
        txtTelefono.setText(cliente.getTelefono());
        txtEmail.setText(cliente.getEmail());
        txtOcupacion.setText(cliente.getOcupacion());
        txtCategoria.setText(cliente.getCategoria());
        txtReferidoPor.setText(cliente.getReferidoPor());
        txtRefirioA.setText(cliente.getRefirioA());
        logger.info("Cargando datos del cliente en el formulario: ID " + cliente.getId());
        btnAgregar.setText("Modificar");
        btnAgregar.setOnAction(e -> modificarCliente(cliente));
    }

    // Limpiar todos los campos del formulario
    public void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtDireccion.clear();
        dpCumpleaños.setValue(null);
        txtReferencia.clear();
        cbEsPadreOMadre.setSelected(false);
        txtTelefono.clear();
        txtEmail.clear();
        txtOcupacion.clear();
        txtCategoria.clear();
        txtReferidoPor.clear();
        txtRefirioA.clear();
    }

    // Mostrar contactos por categoría en una nueva ventana
    private void mostrarContactosPorCategoria() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Contactos por Categoría");

        // Crear un TableView similar al existente, pero filtrado
        TableView<Cliente> tablaCategoria = new TableView<>();
        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        TableColumn<Cliente, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        tablaCategoria.getColumns().addAll(colNombre, colApellido, colCategoria);

        // Filtrar la lista de clientes según la categoría (si se ingresa en txtCategoria)
        String filtro = txtCategoria.getText().trim();
        ObservableList<Cliente> listaFiltrada = clientes.filtered(c -> filtro.isEmpty() || filtro.equalsIgnoreCase(c.getCategoria()));
        tablaCategoria.setItems(listaFiltrada);

        VBox vbox = new VBox(10, tablaCategoria);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.showAndWait();
    }

    // Para integrar el botón "Ver contactos por Categoría" en el layout del formulario,
    // se puede agregar un método que retorne un panel con todos los controles.
    public VBox obtenerPanelFormularioExtendido() {
        VBox panel = new VBox(10);
        panel.getChildren().addAll(
                new Label("Datos Básicos:"),
                txtNombre, txtApellido, txtDireccion, dpCumpleaños,
                new Label("Datos Extendidos:"),
                txtReferencia, cbEsPadreOMadre, txtTelefono, txtEmail, txtOcupacion, txtCategoria, txtReferidoPor, txtRefirioA,
                btnAgregar, btnVerPorCategoria
        );
        panel.setPadding(new Insets(10));
        return panel;
    }
    
}
