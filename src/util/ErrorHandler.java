package util;

import javafx.scene.control.Alert;

public class ErrorHandler {

    /**
     * Muestra un alert de error.
     *
     * @param title   Título del alert.
     * @param message Mensaje de error.
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un alert de información.
     *
     * @param title   Título del alert.
     * @param message Mensaje de información.
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
