package controller;

import dao.ClienteDAO;
import model.Cliente;
import util.LoggerUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Logger;

public class ClienteController {
    // Referencias a los controles de la interfaz
    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtDireccion;
    private DatePicker dpCumpleaños;
    private TableView<Cliente> tableClientes;
    private ObservableList<Cliente> clientes;
    private Button btnAgregar; // Botón que se usará para agregar o modificar
    private HBox formContainer; // Contenedor del formulario (opcional, para tener referencia)

    // Logger
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
        setupEventHandlers();
        applyStyles();
    }

    // Configura los eventos iniciales
    private void setupEventHandlers() {
        btnAgregar.setOnAction(e -> agregarCliente());
    }
    
    // Aplica clases de estilo a los controles para mejorar la apariencia según el CSS
    private void applyStyles() {
        // Asegurate de que en tu archivo CSS estén definidas estas clases.
        txtNombre.getStyleClass().add("text-field");
        txtApellido.getStyleClass().add("text-field");
        txtDireccion.getStyleClass().add("text-field");
        dpCumpleaños.getStyleClass().add("date-picker");
        btnAgregar.getStyleClass().add("button");
        // Si tenés más controles, también podés agregarles estilo.
    }

    // Método para agregar un cliente
    public void agregarCliente() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();
        LocalDate fechaCumpleaños = dpCumpleaños.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }
        Cliente cliente = new Cliente(0, nombre, apellido, direccion, fechaCumpleaños);
        try {
            ClienteDAO.agregarCliente(cliente);
            logger.info("Cliente agregado: " + nombre + " " + apellido);
            limpiarCampos();
            listarClientes();
        } catch (SQLException ex) {
            logger.severe("Error al agregar cliente: " + ex.getMessage());
            mostrarAlerta("Error", "No se pudo agregar el cliente: " + ex.getMessage());
        }
    }

    // Método para listar los clientes
    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
            tableClientes.setItems(clientes);
            logger.info("Listado de clientes actualizado. Total: " + clientes.size());
        } catch (SQLException ex) {
            logger.severe("Error al listar clientes: " + ex.getMessage());
            mostrarAlerta("Error", "No se pudo listar los clientes: " + ex.getMessage());
        }
    }

    // Método para modificar un cliente
    public void modificarCliente(Cliente cliente) {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();
        LocalDate fechaCumpleaños = dpCumpleaños.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }
        Cliente clienteActualizado = new Cliente(cliente.getId(), nombre, apellido, direccion, fechaCumpleaños);
        try {
            ClienteDAO.modificarCliente(clienteActualizado);
            logger.info("Cliente modificado: ID " + cliente.getId());
            limpiarCampos();
            listarClientes();
            // Restaurar el botón "Agregar"
            btnAgregar.setText("Agregar");
            btnAgregar.setOnAction(e -> agregarCliente());
        } catch (SQLException ex) {
            logger.severe("Error al modificar cliente: " + ex.getMessage());
            mostrarAlerta("Error", "No se pudo modificar el cliente: " + ex.getMessage());
        }
    }

    // Método para eliminar un cliente
    public void eliminarCliente(Cliente cliente) {
        try {
            ClienteDAO.eliminarCliente(cliente.getId());
            logger.info("Cliente eliminado: ID " + cliente.getId());
            listarClientes();
        } catch (SQLException ex) {
            logger.severe("Error al eliminar cliente: " + ex.getMessage());
            mostrarAlerta("Error", "No se pudo eliminar el cliente: " + ex.getMessage());
        }
    }

    // Carga los datos de un cliente en el formulario para editar
    public void cargarClienteEnFormulario(Cliente cliente) {
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDireccion.setText(cliente.getDireccion());
        dpCumpleaños.setValue(cliente.getCumpleaños());
        logger.info("Cargando datos del cliente en el formulario: ID " + cliente.getId());

        // Cambiar el botón a "Modificar"
        btnAgregar.setText("Modificar");
        btnAgregar.setOnAction(e -> modificarCliente(cliente));
    }

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtDireccion.clear();
        dpCumpleaños.setValue(null);
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
