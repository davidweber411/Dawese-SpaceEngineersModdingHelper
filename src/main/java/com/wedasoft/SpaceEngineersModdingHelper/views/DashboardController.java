package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DashboardController {

    private final ConfigurationsService configurationsService;
    private final JfxUiService jfxUiService;

    @FXML
    private BorderPane dashboardBorderPane;
    @FXML
    private VBox dashboardContentVbox;

    public void init() {
        ConfigurationsEntity configurations = configurationsService.loadConfigurations();
        if (configurations == null) {
            dashboardContentVbox.getChildren().add(createRedLabel("You have not set the configurations!"));
            Button setConfigurationsHereButton = new Button("Set configurations here");
            setConfigurationsHereButton.setOnAction(e -> {
                try {
                    jfxUiService.createAndShowFxmlDialog(
                            "Configurations",
                            true,
                            true,
                            getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/configurations.fxml"),
                            null,
                            controller -> ((ConfigurationsController) controller).init(this::reinit));
                } catch (IOException ex) {
                    jfxUiService.displayWarnDialog("That didn't work. Please set the configurations via the menu bar.");
                    jfxUiService.displayErrorDialog(ex);
                }
            });
            dashboardContentVbox.getChildren().add(setConfigurationsHereButton);
        } else {
            dashboardContentVbox.getChildren().add(new Label("Path to your modding workspace: " + configurations.getPathToModsWorkspace()));
            dashboardContentVbox.getChildren().add(new Label("Path to your Space Engineers \"Mods\" directory: " + configurations.getPathToAppdataModsDirectory()));
        }
    }

    private void reinit() {
        dashboardContentVbox.getChildren().clear();
        init();
    }

    private Label createRedLabel(String text) {
        Label label = new Label(text);
        Font font = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 16);
        label.setFont(font);
        label.setTextFill(Color.RED);
        return label;
    }

    private Label createGreenLabel(String text) {
        Label label = new Label(text);
        Font font = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 16);
        label.setFont(font);
        label.setTextFill(Color.GREEN);
        return label;
    }

}
