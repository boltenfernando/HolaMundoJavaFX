package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Cliente;

public class FormularioClienteView {

    private VBox panelFormulario;

    public ChoiceBox<String> cmbCategoria;
    public TextField txtNombre;
    public TextField txtApellido;
    public TextField txtReferencia;
    public DatePicker dpProximoContacto;
    public TextField txtDireccion;
    public TextField txtLocalidad;
    public DatePicker dpCumpleaños;
    public TextArea taDatosPersonales;
    public TextArea taDatosLaborales;
    public TextArea taDatosVenta;
    public TextArea taDatosCompra;
    public CheckBox cbDeseaContacto;
    public CheckBox cbFueCliente;
    public DatePicker dpFechaCompraVenta;
    public CheckBox cbEsReferidor;
    public TextField txtRefirioA;
    public TextField txtReferidoPor;
    public CheckBox cbEsPadre;
    public CheckBox cbEsMadre;
    public TextField txtHijos;
    public TextField txtTelefono;
    public TextField txtRedes;
    public TextField txtEmail;
    public ChoiceBox<String> cmbOcupacion;
    public TextField txtGustosMusicales;
    public TextField txtClubFutbol;
    public TextField txtBebidas;
    public TextField txtComida;
    private Button btnGuardar;

    private Button btnExportarCsv;
    private Button btnImportarCsv;

    public FormularioClienteView() {
        panelFormulario = new VBox(10);
        panelFormulario.setPadding(new Insets(10));

        cmbCategoria = new ChoiceBox<>();
        cmbCategoria.getItems().addAll("", "A+", "A", "B", "C", "D");
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");
        txtReferencia = new TextField(); txtReferencia.setPromptText("Referencia");
        dpProximoContacto = new DatePicker();
        txtDireccion = new TextField(); txtDireccion.setPromptText("Dirección");
        txtLocalidad = new TextField(); txtLocalidad.setPromptText("Localidad");
        dpCumpleaños = new DatePicker();
        taDatosPersonales = new TextArea(); taDatosPersonales.setPromptText("Datos personales");
        taDatosLaborales = new TextArea(); taDatosLaborales.setPromptText("Datos laborales");
        taDatosVenta = new TextArea(); taDatosVenta.setPromptText("Datos de venta");
        taDatosCompra = new TextArea(); taDatosCompra.setPromptText("Datos de compra");
        cbDeseaContacto = new CheckBox("Desea contacto");
        cbFueCliente = new CheckBox("Fue cliente");
        dpFechaCompraVenta = new DatePicker();
        cbEsReferidor = new CheckBox("Es referidor");
        txtRefirioA = new TextField(); txtRefirioA.setPromptText("Refirió a");
        txtReferidoPor = new TextField(); txtReferidoPor.setPromptText("Referido por");
        cbEsPadre = new CheckBox("Es padre");
        cbEsMadre = new CheckBox("Es madre");
        txtHijos = new TextField(); txtHijos.setPromptText("Nombre hijos");
        txtTelefono = new TextField(); txtTelefono.setPromptText("Teléfono");
        txtRedes = new TextField(); txtRedes.setPromptText("Redes sociales");
        txtEmail = new TextField(); txtEmail.setPromptText("Email");
        cmbOcupacion = new ChoiceBox<>();
        txtGustosMusicales = new TextField(); txtGustosMusicales.setPromptText("Gustos musicales");
        txtClubFutbol = new TextField(); txtClubFutbol.setPromptText("Club de fútbol");
        txtBebidas = new TextField(); txtBebidas.setPromptText("Gusto de bebidas");
        txtComida = new TextField(); txtComida.setPromptText("Preferencias comida");
        btnGuardar = new Button("Guardar");

        panelFormulario.getChildren().addAll(
            new Label("Categoría:"), cmbCategoria,
            new Label("Nombre:"), txtNombre,
            new Label("Apellido:"), txtApellido,
            new Label("Referencia:"), txtReferencia,
            new Label("Próximo Contacto:"), dpProximoContacto,
            new Label("Dirección:"), txtDireccion,
            new Label("Localidad:"), txtLocalidad,
            new Label("Cumpleaños:"), dpCumpleaños,
            new Label("Datos Personales:"), taDatosPersonales,
            new Label("Datos Laborales:"), taDatosLaborales,
            new Label("Datos Venta:"), taDatosVenta,
            new Label("Datos Compra:"), taDatosCompra,
            cbDeseaContacto, cbFueCliente,
            new Label("Fecha Compra/Venta:"), dpFechaCompraVenta,
            cbEsReferidor,
            new Label("Refirió a:"), txtRefirioA,
            new Label("Referido por:"), txtReferidoPor,
            cbEsPadre, cbEsMadre,
            new Label("Nombre Hijos:"), txtHijos,
            new Label("Teléfono:"), txtTelefono,
            new Label("Redes Sociales:"), txtRedes,
            new Label("Email:"), txtEmail,
            new Label("Ocupación:"), cmbOcupacion,
            new Label("Gustos Musicales:"), txtGustosMusicales,
            new Label("Club de Fútbol:"), txtClubFutbol,
            new Label("Gusto de Bebidas:"), txtBebidas,
            new Label("Preferencias Comida:"), txtComida,
            btnGuardar
        );

        // Botones CSV al final
        btnExportarCsv = new Button("Exportar a CSV");
        btnImportarCsv = new Button("Importar desde CSV");
        HBox botonesCsv = new HBox(10, btnExportarCsv, btnImportarCsv);
        botonesCsv.setPadding(new Insets(10));
        panelFormulario.getChildren().add(botonesCsv);
    }

