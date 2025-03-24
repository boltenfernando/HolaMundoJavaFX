package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controller.ClienteController;
import db.TestSQLite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cliente;

public class ClienteApp extends Application {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private TextField txtNombre = new TextField();
    private TextField txtApellido = new TextField();
    private TextField txtDireccion = new TextField();
    private DatePicker dpCumpleaños = new DatePicker();
    private TableView<Cliente> tableClientes = new TableView<>();
    private Button btnAgregar = new Button("Agregar");
    private Button btnListar = new Button("Listar");

    // Declaramos el controlador
    private ClienteController controller;

    @Override
    public void start(Stage primaryStage) {
        // Configurar base de datos
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnaCumpleaños();
        // Mostrar recordatorios al iniciar la app
        mostrarRecordatorios();

        // Configuración de los campos de texto
        txtNombre.setPromptText("Nombre");
        txtApellido.setPromptText("Apellido");
        txtDireccion.setPromptText("Dirección");

        // Configurar la tabla (mantén el método actual o refactorízalo luego)
        configurarTabla();

        // Crear el contenedor del formulario
        HBox formulario = new HBox(10, txtNombre, txtApellido, txtDireccion, dpCumpleaños, btnAgregar, btnListar);

        // Instanciar el controlador pasando las referencias de los controles
        controller = new ClienteController(txtNombre, txtApellido, txtDireccion, dpCumpleaños, btnAgregar, tableClientes, clientes, formulario);

        // Configurar el botón "Listar" para refrescar la tabla
        btnListar.setOnAction(e -> controller.listarClientes());

        VBox root = new VBox(10, formulario, tableClientes);
        Scene scene = new Scene(root, 800, 400);
        
        // Verifica la ruta del CSS
        System.out.println(getClass().getResource("/resources/style.css"));
        // Cargar el archivo CSS en la escena
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());

        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Listar clientes al iniciar la aplicación
        controller.listarClientes();
        controller.listarClientes();
    }

 private void mostrarRecordatorios() {
     LocalDate hoy = LocalDate.now();
     LocalDate semanaFutura = hoy.plusDays(7);

     String sql = "SELECT nombre, apellido, cumpleaños FROM clientes WHERE strftime('%m-%d', cumpleaños) BETWEEN strftime('%m-%d', ?) AND strftime('%m-%d', ?)";
     StringBuilder recordatoriosHoy = new StringBuilder();
     StringBuilder recordatoriosSemana = new StringBuilder();

     try (Connection conn = TestSQLite.connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, hoy.toString());
         pstmt.setString(2, semanaFutura.toString());

         ResultSet rs = pstmt.executeQuery();

         while (rs.next()) {
             String nombre = rs.getString("nombre");
             String apellido = rs.getString("apellido");
             String fechaCumpleañosStr = rs.getString("cumpleaños");
             LocalDate fechaCumpleaños = (fechaCumpleañosStr != null) ? LocalDate.parse(fechaCumpleañosStr) : null;

             if (fechaCumpleaños != null) {
                 if (fechaCumpleaños.equals(hoy)) {
                     recordatoriosHoy.append("🎉 Hoy es el cumpleaños de ")
                         .append(nombre).append(" ").append(apellido).append("!\n");
                 } else {
                     recordatoriosSemana.append("📅 Esta semana cumple ")
                         .append(nombre).append(" ").append(apellido)
                         .append(" el ").append(fechaCumpleaños.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                         .append(".\n");
                 }
             }
         }

         String mensajeFinal = "";
         if (recordatoriosHoy.length() > 0) {
             mensajeFinal += recordatoriosHoy.toString() + "\n";
         }
         if (recordatoriosSemana.length() > 0) {
             mensajeFinal += recordatoriosSemana.toString();
         }
         if (mensajeFinal.isEmpty()) {
             mensajeFinal = "Hoy no hay cumpleaños ni recordatorios pendientes.";
         }

         mostrarAlerta("🎂 Recordatorios de Cumpleaños", mensajeFinal);

     } catch (SQLException e) {
         mostrarAlerta("Error", "Error obteniendo recordatorios: " + e.getMessage());
     }
 }

 private void mostrarAlerta(String titulo, String mensaje) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(titulo);
	    alert.setContentText(mensaje);
	    alert.showAndWait();
	}

 private void configurarTabla() {
	    // Columna de ID
	    TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
	    colId.setCellValueFactory(new PropertyValueFactory<>("id"));

	    // Columna de Nombre
	    TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
	    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

	    // Columna de Apellido
	    TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
	    colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

	    // Columna de Dirección
	    TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
	    colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

	    // Columna de Cumpleaños
	    TableColumn<Cliente, LocalDate> colCumpleaños = new TableColumn<>("Cumpleaños");
	    colCumpleaños.setCellValueFactory(new PropertyValueFactory<>("cumpleaños"));

	    // Columna para Modificar
	    TableColumn<Cliente, Void> colModificar = new TableColumn<>("Modificar");
	    colModificar.setCellFactory(tc -> new TableCell<Cliente, Void>() {
	        private final Button btnModificar = new Button("Modificar");

	        {
	            btnModificar.setOnAction(e -> {
	                Cliente cliente = getTableView().getItems().get(getIndex());
	                // Llama al método del controlador para cargar el cliente en el formulario
	                controller.cargarClienteEnFormulario(cliente);
	            });
	        }

	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            setGraphic(empty ? null : btnModificar);
	        }
	    });

	    // Columna para Eliminar
	    TableColumn<Cliente, Void> colEliminar = new TableColumn<>("Eliminar");
	    colEliminar.setCellFactory(tc -> new TableCell<Cliente, Void>() {
	        private final Button btnEliminar = new Button("Eliminar");

	        {
	            btnEliminar.setOnAction(e -> {
	                Cliente cliente = getTableView().getItems().get(getIndex());
	                // Llama al método del controlador para eliminar el cliente
	                controller.eliminarCliente(cliente);
	            });
	        }

	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            setGraphic(empty ? null : btnEliminar);
	        }
	    });

	    // Configurar las columnas en la tabla
	    tableClientes.getColumns().clear();
	    tableClientes.getColumns().addAll(colId, colNombre, colApellido, colDireccion, colCumpleaños, colModificar, colEliminar);
	}


    public static void main(String[] args) {
        launch(args);
    }
}
