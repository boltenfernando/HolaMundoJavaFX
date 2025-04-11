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
        System.out.println("DEBUG: abrir formulario modal");
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");

        if (cliente != null) {
            // precarga...
        } else {
            limpiarCampos();
        }

        btnGuardar.setText(cliente == null ? "Guardar" : "Actualizar");
        btnGuardar.setOnAction(e -> {
            boolean ok = cliente == null ? guardarCliente() : actualizarCliente(cliente);
            if (ok) dialog.close();
        });

        ScrollPane scroll = new ScrollPane(panelFormulario);
        scroll.setFitToWidth(true);

        Scene scene = new Scene(scroll, 450, 700);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

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
        Cliente c = new Cliente();
        c.setId(id);
        c.setCategoria(categoria);
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setReferencia(txtReferencia.getText());
        c.setProximoContacto(dpProximoContacto.getValue());
        c.setDireccion(txtDireccion.getText());
        c.setLocalidad(txtLocalidad.getText());
        c.setCumpleaños(dpCumpleaños.getValue());
        c.setDatosPersonales(taDatosPersonales.getText());
        c.setDatosLaborales(taDatosLaborales.getText());
        c.setDatosVenta(taDatosVenta.getText());
        c.setDatosCompra(taDatosCompra.getText());
        c.setDeseaContacto(cbDeseaContacto.isSelected());
        c.setFueCliente(cbFueCliente.isSelected());
        c.setFechaCompraVenta(dpFechaCompraVenta.getValue());
        c.setEsReferidor(cbEsReferidor.isSelected());
        c.setRefirioA(txtRefirioA.getText());
        c.setReferidoPor(txtReferidoPor.getText());
        c.setEsPadre(cbEsPadre.isSelected());
        c.setEsMadre(cbEsMadre.isSelected());
        c.setNombreHijos(txtHijos.getText());
        c.setTelefono(txtTelefono.getText());
        c.setRedesSociales(txtRedes.getText());
        c.setEmail(txtEmail.getText());
        c.setOcupacion(cmbOcupacion.getValue());
        c.setGustosMusicales(txtGustosMusicales.getText());
        c.setClubFutbol(txtClubFutbol.getText());
        c.setGustoBebidas(txtBebidas.getText());
        c.setPreferenciasComida(txtComida.getText());
        return c;
    }

    public void limpiarCampos() {
        cmbCategoria.setValue(null);
        txtNombre.clear();
        txtApellido.clear();
        txtReferencia.clear();
        dpProximoContacto.setValue(null);
        txtDireccion.clear();
        txtLocalidad.clear();
        dpCumpleaños.setValue(null);
        taDatosPersonales.clear();
        taDatosLaborales.clear();
        taDatosVenta.clear();
        taDatosCompra.clear();
        cbDeseaContacto.setSelected(false);
        cbFueCliente.setSelected(false);
        dpFechaCompraVenta.setValue(null);
        cbEsReferidor.setSelected(false);
        txtRefirioA.clear();
        txtReferidoPor.clear();
        cbEsPadre.setSelected(false);
        cbEsMadre.setSelected(false);
        txtHijos.clear();
        txtTelefono.clear();
        txtRedes.clear();
        txtEmail.clear();
        cmbOcupacion.setValue(null);
        txtGustosMusicales.clear();
        txtClubFutbol.clear();
        txtBebidas.clear();
        txtComida.clear();
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
        Scene sc = new Scene(scroll, 400, 600);
        s.setScene(sc);
        s.showAndWait();
    }
}