    public VBox getView() {
        return panelFormulario;
    }

    public Button getBtnGuardar() {
        return btnGuardar;
    }

    public Button getBtnExportarCsv() {
        return btnExportarCsv;
    }

    public Button getBtnImportarCsv() {
        return btnImportarCsv;
    }

    public void precargar(Cliente cliente) {
        if (cliente != null) {
            cmbCategoria.setValue(cliente.getCategoria());
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
            txtReferencia.setText(cliente.getReferencia());
            dpProximoContacto.setValue(cliente.getProximoContacto());
            txtDireccion.setText(cliente.getDireccion());
            txtLocalidad.setText(cliente.getLocalidad());
            dpCumpleaños.setValue(cliente.getCumpleaños());
            taDatosPersonales.setText(cliente.getDatosPersonales());
            taDatosLaborales.setText(cliente.getDatosLaborales());
            taDatosVenta.setText(cliente.getDatosVenta());
            taDatosCompra.setText(cliente.getDatosCompra());
            cbDeseaContacto.setSelected(cliente.isDeseaContacto());
            cbFueCliente.setSelected(cliente.isFueCliente());
            dpFechaCompraVenta.setValue(cliente.getFechaCompraVenta());
            cbEsReferidor.setSelected(cliente.isEsReferidor());
            txtRefirioA.setText(cliente.getRefirioA());
            txtReferidoPor.setText(cliente.getReferidoPor());
            cbEsPadre.setSelected(cliente.isEsPadre());
            cbEsMadre.setSelected(cliente.isEsMadre());
            txtHijos.setText(cliente.getNombreHijos());
            txtTelefono.setText(cliente.getTelefono());
            txtRedes.setText(cliente.getRedesSociales());
            txtEmail.setText(cliente.getEmail());
            cmbOcupacion.setValue(cliente.getOcupacion());
            txtGustosMusicales.setText(cliente.getGustosMusicales());
            txtClubFutbol.setText(cliente.getClubFutbol());
            txtBebidas.setText(cliente.getGustoBebidas());
            txtComida.setText(cliente.getPreferenciasComida());
        } else {
            limpiar();
        }
    }

    public boolean isNew() {
        return txtNombre.getText().isEmpty() && txtApellido.getText().isEmpty();
    }

    public boolean guardar() {
        // Lógica para guardar
        return true;
    }

    public boolean actualizar() {
        // Lógica para actualizar
        return true;
    }

    private void limpiar() {
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
}
