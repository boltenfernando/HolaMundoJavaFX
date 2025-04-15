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
import java.time.format.DateTimeFormatter;
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

        BorderPane root = new BorderPane();
        root.setLeft(formView.getView());
        root.setCenter(tableClientes);
        root.setTop(panelRecordatorios);
        HBox botonesCsv = new HBox(10, btnExportarCsv, btnImportarCsv);
        botonesCsv.setPadding(new Insets(10));
        root.setBottom(botonesCsv);
        root.setPadding(new Insets(10));

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

        TableColumn<Cliente, Void> colVer = new TableColumn<>("VER");
        colVer.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("VER");
            {
                btn.setOnAction(e ->
                    ClienteController.this.mostrarDetalle(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEditar = new TableColumn<>("EDITAR");
        colEditar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("EDITAR");
            {
                btn.setOnAction(e ->
                    ClienteController.this.mostrarFormularioModal(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, Void> colEliminar = new TableColumn<>("ELIMINAR");
        colEliminar.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("ELIMINAR");
            {
                btn.setOnAction(e ->
                    ClienteController.this.eliminarConfirmacion(getTableView().getItems().get(getIndex()))
                );
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<Cliente, String> colRec = new TableColumn<>("Recordatorios");
        colRec.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(String.join(", ", RecordatorioService.getRecordatorios(c.getValue())))
        );

        tableClientes.getColumns().addAll(
            colCat, colNom, colApe, colRef,
            colVer, colEditar, colEliminar, colRec
        );
    }

    private void configurarRecordatorios() {
        panelRecordatorios = RecordatorioService.crearPanelRecordatoriosPequeno();
        Button btnLupa = new Button("🔍");
        btnLupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());
        panelRecordatorios.getChildren().add(btnLupa);
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
                pw.println("id,nombre,apellido,referencia,proximoContacto,direccion,localidad,cumpleaños," +
                           "datosPersonales,datosLaborales,datosVenta,datosCompra,deseaContacto,fueCliente," +
                           "fechaCompraVenta,esReferidor,refirioA,referidoPor,esPadre,esMadre,nombreHijos," +
                           "telefono,redesSociales,email,ocupacion,gustosMusicales,clubFutbol,gustoBebidas," +
                           "preferenciasComida,categoria");
                for (Cliente c : lista) {
                    pw.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%b,%b,%s,%b,%s,%s,%b,%b,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        c.getId(), c.getNombre(), c.getApellido(), c.getReferencia(),
                        c.getProximoContacto(), c.getDireccion(), c.getLocalidad(),
                        c.getCumpleaños(), c.getDatosPersonales(), c.getDatosLaborales(),
                        c.getDatosVenta(), c.getDatosCompra(), c.isDeseaContacto(), c.isFueCliente(),
                        c.getFechaCompraVenta(), c.isEsReferidor(), c.getRefirioA(), c.getReferidoPor(),
                        c.isEsPadre(), c.isEsMadre(), c.getNombreHijos(), c.getTelefono(),
                        c.getRedesSociales(), c.getEmail(), c.getOcupacion(),
                        c.getGustosMusicales(), c.getClubFutbol(), c.getGustoBebidas(),
                        c.getPreferenciasComida(), c.getCategoria());
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
                c.setProximoContacto(!cols[4].isEmpty() ? LocalDate.parse(cols[4]) : null);
                c.setDireccion(cols[5]);
                c.setLocalidad(cols[6]);
                c.setCumpleaños(!cols[7].isEmpty() ? LocalDate.parse(cols[7]) : null);
                c.setDatosPersonales(cols[8]);
                c.setDatosLaborales(cols[9]);
                c.setDatosVenta(cols[10]);
                c.setDatosCompra(cols[11]);
                c.setDeseaContacto(Boolean.parseBoolean(cols[12]));
                c.setFueCliente(Boolean.parseBoolean(cols[13]));
                c.setFechaCompraVenta(!cols[14].isEmpty() ? LocalDate.parse(cols[14]) : null);
                c.setEsReferidor(Boolean.parseBoolean(cols[15]));
                c.setRefirioA(cols[16]);
                c.setReferidoPor(cols[17]);
                c.setEsPadre(Boolean.parseBoolean(cols[18]));
                c.setEsMadre(Boolean.parseBoolean(cols[19]));
                c.setNombreHijos(cols[20]);
                c.setTelefono(cols[21]);
                c.setRedesSociales(cols[22]);
                c.setEmail(cols[23]);
                c.setOcupacion(cols[24]);
                c.setGustosMusicales(cols[25]);
                c.setClubFutbol(cols[26]);
                c.setGustoBebidas(cols[27]);
                c.setPreferenciasComida(cols[28]);
                c.setCategoria(cols[29]);
                ClienteDAO.guardarOActualizar(c);
            }
        } catch (Exception ex) {
            ErrorHandler.showError("Error importando CSV", ex.getMessage());
        }
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

    public void mostrarFormularioModal(Cliente cliente) {
        VBox panel = formView.getView();
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");
        formView.precargar(cliente);
        formView.getBtnGuardar().setOnAction(e -> {
            boolean ok = formView.isNew() ? formView.guardar() : formView.actualizar();
            if (ok) {
                listarClientes();
                dialog.close();
            }
        });
        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(true);
        dialog.setScene(new Scene(scroll, 450, 700));
        dialog.showAndWait();
    }

    public void mostrarDetalle(Cliente cliente) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Detalle Cliente");
        VBox v = new VBox(5);
        v.setPadding(new Insets(10));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var addField = (java.util.function.BiConsumer<String, String>) (label, value) -> {
            Label lbl = new Label(label);
            lbl.setStyle("-fx-font-weight:bold");
            Label val = new Label(value);
            v.getChildren().add(new HBox(5, lbl, val));
        };
        if (cliente.getCategoria()!=null) addField.accept("Categoría:", cliente.getCategoria());
        if (cliente.getNombre()!=null) addField.accept("Nombre:", cliente.getNombre());
        if (cliente.getApellido()!=null) addField.accept("Apellido:", cliente.getApellido());
        if (cliente.getReferencia()!=null&&!cliente.getReferencia().isEmpty()) addField.accept("Referencia:", cliente.getReferencia());
        if (cliente.getProximoContacto()!=null) addField.accept("Próximo contacto:", cliente.getProximoContacto().format(fmt));
        if (cliente.getDireccion()!=null&&!cliente.getDireccion().isEmpty()) addField.accept("Dirección:", cliente.getDireccion());
        if (cliente.getLocalidad()!=null&&!cliente.getLocalidad().isEmpty()) addField.accept("Localidad:", cliente.getLocalidad());
        if (cliente.getCumpleaños()!=null) addField.accept("Cumpleaños:", cliente.getCumpleaños().format(fmt));
        if (cliente.getDatosPersonales()!=null&&!cliente.getDatosPersonales().isEmpty()) addField.accept("Datos Personales:", cliente.getDatosPersonales());
        if (cliente.getDatosLaborales()!=null&&!cliente.getDatosLaborales().isEmpty()) addField.accept("Datos Laborales:", cliente.getDatosLaborales());
        if (cliente.getDatosVenta()!=null&&!cliente.getDatosVenta().isEmpty()) addField.accept("Datos de Venta:", cliente.getDatosVenta());
        if (cliente.getDatosCompra()!=null&&!cliente.getDatosCompra().isEmpty()) addField.accept("Datos de Compra:", cliente.getDatosCompra());
        if (cliente.isDeseaContacto()) addField.accept("Desea contacto:", "Sí");
        if (cliente.isFueCliente()) addField.accept("Es cliente:", "Sí");
        if (cliente.getFechaCompraVenta()!=null) addField.accept("Fecha compra/venta:", cliente.getFechaCompraVenta().format(fmt));
        if (cliente.isEsReferidor()) addField.accept("Es referidor:", "Sí");
        if (cliente.getRefirioA()!=null&&!cliente.getRefirioA().isEmpty()) addField.accept("Refirió a:", cliente.getRefirioA());
        if (cliente.getReferidoPor()!=null&&!cliente.getReferidoPor().isEmpty()) addField.accept("Fue referido por:", cliente.getReferidoPor());
        if (cliente.isEsPadre()) addField.accept("Es padre:", "Sí");
        if (cliente.isEsMadre()) addField.accept("Es madre:", "Sí");
        if (cliente.getNombreHijos()!=null&&!cliente.getNombreHijos().isEmpty()) addField.accept("Nombre de los hijos:", cliente.getNombreHijos());
        if (cliente.getTelefono()!=null&&!cliente.getTelefono().isEmpty()) addField.accept("Teléfono:", cliente.getTelefono());
        if (cliente.getRedesSociales()!=null&&!cliente.getRedesSociales().isEmpty()) addField.accept("Redes sociales:", cliente.getRedesSociales());
        if (cliente.getEmail()!=null&&!cliente.getEmail().isEmpty()) addField.accept("Email:", cliente.getEmail());
        if (cliente.getOcupacion()!=null&&!cliente.getOcupacion().isEmpty()) addField.accept("Ocupación:", cliente.getOcupacion());
        if (cliente.getGustosMusicales()!=null&&!cliente.getGustosMusicales().isEmpty()) addField.accept("Gustos musicales:", cliente.getGustosMusicales());
        if (cliente.getClubFutbol()!=null&&!cliente.getClubFutbol().isEmpty()) addField.accept("Club de fútbol:", cliente.getClubFutbol());
        if (cliente.getGustoBebidas()!=null&&!cliente.getGustoBebidas().isEmpty()) addField.accept("Gusto de bebidas:", cliente.getGustoBebidas());
        if (cliente.getPreferenciasComida()!=null&&!cliente.getPreferenciasComida().isEmpty()) addField.accept("Preferencias de comida:", cliente.getPreferenciasComida());

        ScrollPane scroll = new ScrollPane(v);
        scroll.setFitToWidth(true);
        s.setScene(new Scene(scroll, 400, 600));
        s.showAndWait();
    }

    /**
     * Confirma con el usuario y elimina el cliente de la base de datos.
     */
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

    
    public Button getBtnExportarCsv() {
        return btnExportarCsv;
    }

    public Button getBtnImportarCsv() {
        return btnImportarCsv;
    }
}
