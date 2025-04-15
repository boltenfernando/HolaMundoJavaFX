package controller;

import dao.ClienteDAO;
import javafx.collections.FXCollections;
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
    private final Stage primaryStage;
    private final TableView<Cliente> tableClientes = new TableView<>();
    private final VBox panelRecordatorios = new VBox(10);
    private final Button btnExportarCsv = new Button("Exportar CSV");
    private final Button btnImportarCsv = new Button("Importar CSV");

    private static final Logger logger = LoggerUtil.getLogger();

    public ClienteController(Stage stage, ObservableList<Cliente> clientes) {
        this.primaryStage = stage;
        this.clientes = clientes;

        // Configurar tabla
        configurarTabla();

        // Configurar panel derecho (lupa + CSV)
        configurarRecordatorios();

        // Toolbar
        HBox toolbar = crearToolbar();

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(tableClientes);
        root.setRight(panelRecordatorios);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setTitle("Gestión de Clientes");
        primaryStage.show();

        listarClientes();
    }

    private HBox crearToolbar() {
        Button btnNuevo = new Button("Nuevo Cliente");
        btnNuevo.setOnAction(e -> mostrarFormularioModal(null));

        ChoiceBox<String> cbCat = new ChoiceBox<>(FXCollections.observableArrayList("", "A+","A","B","C","D"));
        cbCat.setValue("");
        TextField tfNom = new TextField(); tfNom.setPromptText("Nombre");
        TextField tfApe = new TextField(); tfApe.setPromptText("Apellido");
        Button btnFil = new Button("Filtrar");
        btnFil.setOnAction(e -> aplicarFiltros(cbCat.getValue(), tfNom.getText(), tfApe.getText()));
        Button btnLim = new Button("Limpiar");
        btnLim.setOnAction(e -> { cbCat.setValue(""); tfNom.clear(); tfApe.clear(); listarClientes(); });

        HBox filtros = new HBox(5, new Label("Cat:"), cbCat, new Label("Nom:"), tfNom, new Label("Ape:"), tfApe, btnFil, btnLim);
        filtros.setPadding(new Insets(10));

        HBox toolbar = new HBox(10, btnNuevo, filtros);
        toolbar.setPadding(new Insets(10));
        return toolbar;
    }

    private void configurarTabla() {
        TableColumn<Cliente,String> colCat = new TableColumn<>("Categoría");
        colCat.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        TableColumn<Cliente,String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Cliente,String> colApe = new TableColumn<>("Apellido");
        colApe.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        TableColumn<Cliente,String> colRef = new TableColumn<>("Referencia");
        colRef.setCellValueFactory(new PropertyValueFactory<>("referencia"));

        TableColumn<Cliente,Void> colVer = new TableColumn<>("VER");
        colVer.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("VER");
            { btn.setOnAction(e -> mostrarDetalle(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty?null:btn);
            }
        });

        TableColumn<Cliente,Void> colEdit = new TableColumn<>("EDITAR");
        colEdit.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("EDITAR");
            { btn.setOnAction(e -> mostrarFormularioModal(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty?null:btn);
            }
        });

        TableColumn<Cliente,Void> colDel = new TableColumn<>("ELIMINAR");
        colDel.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("ELIMINAR");
            { btn.setOnAction(e -> eliminarConfirmacion(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty?null:btn);
            }
        });

        tableClientes.getColumns().setAll(colCat, colNom, colApe, colRef, colVer, colEdit, colDel);
        tableClientes.setItems(clientes);
    }

    private void configurarRecordatorios() {
        panelRecordatorios.getChildren().clear();
        Button btnLupa = new Button("🔍");
        btnLupa.setOnAction(e -> RecordatorioService.mostrarRecordatorios());

        btnExportarCsv.setOnAction(e -> {
            FileChooser c = new FileChooser();
            c.setTitle("Exportar CSV");
            c.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
            File f = c.showSaveDialog(primaryStage);
            if (f!=null) exportarCsv(f);
        });
        btnImportarCsv.setOnAction(e -> {
            FileChooser c = new FileChooser();
            c.setTitle("Importar CSV");
            c.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
            File f = c.showOpenDialog(primaryStage);
            if (f!=null) { importarCsv(f); listarClientes(); }
        });

        panelRecordatorios.getChildren().addAll(btnLupa, btnExportarCsv, btnImportarCsv);
        panelRecordatorios.setPadding(new Insets(10));
    }

    private void exportarCsv(File file) { /* tu lógica */ }
    private void importarCsv(File file) { /* tu lógica */ }

    public void listarClientes() {
        clientes.clear();
        try {
            clientes.addAll(ClienteDAO.listarClientes());
        } catch (SQLException ex) {
            ErrorHandler.showError("Error", ex.getMessage());
        }
    }

    public void aplicarFiltros(String cat, String nom, String ape) {
        tableClientes.setItems(clientes.filtered(c ->
            (cat.isEmpty()||c.getCategoria().equals(cat)) &&
            (nom.isEmpty()||c.getNombre().toLowerCase().contains(nom.toLowerCase())) &&
            (ape.isEmpty()||c.getApellido().toLowerCase().contains(ape.toLowerCase()))
        ));
    }

    public void mostrarFormularioModal(Cliente cliente) {
        FormularioClienteView fv = new FormularioClienteView();
        if (cliente!=null) fv.precargar(cliente);
        Stage d = new Stage(); d.initModality(Modality.APPLICATION_MODAL);
        d.setTitle(cliente==null?"Nuevo Cliente":"Editar Cliente");
        fv.btnGuardar.setOnAction(e -> {
            Cliente c = cliente==null? new Cliente() : cliente;
            // leer campos de fv en c...
            c.setCategoria(fv.cmbCategoria.getValue());
            c.setNombre(fv.txtNombre.getText());
            c.setApellido(fv.txtApellido.getText());
            c.setReferencia(fv.txtReferencia.getText());
            c.setProximoContacto(fv.dpProximoContacto.getValue());
            c.setDireccion(fv.txtDireccion.getText());
            c.setLocalidad(fv.txtLocalidad.getText());
            c.setCumpleaños(fv.dpCumpleaños.getValue());
            c.setDatosPersonales(fv.taDatosPersonales.getText());
            c.setDatosLaborales(fv.taDatosLaborales.getText());
            c.setDatosVenta(fv.taDatosVenta.getText());
            c.setDatosCompra(fv.taDatosCompra.getText());
            c.setDeseaContacto(fv.cbDeseaContacto.isSelected());
            c.setFueCliente(fv.cbFueCliente.isSelected());
            c.setFechaCompraVenta(fv.dpFechaCompraVenta.getValue());
            c.setEsReferidor(fv.cbEsReferidor.isSelected());
            c.setRefirioA(fv.txtRefirioA.getText());
            c.setReferidoPor(fv.txtReferidoPor.getText());
            c.setEsPadre(fv.cbEsPadre.isSelected());
            c.setEsMadre(fv.cbEsMadre.isSelected());
            c.setNombreHijos(fv.txtHijos.getText());
            c.setTelefono(fv.txtTelefono.getText());
            c.setRedesSociales(fv.txtRedes.getText());
            c.setEmail(fv.txtEmail.getText());
            c.setOcupacion(fv.cmbOcupacion.getValue());
            c.setGustosMusicales(fv.txtGustosMusicales.getText());
            c.setClubFutbol(fv.txtClubFutbol.getText());
            c.setGustoBebidas(fv.txtBebidas.getText());
            c.setPreferenciasComida(fv.txtComida.getText());

            try {
                if (cliente==null) ClienteDAO.agregarCliente(c);
                else ClienteDAO.modificarCliente(c);
                listarClientes();
                d.close();
            } catch (SQLException ex) {
                ErrorHandler.showError("Error", ex.getMessage());
            }
        });
        ScrollPane sp = new ScrollPane(fv.getView());
        sp.setFitToWidth(true);
        d.setScene(new Scene(sp,450,700));
        d.showAndWait();
    }

    public void mostrarDetalle(Cliente c) {
        Stage s = new Stage(); s.initModality(Modality.APPLICATION_MODAL); s.setTitle("Detalle");
        VBox v = new VBox(5); v.setPadding(new Insets(10));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var add = (java.util.function.BiConsumer<String,String>) (lab,val)->{
            Label L=new Label(lab);L.setStyle("-fx-font-weight:bold");
            Label V=new Label(val); v.getChildren().add(new HBox(5,L,V));
        };
        if(c.getCategoria()!=null) add.accept("Categoría:",c.getCategoria());
        if(c.getNombre()!=null) add.accept("Nombre:",c.getNombre());
        if(c.getApellido()!=null) add.accept("Apellido:",c.getApellido());
        if(c.getReferencia()!=null&&!c.getReferencia().isEmpty()) add.accept("Referencia:",c.getReferencia());
        if(c.getProximoContacto()!=null) add.accept("Próximo contacto:",c.getProximoContacto().format(fmt));
        if(c.getDireccion()!=null&&!c.getDireccion().isEmpty()) add.accept("Dirección:",c.getDireccion());
        if(c.getLocalidad()!=null&&!c.getLocalidad().isEmpty()) add.accept("Localidad:",c.getLocalidad());
        if(c.getCumpleaños()!=null) add.accept("Cumpleaños:",c.getCumpleaños().format(fmt));
        if(c.getDatosPersonales()!=null&&!c.getDatosPersonales().isEmpty()) add.accept("Datos Personales:",c.getDatosPersonales());
        if(c.getDatosLaborales()!=null&&!c.getDatosLaborales().isEmpty()) add.accept("Datos Laborales:",c.getDatosLaborales());
        if(c.getDatosVenta()!=null&&!c.getDatosVenta().isEmpty()) add.accept("Datos de Venta:",c.getDatosVenta());
        if(c.getDatosCompra()!=null&&!c.getDatosCompra().isEmpty()) add.accept("Datos de Compra:",c.getDatosCompra());
        if(c.isDeseaContacto()) add.accept("Desea contacto:","Sí");
        if(c.isFueCliente()) add.accept("Fue cliente:","Sí");
        if(c.getFechaCompraVenta()!=null) add.accept("Fecha compra/venta:",c.getFechaCompraVenta().format(fmt));
        if(c.isEsReferidor()) add.accept("Es referidor:","Sí");
        if(c.getRefirioA()!=null&&!c.getRefirioA().isEmpty()) add.accept("Refirió a:",c.getRefirioA());
        if(c.getReferidoPor()!=null&&!c.getReferidoPor().isEmpty()) add.accept("Referido por:",c.getReferidoPor());
        if(c.isEsPadre()) add.accept("Es padre:","Sí");
        if(c.isEsMadre()) add.accept("Es madre:","Sí");
        if(c.getNombreHijos()!=null&&!c.getNombreHijos().isEmpty()) add.accept("Hijos:",c.getNombreHijos());
        if(c.getTelefono()!=null&&!c.getTelefono().isEmpty()) add.accept("Teléfono:",c.getTelefono());
        if(c.getRedesSociales()!=null&&!c.getRedesSociales().isEmpty()) add.accept("Redes sociales:",c.getRedesSociales());
        if(c.getEmail()!=null&&!c.getEmail().isEmpty()) add.accept("Email:",c.getEmail());
        if(c.getOcupacion()!=null&&!c.getOcupacion().isEmpty()) add.accept("Ocupación:",c.getOcupacion());
        if(c.getGustosMusicales()!=null&&!c.getGustosMusicales().isEmpty()) add.accept("Gustos musicales:",c.getGustosMusicales());
        if(c.getClubFutbol()!=null&&!c.getClubFutbol().isEmpty()) add.accept("Club fútbol:",c.getClubFutbol());
        if(c.getGustoBebidas()!=null&&!c.getGustoBebidas().isEmpty()) add.accept("Gusto bebidas:",c.getGustoBebidas());
        if(c.getPreferenciasComida()!=null&&!c.getPreferenciasComida().isEmpty()) add.accept("Preferencias comida:",c.getPreferenciasComida());

        ScrollPane sp=new ScrollPane(v); sp.setFitToWidth(true);
        s.setScene(new Scene(sp,400,600)); s.showAndWait();
    }

    public void eliminarConfirmacion(Cliente c) {
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar "+c.getNombre()+" "+c.getApellido()+"?",ButtonType.OK,ButtonType.CANCEL);
        a.showAndWait().ifPresent(b->{
            if(b==ButtonType.OK){
                try{ ClienteDAO.eliminarCliente(c.getId()); listarClientes(); }
                catch(SQLException ex){ ErrorHandler.showError("Error",ex.getMessage()); }
            }
        });
    }
}
