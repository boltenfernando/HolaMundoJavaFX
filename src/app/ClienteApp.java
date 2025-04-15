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
    @Override
    public void start(Stage primaryStage) {
        TestSQLite.createTable();
        TestSQLite.verificarYAgregarColumnas();
        try { RecordatorioService.mostrarRecordatorios(); } catch (Exception e) { e.printStackTrace(); }
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        new ClienteController(primaryStage, clientes);
    }
    public static void main(String[] args) { launch(args); }
}
