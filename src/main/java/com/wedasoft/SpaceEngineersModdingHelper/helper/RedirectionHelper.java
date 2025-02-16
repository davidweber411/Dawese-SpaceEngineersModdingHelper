package com.wedasoft.SpaceEngineersModdingHelper.helper;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Window;

public class RedirectionHelper {

    public static void redirectToDashboard() {
        for (Window window : Window.getWindows()) {
            if (window.getScene() != null) {
                Node node = window.getScene().lookup("#dashboardButton");
                if (node != null) {
                    Platform.runLater(() -> ((Button) node).fire());
                    return;
                }
            }
        }
        throw new RuntimeException("Couldn't redirect to the dashboard!");
    }

}
