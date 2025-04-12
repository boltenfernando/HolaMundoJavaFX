package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class FormularioClienteView {
    public final ComboBox<String> cmbCategoria = new ComboBox<>();
    public final TextField txtNombre = new TextField();
    public final TextField txtApellido = new TextField();
    public final TextField txtReferencia = new TextField();
    public final DatePicker dpProximoContacto = new DatePicker();
    public final TextField txtDireccion = new TextField();
    public final TextField txtLocalidad = new TextField();
    public final DatePicker dpCumpleaños = new DatePicker();
    public final TextArea taDatosPersonales = new TextArea();
    public final TextArea taDatosLaborales = new TextArea();
    public final TextArea taDatosVenta = new TextArea();
    public final TextArea taDatosCompra = new TextArea();
    public final CheckBox cbDeseaContacto = new CheckBox("Quiero tener contacto");
    public final CheckBox cbFueCliente = new CheckBox("Es cliente");
    public final DatePicker dpFechaCompraVenta = new DatePicker();
    public final CheckBox cbEsReferidor = new CheckBox("Es referidor?");
    public final TextField txtRefirioA = new TextField();
    public final TextField txtReferidoPor = new TextField();
    public final CheckBox cbEsPadre = new CheckBox("Es padre");
    public final CheckBox cbEsMadre = new CheckBox("Es madre");
    public final TextField txtHijos = new TextField();
    public final TextField txtTelefono = new TextField();
    public final TextField txtRedes = new TextField();
    public final TextField txtEmail = new TextField();
    public final ComboBox<String> cmbOcupacion = new ComboBox<>();
    public final TextField txtGustosMusicales = new TextField();
    public final TextField txtClubFutbol = new TextField();
    public final TextField txtBebidas = new TextField();
    public final TextField txtComida = new TextField();
    public final Button btnGuardar = new Button();

    private static final List<String> CATEGORIAS = Arrays.asList("A+", "A", "B", "C", "D");
    private static final List<String> OCUPACIONES = Arrays.asList("Abogado", "Contador", "Médico", "Escribano");

    public FormularioClienteView() {
        cmbCategoria.getItems().addAll(CATEGORIAS);
        cmbCategoria.setPromptText("Categoría");
        txtNombre.setPromptText("Nombre");
        txtApellido.setPromptText("Apellido");
        txtReferencia.setPromptText("Referencia");
        txtDireccion.setPromptText("Dirección");
        txtLocalidad.setPromptText("Localidad");
        taDatosPersonales.setPromptText("Datos Personales / Familiares");
        taDatosLaborales.setPromptText("Datos Laborales");
        taDatosVenta.setPromptText("Datos de Venta");
        taDatosCompra.setPromptText("Datos de Compra");
        txtRefirioA.setPromptText("Refirió a");
        txtReferidoPor.setPromptText("Fue referido por");
        txtHijos.setPromptText("Nombre de los hijos");
        txtTelefono.setPromptText("Teléfono");
        txtRedes.setPromptText("Redes sociales");
        txtEmail.setPromptText("Email");
        cmbOcupacion.getItems().addAll(OCUPACIONES);
        cmbOcupacion.setPromptText("Ocupación");
        txtGustosMusicales.setPromptText("Gustos musicales");
        txtClubFutbol.setPromptText("Club de fútbol");
        txtBebidas.setPromptText("Gusto de bebidas");
        txtComida.setPromptText("Preferencias de comida");
        btnGuardar.setText("Guardar");
    }

    public VBox getView() {
        VBox v = new VBox(8,
            new Label("Categoría:"), cmbCategoria,
            new Label("Nombre:"), txtNombre,
            new Label("Apellido:"), txtApellido,
            new Label("Referencia:"), txtReferencia,
            new Label("Próximo contacto:"), dpProximoContacto,
            new Separator(),
            new Label("Dirección:"), txtDireccion,
            new Label("Localidad:"), txtLocalidad,
            new Label("Fecha cumpleaños:"), dpCumpleaños,
            new Separator(),
            new Label("Datos Personales / Familiares:"), taDatosPersonales,
            new Label("Datos Laborales:"), taDatosLaborales,
            new Label("Datos de Venta:"), taDatosVenta,
            new Label("Datos de Compra:"), taDatosCompra,
            new Separator(),
            cbDeseaContacto, cbFueCliente,
            new Label("Fecha compra/venta (opcional):"), dpFechaCompraVenta,
            new Separator(),
            cbEsReferidor,
            new Label("Refirió a:"), txtRefirioA,
            new Label("Fue referido por:"), txtReferidoPor,
            new Separator(),
            cbEsPadre, cbEsMadre,
            new Label("Nombre de los hijos:"), txtHijos,
            new Separator(),
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
        v.setPadding(new Insets(10));
        return v;
    }
}
