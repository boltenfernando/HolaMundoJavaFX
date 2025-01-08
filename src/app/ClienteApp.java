package app;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.TestSQLite;

public class ClienteApp extends Application {

	private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private final TextField txtNombre = new TextField();
    private final TextField txtApellido = new TextField();
    private final TextField txtDireccion = new TextField();
    private TableView<Cliente> tableClientes;

    @Override
    public void start(Stage primaryStage) {
    	
    	TestSQLite.createTable(); //Asegurar que la tabla 'clientes' existe

        // Configuración de campos de texto ll
        txtNombre.setPromptText("Nombre Modificado 3");
        txtApellido.setPromptText("Apellido");
        txtDireccion.setPromptText("Dirección");

        // Botón para agregar cliente
        //PROBANDO
        //Button btnAgregar = new Button("Agregar");
        //btnAgregar.setOnAction(e -> agregarCliente());

        // Botón para listar clientes
        Button btnListar = new Button("Listar");
        btnListar.setOnAction(e -> listarClientes());

        // Tabla para mostrar clientes
        tableClientes = new TableView<>();
        configurarTabla();

        // Diseño de la interfaz
        HBox formulario = new HBox(10, txtNombre, txtApellido, txtDireccion/*, btnAgregar*/, btnListar);
        VBox root = new VBox(10, formulario, tableClientes);
        Scene scene = new Scene(root, 800, 400);

        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarTabla() {
        // Columnas existentes
        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Columna de Modificar
        TableColumn<Cliente, Void> colModificar = new TableColumn<>("Modificar");
        colModificar.setCellFactory(tc -> new TableCell<>() {
            private final Button btnModificar = new Button("Modificar");

            {
                btnModificar.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    cargarClienteEnFormulario(cliente); // Cargar los datos del cliente en los campos de texto
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnModificar);
                }
            }
        });

        // Columna de Eliminar
        TableColumn<Cliente, Void> colEliminar = new TableColumn<>("Eliminar");
        colEliminar.setCellFactory(tc -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    eliminarCliente(cliente); // Eliminar cliente de la base de datos
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });

        // Agregar todas las columnas a la tabla
        tableClientes.getColumns().addAll(colId, colNombre, colApellido, colDireccion, colModificar, colEliminar);
    }

    private void agregarCliente() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        String sql = "INSERT INTO clientes(nombre, apellido, direccion) VALUES(?, ?, ?)";

        try (Connection conn = TestSQLite.connect(); // Reutilizamos la conexión de TestSQLite
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, direccion);
            pstmt.executeUpdate();
            txtNombre.clear();
            txtApellido.clear();
            txtDireccion.clear();
            listarClientes(); // Actualizar la tabla después de agregar
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo agregar el cliente: " + e.getMessage());
        }
    }

    private void listarClientes() {
        clientes.clear();
        String sql = "SELECT id, nombre, apellido, direccion FROM clientes";

        try (Connection conn = TestSQLite.connect(); // Reutilizamos la conexión de TestSQLite
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("direccion")
                ));
            }
            tableClientes.setItems(clientes);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo listar los clientes: " + e.getMessage());
        }
    }
    
    private void cargarClienteEnFormulario(Cliente cliente) {
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDireccion.setText(cliente.getDireccion());

        // Cambiar el botón a "Modificar"
        Button btnAgregar = (Button) ((HBox) txtNombre.getParent()).getChildren().get(3);
        btnAgregar.setText("Modificar");
        btnAgregar.setOnAction(e -> modificarCliente(cliente));
    }
    
    private void modificarCliente(Cliente cliente) {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, direccion = ? WHERE id = ?";

        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, direccion);
            pstmt.setInt(4, cliente.getId());
            pstmt.executeUpdate();

            txtNombre.clear();
            txtApellido.clear();
            txtDireccion.clear();
            listarClientes(); // Actualizar la tabla después de modificar

            // Restaurar el botón "Agregar"
            Button btnAgregar = (Button) ((HBox) txtNombre.getParent()).getChildren().get(3);
            btnAgregar.setText("Agregar");
            btnAgregar.setOnAction(e -> agregarCliente());
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo modificar el cliente: " + e.getMessage());
        }
    }    
    private void eliminarCliente(Cliente cliente) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cliente.getId());
            pstmt.executeUpdate();
            listarClientes(); // Actualizar la tabla después de eliminar
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo eliminar el cliente: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}