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

import java.sql.SQLException;
import java.time.LocalDate;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private TableView<Cliente> tableClientes;
    private VBox panelRecordatorios;

    public ClienteController(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
        configurarTabla();
        configurarRecordatorios();
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

        // Columna VER
        TableColumn<Cliente, Void> colVer = new TableColumn<>("VER");
        colVer.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("VER");
            {
                btn.setOnAction(e -> mostrarDetalle(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Columna EDITAR
        TableColumn<Cliente, Void> colEditar = new TableColumn<>("EDITAR");
        colEditar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("EDITAR");
            {
                btn.setOnAction(e -> cargarParaEdicion(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Columna ELIMINAR
        TableColumn<Cliente, Void> colEliminar = new TableColumn<>("ELIMINAR");
        colEliminar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("ELIMINAR");
            {
                btn.setOnAction(e -> eliminarConfirmacion(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
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

        // Aquí puedes construir el formulario con ScrollPane si lo deseas
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        // Agrega tus controles de formulario (TextField, DatePicker, CheckBox, etc.) y un botón para guardar
        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Scene scene = new Scene(scroll, 400, 600);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void mostrarDetalle(Cliente cliente) {
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

    private void cargarParaEdicion(Cliente cliente) {
        // Implementa la lógica para cargar los datos en el formulario modal
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
}
