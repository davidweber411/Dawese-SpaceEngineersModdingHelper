package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.services.CreateNewEmptyModService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
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
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UiController implements Initializable {

    private final JfxUiService jfxUiService;
    private final CreateNewEmptyModService createNewEmptyModService;

    @FXML
    private StackPane centerStackPane;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button logScannerButton;
    @FXML
    private Button deployModButton;
    @FXML
    private Button createNewCharacterModButton;
    @FXML
    private Button createNewEmptyModButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jfxUiService.createTooltipFor(dashboardButton, "The dashboard contains an overview over your configurations.");
        jfxUiService.createTooltipFor(logScannerButton, "The log scanner gives you access to the SE error logs on a simple way.");
        jfxUiService.createTooltipFor(deployModButton, "This functionality is used to copy the mod with just the relevant files and directories from your workspace into the 'Mods' dir of your SE installation.");
        jfxUiService.createTooltipFor(createNewCharacterModButton, "Creates a template mod for creating a new character.");
        jfxUiService.createTooltipFor(createNewEmptyModButton, "Create a new empty template mod.");
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

    public void onDeployModButtonClick() throws IOException {
        Stage dialog = jfxUiService.createFxmlDialog(
                "Deploy mod",
                false,
                true,
                getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/deployMod.fxml"),
                new Dimension2D(600, 400),
                controller -> ((DeployModController) controller).init());
        dialog.initOwner(centerStackPane.getScene().getWindow());
        dialog.show();
    }

    public void onCreateNewCharacterModButtonClick() throws IOException {
        Parent parent = jfxUiService.loadAndGetNewSceneParent(getClass().getResource(
                        "/com/wedasoft/SpaceEngineersModdingHelper/views/createNewCharacterMod.fxml"),
                controller -> ((CreateNewCharacterModController) controller).init());
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(parent);
    }

    public void onCreateNewEmptyModButtonClick() {
        Optional<String> modname = jfxUiService.displayInputDialog("Create new empty mod", "Please enter the modname:");
        if (modname.isEmpty()) {
            return;
        }
        if (modname.get().isBlank()) {
            jfxUiService.displayWarnDialog("You must enter a mod name.");
            return;
        }
        try {
            createNewEmptyModService.createNewEmptyMod(modname.get());
            jfxUiService.displayInformationDialog("Mod '" + modname.get() + "' created!");
        } catch (NotValidException e) {
            jfxUiService.displayWarnDialog(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
//            jfxUiService.displayErrorDialog(e);
        }
    }

}
