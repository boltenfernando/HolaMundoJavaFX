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
import view.FormularioClienteView;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class ClienteController {
    private final ObservableList<Cliente> clientes;
    private TableView<Cliente> tableClientes;
    private VBox panelRecordatorios;
    private final FormularioClienteView formView;

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
        formView = new FormularioClienteView();
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
            List<Cliente> list = ClienteDAO.listarClientes();
            clientes.addAll(list);
            tableClientes.setItems(clientes);
            System.out.println("DEBUG: clientes cargados = " + list.size());
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", ex.getMessage());
        }
    }

    /**
     * Filtra la lista de clientes según categoría, nombre y apellido.
     */
    public void aplicarFiltros(String categoria, String nombre, String apellido) {
        tableClientes.setItems(clientes.filtered(c ->
            (categoria == null || categoria.isEmpty() || c.getCategoria().equals(categoria)) &&
            (nombre == null || nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
            (apellido == null || apellido.isEmpty() || c.getApellido().toLowerCase().contains(apellido.toLowerCase()))
        ));
    }

    public void mostrarFormularioModal(Cliente cliente) {
        System.out.println("DEBUG: abrir formulario modal");
        VBox panelFormulario = formView.getView();

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");

        if (cliente != null) {
            // Precarga campos
            formView.cmbCategoria.setValue(cliente.getCategoria());
            formView.txtNombre.setText(cliente.getNombre());
            formView.txtApellido.setText(cliente.getApellido());
            formView.txtReferencia.setText(cliente.getReferencia());
            formView.dpProximoContacto.setValue(cliente.getProximoContacto());
            formView.txtDireccion.setText(cliente.getDireccion());
            formView.txtLocalidad.setText(cliente.getLocalidad());
            formView.dpCumpleaños.setValue(cliente.getCumpleaños());
            formView.taDatosPersonales.setText(cliente.getDatosPersonales());
            formView.taDatosLaborales.setText(cliente.getDatosLaborales());
            formView.taDatosVenta.setText(cliente.getDatosVenta());
            formView.taDatosCompra.setText(cliente.getDatosCompra());
            formView.cbDeseaContacto.setSelected(cliente.isDeseaContacto());
            formView.cbFueCliente.setSelected(cliente.isFueCliente());
            formView.dpFechaCompraVenta.setValue(cliente.getFechaCompraVenta());
            formView.cbEsReferidor.setSelected(cliente.isEsReferidor());
            formView.txtRefirioA.setText(cliente.getRefirioA());
            formView.txtReferidoPor.setText(cliente.getReferidoPor());
            formView.cbEsPadre.setSelected(cliente.isEsPadre());
            formView.cbEsMadre.setSelected(cliente.isEsMadre());
            formView.txtHijos.setText(cliente.getNombreHijos());
            formView.txtTelefono.setText(cliente.getTelefono());
            formView.txtRedes.setText(cliente.getRedesSociales());
            formView.txtEmail.setText(cliente.getEmail());
            formView.cmbOcupacion.setValue(cliente.getOcupacion());
            formView.txtGustosMusicales.setText(cliente.getGustosMusicales());
            formView.txtClubFutbol.setText(cliente.getClubFutbol());
            formView.txtBebidas.setText(cliente.getGustoBebidas());
            formView.txtComida.setText(cliente.getPreferenciasComida());
            formView.btnGuardar.setText("Actualizar");
        } else {
            formView.btnGuardar.setText("Guardar");
            limpiarCampos();
        }

        formView.btnGuardar.setOnAction(e -> {
            boolean ok = (cliente == null) ? guardarCliente() : actualizarCliente(cliente);
            if (ok) {
                Alert info = new Alert(Alert.AlertType.INFORMATION,
                    cliente == null ? "Cliente creado." : "Cliente actualizado.");
                info.showAndWait();
                dialog.close();
            }
        });

        ScrollPane scroll = new ScrollPane(panelFormulario);
        scroll.setFitToWidth(true);

        dialog.setScene(new Scene(scroll, 450, 700));
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
        String categoria = formView.cmbCategoria.getValue();
        String nombre = formView.txtNombre.getText();
        String apellido = formView.txtApellido.getText();
        if (categoria == null || nombre.isEmpty() || apellido.isEmpty()) {
            ErrorHandler.showError("Error", "Categoría, Nombre y Apellido son obligatorios.");
            return null;
        }
        Cliente c = new Cliente();
        c.setId(id);
        c.setCategoria(categoria);
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setReferencia(formView.txtReferencia.getText());
        c.setProximoContacto(formView.dpProximoContacto.getValue());
        c.setDireccion(formView.txtDireccion.getText());
        c.setLocalidad(formView.txtLocalidad.getText());
        c.setCumpleaños(formView.dpCumpleaños.getValue());
        c.setDatosPersonales(formView.taDatosPersonales.getText());
        c.setDatosLaborales(formView.taDatosLaborales.getText());
        c.setDatosVenta(formView.taDatosVenta.getText());
        c.setDatosCompra(formView.taDatosCompra.getText());
        c.setDeseaContacto(formView.cbDeseaContacto.isSelected());
        c.setFueCliente(formView.cbFueCliente.isSelected());
        c.setFechaCompraVenta(formView.dpFechaCompraVenta.getValue());
        c.setEsReferidor(formView.cbEsReferidor.isSelected());
        c.setRefirioA(formView.txtRefirioA.getText());
        c.setReferidoPor(formView.txtReferidoPor.getText());
        c.setEsPadre(formView.cbEsPadre.isSelected());
        c.setEsMadre(formView.cbEsMadre.isSelected());
        c.setNombreHijos(formView.txtHijos.getText());
        c.setTelefono(formView.txtTelefono.getText());
        c.setRedesSociales(formView.txtRedes.getText());
        c.setEmail(formView.txtEmail.getText());
        c.setOcupacion(formView.cmbOcupacion.getValue());
        c.setGustosMusicales(formView.txtGustosMusicales.getText());
        c.setClubFutbol(formView.txtClubFutbol.getText());
        c.setGustoBebidas(formView.txtBebidas.getText());
        c.setPreferenciasComida(formView.txtComida.getText());
        return c;
    }

    public void limpiarCampos() {
        formView.cmbCategoria.setValue(null);
        formView.txtNombre.clear();
        formView.txtApellido.clear();
        formView.txtReferencia.clear();
        formView.dpProximoContacto.setValue(null);
        formView.txtDireccion.clear();
        formView.txtLocalidad.clear();
        formView.dpCumpleaños.setValue(null);
        formView.taDatosPersonales.clear();
        formView.taDatosLaborales.clear();
        formView.taDatosVenta.clear();
        formView.taDatosCompra.clear();
        formView.cbDeseaContacto.setSelected(false);
        formView.cbFueCliente.setSelected(false);
        formView.dpFechaCompraVenta.setValue(null);
        formView.cbEsReferidor.setSelected(false);
        formView.txtRefirioA.clear();
        formView.txtReferidoPor.clear();
        formView.cbEsPadre.setSelected(false);
        formView.cbEsMadre.setSelected(false);
        formView.txtHijos.clear();
        formView.txtTelefono.clear();
        formView.txtRedes.clear();
        formView.txtEmail.clear();
        formView.cmbOcupacion.setValue(null);
        formView.txtGustosMusicales.clear();
        formView.txtClubFutbol.clear();
        formView.txtBebidas.clear();
        formView.txtComida.clear();
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
