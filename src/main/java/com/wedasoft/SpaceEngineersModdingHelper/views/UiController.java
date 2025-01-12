package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UiController implements Initializable {

    private final JfxUiService jfxUiService;

    @FXML
    private StackPane centerStackPane;
    @FXML
    private Button dashboardButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> dashboardButton.fire()); // set start page
    }

    public void onExitMenuItemClick() {
        Platform.exit();
        System.exit(0);
    }

    public void onPreferencesMenuItemClick() throws IOException {
        jfxUiService.createAndShowFxmlDialog(
                "Preferences",
                true,
                true,
                getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/preferences.fxml"),
                null,
                dialogController -> ((PreferencesController) dialogController).init());
    }

    public void onDashboardButtonClick() throws IOException {
        Parent parent = jfxUiService.loadAndGetNewSceneParent(getClass().getResource(
                        "/com/wedasoft/SpaceEngineersModdingHelper/views/dashboard.fxml"),
                controller -> ((DashboardController) controller).init());
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(parent);
    }

    public void onDeployModIntoSeModsDirectoryButtonClick() throws IOException {
        Parent parent = jfxUiService.loadAndGetNewSceneParent(getClass().getResource(
                        "/com/wedasoft/SpaceEngineersModdingHelper/views/deployMod.fxml"),
                controller -> ((DeployModController) controller).init());
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(parent);
    }

}
