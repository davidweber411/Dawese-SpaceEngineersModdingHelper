package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
    @FXML
    private Button logScannerButton;
    @FXML
    private Button deployModIntoSeModsDirectoryButton;
    @FXML
    private Button createNewCharacterModButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jfxUiService.createTooltipFor(dashboardButton, "The dashboard contains an overview over your configurations.");
        jfxUiService.createTooltipFor(logScannerButton, "The log scanner gives you access to the SE error logs on a simple way.");
        jfxUiService.createTooltipFor(deployModIntoSeModsDirectoryButton, "This functionality is used to copy the mod with just the relevant files and directories from your workspace into the 'Mods' dir of your SE installation.");
        jfxUiService.createTooltipFor(createNewCharacterModButton, "This functionality is used to create a template mod when creating a new character.");
        Platform.runLater(() -> dashboardButton.fire()); // set start page
    }

    public void onExitMenuItemClick() {
        Platform.exit();
        System.exit(0);
    }

    public void onConfigurationsMenuItemClick() throws IOException {
        jfxUiService.createAndShowFxmlDialog(
                "Configurations",
                true,
                true,
                getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/configurations.fxml"),
                null,
                dialogController -> ((ConfigurationsController) dialogController).init());
    }

    public void onDashboardButtonClick() throws IOException {
        Parent parent = jfxUiService.loadAndGetNewSceneParent(getClass().getResource(
                        "/com/wedasoft/SpaceEngineersModdingHelper/views/dashboard.fxml"),
                controller -> ((DashboardController) controller).init());
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(parent);
    }

    public void onLogScannerButtonClick() throws IOException {
        Stage logScannerDialog = jfxUiService.createFxmlDialog(
                "Log scanner",
                false,
                true,
                getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/logScanner.fxml"),
                null,
                controller -> ((LogScannerController) controller).init());
        logScannerDialog.initOwner(centerStackPane.getScene().getWindow());
        logScannerDialog.show();
    }

    public void onDeployModIntoSeModsDirectoryButtonClick() throws IOException {
        Parent parent = jfxUiService.loadAndGetNewSceneParent(getClass().getResource(
                        "/com/wedasoft/SpaceEngineersModdingHelper/views/deployMod.fxml"),
                controller -> ((DeployModController) controller).init());
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(parent);
    }

    public void onCreateNewCharacterModButtonClick() throws IOException {
        Parent parent = jfxUiService.loadAndGetNewSceneParent(getClass().getResource(
                        "/com/wedasoft/SpaceEngineersModdingHelper/views/createNewCharacterMod.fxml"),
                controller -> ((CreateNewCharacterModController) controller).init());
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(parent);
    }

}
