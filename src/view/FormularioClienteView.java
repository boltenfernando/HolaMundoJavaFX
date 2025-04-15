package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class FormularioClienteView {
    public ComboBox<String> cmbCategoria = new ComboBox<>();
    public TextField txtNombre = new TextField();
    public TextField txtApellido = new TextField();
    public TextField txtReferencia = new TextField();
    public DatePicker dpProximoContacto = new DatePicker();
    public TextField txtDireccion = new TextField();
    public TextField txtLocalidad = new TextField();
    public DatePicker dpCumpleaños = new DatePicker();
    public TextArea taDatosPersonales = new TextArea();
    public TextArea taDatosLaborales = new TextArea();
    public TextArea taDatosVenta = new TextArea();
    public TextArea taDatosCompra = new TextArea();
    public CheckBox cbDeseaContacto = new CheckBox("Desea Contacto");
    public CheckBox cbFueCliente = new CheckBox("Fue Cliente");
    public DatePicker dpFechaCompraVenta = new DatePicker();
    public CheckBox cbEsReferidor = new CheckBox("Es Referidor");
    public TextField txtRefirioA = new TextField();
    public TextField txtReferidoPor = new TextField();
    public CheckBox cbEsPadre = new CheckBox("Es Padre");
    public CheckBox cbEsMadre = new CheckBox("Es Madre");
    public TextField txtHijos = new TextField();
    public TextField txtTelefono = new TextField();
    public TextField txtRedes = new TextField();
    public TextField txtEmail = new TextField();
    public ComboBox<String> cmbOcupacion = new ComboBox<>();
    public TextField txtGustosMusicales = new TextField();
    public TextField txtClubFutbol = new TextField();
    public TextField txtBebidas = new TextField();
    public TextField txtComida = new TextField();
    public Button btnGuardar = new Button("Guardar");

    private VBox root;

    public FormularioClienteView() {
        cmbCategoria.getItems().addAll("A+","A","B","C","D");
        cmbOcupacion.getItems().addAll("Abogado","Contador","Otro");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        int r=0;
        grid.add(new Label("Categoría:"),0,r); grid.add(cmbCategoria,1,r++);
        grid.add(new Label("Nombre:"),0,r); grid.add(txtNombre,1,r++);
        grid.add(new Label("Apellido:"),0,r); grid.add(txtApellido,1,r++);
        grid.add(new Label("Referencia:"),0,r); grid.add(txtReferencia,1,r++);
        grid.add(new Label("Próx. Contacto:"),0,r); grid.add(dpProximoContacto,1,r++);
        grid.add(new Label("Dirección:"),0,r); grid.add(txtDireccion,1,r++);
        grid.add(new Label("Localidad:"),0,r); grid.add(txtLocalidad,1,r++);
        grid.add(new Label("Cumpleaños:"),0,r); grid.add(dpCumpleaños,1,r++);
        grid.add(new Label("Datos Personales:"),0,r); grid.add(taDatosPersonales,1,r++);
        grid.add(new Label("Datos Laborales:"),0,r); grid.add(taDatosLaborales,1,r++);
        grid.add(new Label("Datos Venta:"),0,r); grid.add(taDatosVenta,1,r++);
        grid.add(new Label("Datos Compra:"),0,r); grid.add(taDatosCompra,1,r++);
        grid.add(cbDeseaContacto,1,r++); grid.add(cbFueCliente,1,r++);
        grid.add(new Label("Fecha Compra/Venta:"),0,r); grid.add(dpFechaCompraVenta,1,r++);
        grid.add(cbEsReferidor,1,r++); grid.add(new Label("Refirió a:"),0,r); grid.add(txtRefirioA,1,r++);
        grid.add(new Label("Referido por:"),0,r); grid.add(txtReferidoPor,1,r++);
        grid.add(cbEsPadre,1,r++); grid.add(cbEsMadre,1,r++);
        grid.add(new Label("Hijos:"),0,r); grid.add(txtHijos,1,r++);
        grid.add(new Label("Teléfono:"),0,r); grid.add(txtTelefono,1,r++);
        grid.add(new Label("Redes:"),0,r); grid.add(txtRedes,1,r++);
        grid.add(new Label("Email:"),0,r); grid.add(txtEmail,1,r++);
        grid.add(new Label("Ocupación:"),0,r); grid.add(cmbOcupacion,1,r++);
        grid.add(new Label("Gustos Mus.:"),0,r); grid.add(txtGustosMusicales,1,r++);
        grid.add(new Label("Club Fútbol:"),0,r); grid.add(txtClubFutbol,1,r++);
        grid.add(new Label("Bebidas:"),0,r); grid.add(txtBebidas,1,r++);
        grid.add(new Label("Comida:"),0,r); grid.add(txtComida,1,r++);
        grid.add(btnGuardar,1,r);

        root = new VBox(grid);
    }

    public VBox getView() {
        return root;
    }

    public void precargar(model.Cliente c) {
        if (c==null) return;
        cmbCategoria.setValue(c.getCategoria());
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtReferencia.setText(c.getReferencia());
        dpProximoContacto.setValue(c.getProximoContacto());
        txtDireccion.setText(c.getDireccion());
        txtLocalidad.setText(c.getLocalidad());
        dpCumpleaños.setValue(c.getCumpleaños());
        taDatosPersonales.setText(c.getDatosPersonales());
        taDatosLaborales.setText(c.getDatosLaborales());
        taDatosVenta.setText(c.getDatosVenta());
        taDatosCompra.setText(c.getDatosCompra());
        cbDeseaContacto.setSelected(c.isDeseaContacto());
        cbFueCliente.setSelected(c.isFueCliente());
        dpFechaCompraVenta.setValue(c.getFechaCompraVenta());
        cbEsReferidor.setSelected(c.isEsReferidor());
        txtRefirioA.setText(c.getRefirioA());
        txtReferidoPor.setText(c.getReferidoPor());
        cbEsPadre.setSelected(c.isEsPadre());
        cbEsMadre.setSelected(c.isEsMadre());
        txtHijos.setText(c.getNombreHijos());
        txtTelefono.setText(c.getTelefono());
        txtRedes.setText(c.getRedesSociales());
        txtEmail.setText(c.getEmail());
        cmbOcupacion.setValue(c.getOcupacion());
        txtGustosMusicales.setText(c.getGustosMusicales());
        txtClubFutbol.setText(c.getClubFutbol());
        txtBebidas.setText(c.getGustoBebidas());
        txtComida.setText(c.getPreferenciasComida());
    }
}
