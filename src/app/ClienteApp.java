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
import java.time.LocalDate;

import db.TestSQLite;

public class ClienteApp extends Application {

	private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
	private final TextField txtNombre = new TextField();
	private final TextField txtApellido = new TextField();
	private final TextField txtDireccion = new TextField();
	private final DatePicker dpCumpleaños = new DatePicker(); // Campo para la fecha de cumpleaños
    private TableView<Cliente> tableClientes;

    @Override
    public void start(Stage primaryStage) {
    	
    	TestSQLite.createTable(); //Asegurar que la tabla 'clientes' existe
        TestSQLite.verificarYAgregarColumnaCumpleaños(); // Verificar y agregar la columna 'cumpleaños' si falta


        // Mostrar recordatorios al iniciar la app
        mostrarRecordatorios();
        
        // Configuración de campos de texto ll
        txtNombre.setPromptText("modificado por ChatGPT PROBANDO SI LEE 3 ");
        txtApellido.setPromptText("Apellido");
        txtDireccion.setPromptText("Dirección");

        // Botón para agregar cliente
        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> agregarCliente());

        // Botón para listar clientes
        Button btnListar = new Button("Listar");
        btnListar.setOnAction(e -> listarClientes());

        // Tabla para mostrar clientes
        tableClientes = new TableView<>();
        configurarTabla();

        // Diseño de la interfaz
        HBox formulario = new HBox(10, txtNombre, txtApellido, txtDireccion, dpCumpleaños, btnAgregar, btnListar);
        VBox root = new VBox(10, formulario, tableClientes);
        Scene scene = new Scene(root, 800, 400);

        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void mostrarRecordatorios() {
        String sql = "SELECT nombre, apellido, cumpleaños FROM clientes WHERE strftime('%m-%d', cumpleaños) = strftime('%m-%d', 'now')";

        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            StringBuilder recordatorios = new StringBuilder();
            LocalDate hoy = LocalDate.now();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                LocalDate cumpleaños = rs.getObject("cumpleaños", LocalDate.class);

                if (cumpleaños != null) {
                    int edad = hoy.getYear() - cumpleaños.getYear();
                    recordatorios.append(String.format("Hoy es el cumpleaños de %s %s (Cumple %d años).\n", nombre, apellido, edad));
                } else {
                    recordatorios.append(String.format("Hoy es el cumpleaños de %s %s.\n", nombre, apellido));
                }
            }

            if (recordatorios.length() > 0) {
                mostrarAlerta("Recordatorios de Cumpleaños", recordatorios.toString());
            } else {
                mostrarAlerta("Recordatorios de Cumpleaños", "Hoy no hay cumpleaños.");
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error obteniendo recordatorios: " + e.getMessage());
        }
    }

    
   
    private String obtenerRecordatorios() {
        StringBuilder recordatorios = new StringBuilder();
        String sql = "SELECT nombre, apellido FROM clientes WHERE DATE(cumpleaños) = DATE('now')";
        try (Connection conn = TestSQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
            	String cumpleaños = rs.getString("cumpleaños");
            	int edad = -1; // Valor por defecto para cuando no sabemos la edad

            	if (cumpleaños != null && !cumpleaños.startsWith("2000")) { // Si no es la fecha por defecto
            	    int añoNacimiento = Integer.parseInt(cumpleaños.substring(0, 4));
            	    int añoActual = java.time.LocalDate.now().getYear();
            	    edad = añoActual - añoNacimiento;
            	}

            	recordatorios.append("Hoy es el cumpleaños de ")
            	        .append(rs.getString("nombre"))
            	        .append(" ")
            	        .append(rs.getString("apellido"));

            	if (edad != -1) {
            	    recordatorios.append(" (Cumple " + edad + " años)");
            	}

            	recordatorios.append("!\n");

            }
        } catch (SQLException e) {
            System.out.println("Error obteniendo recordatorios: " + e.getMessage());
        }
        return recordatorios.toString();
    }

    
    private void configurarTabla() {
        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Nueva columna: Cumpleaños
        TableColumn<Cliente, String> colCumpleaños = new TableColumn<>("Cumpleaños");
        colCumpleaños.setCellValueFactory(new PropertyValueFactory<>("cumpleaños"));

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
        tableClientes.getColumns().addAll(colId, colNombre, colApellido, colDireccion, colCumpleaños, colModificar, colEliminar);

    }

    private void agregarCliente() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String direccion = txtDireccion.getText();
        LocalDate cumpleaños = dpCumpleaños.getValue();


        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        String sql = "INSERT INTO clientes(nombre, apellido, direccion, cumpleaños) VALUES(?, ?, ?, ?)";


        try (Connection conn = TestSQLite.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setString(1, nombre);
               pstmt.setString(2, apellido);
               pstmt.setString(3, direccion);
               pstmt.setObject(4, cumpleaños); // Inserta la fecha de cumpleaños
               pstmt.executeUpdate();
               txtNombre.clear();
               txtApellido.clear();
               txtDireccion.clear();
               dpCumpleaños.setValue(null); // Limpia el DatePicker
               listarClientes(); // Actualiza la tabla después de agregar
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