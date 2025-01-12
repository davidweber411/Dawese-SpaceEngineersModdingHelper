package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.wedasoftFxGuiCommons.jfxDialogs.JfxDialogUtil;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class JfxUiService {

    private final ApplicationContext springApplicationContext;

    public void createAndShowFxmlDialog(
            String title, boolean dialogIsModal, boolean dialogIsResizeable,
            URL absoluteFxmlFileUrl, Dimension2D sceneSize,
            Consumer<Object> initMethodOfController)
            throws IOException {

        createFxmlDialog(
                title, dialogIsModal, dialogIsResizeable,
                absoluteFxmlFileUrl, sceneSize,
                initMethodOfController).showAndWait();
    }

    public Stage createFxmlDialog(
            String title, boolean dialogIsModal, boolean dialogIsResizeable,
            URL absoluteFxmlFileUrl, Dimension2D sceneSize,
            Consumer<Object> initMethodOfController)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(absoluteFxmlFileUrl);
        loader.setControllerFactory(springApplicationContext::getBean);
        Parent root = loader.load();
        Object viewController = loader.getController();
        Scene scene = sceneSize == null ? new Scene(root) : new Scene(root, sceneSize.getWidth(), sceneSize.getHeight());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(dialogIsModal ? Modality.APPLICATION_MODAL : Modality.NONE);
        stage.setResizable(dialogIsResizeable);
        stage.setScene(scene);
        if (initMethodOfController != null) {
            initMethodOfController.accept(viewController);
        }

        return stage;
    }

    public void displayInformationDialog(String text) {
        JfxDialogUtil.createInformationDialog(text).showAndWait();
    }

    public void displayWarnDialog(String text) {
        JfxDialogUtil.createWarningDialog(text).showAndWait();
    }

    public void displayErrorDialog(Exception e) {
        JfxDialogUtil.createErrorDialog(e.getMessage(), e).showAndWait();
    }

    public Parent loadAndGetNewSceneParent(URL absoluteFxmlFileUrl, Consumer initMethodOfController) throws IOException {
        FXMLLoader loader = new FXMLLoader(absoluteFxmlFileUrl);
        loader.setControllerFactory(springApplicationContext::getBean);
        Parent parent = loader.load();
        Object viewController = loader.getController();
        if (initMethodOfController != null) {
            initMethodOfController.accept(viewController);
        }
        return parent;
    }

    public void displayConfirmDialog(String contentText, Runnable callbackOnOk) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation please");
        alert.setContentText(contentText);
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            callbackOnOk.run();
        }
    }

}
