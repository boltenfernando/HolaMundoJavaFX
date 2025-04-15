package app;

import controller.ClienteController;
import db.TestSQLite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Cliente;
import service.RecordatorioService;

public class ClienteApp extends Application {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Prepara la base de datos y recordatorios
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnas();
        try {
            RecordatorioService.mostrarRecordatorios();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // El controlador crea y muestra toda la interfaz
        new ClienteController(primaryStage, clientes);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
