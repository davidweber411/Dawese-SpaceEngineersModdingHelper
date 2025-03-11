package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.helper.RedirectionHelper;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.DeploymentService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DeployModController {

    private final ConfigurationsService configurationsService;
    private final DeploymentService deploymentService;
    private final JfxUiService jfxUiService;

    @FXML
    private BorderPane deployModBorderPane;
    @FXML
    private ListView<File> modsListView;

    public void init() {
        ConfigurationsEntity configurations;
        try {
            configurations = configurationsService.loadAndValidateConfigurations();
        } catch (NotValidException e) {
            jfxUiService.displayWarnDialog(e.getMessage());
            RedirectionHelper.redirectToDashboard();
            return;
        }

        deployModBorderPane.getScene().getWindow().setOnCloseRequest(e -> unloadScene());
        deployModBorderPane.parentProperty().addListener((observable, oldParent, newParent) -> {
            if (newParent == null) {
                unloadScene();
            }
        });

        modsListView.setCellFactory(listView -> new TextFieldListCell<>() {
            @Override
            public void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                if (empty || file == null) {
                    setText(null);
                } else {
                    setText(file.getAbsolutePath());
                }
            }
        });
        modsListView.setItems(FXCollections.observableArrayList(
                Arrays.stream(Objects.requireNonNull(new File(configurations.getPathToModsWorkspace()).listFiles()))
                        .filter(File::isDirectory).toList()));
    }

    private void unloadScene() {
        System.out.println("unloadScene from deploy mod");
    }

    public void onDeploySelectedModButtonClick() {
        File selectedMod = modsListView.getSelectionModel().getSelectedItem();
        if (selectedMod == null) {
            jfxUiService.displayWarnDialog("You must select a mod first.");
            return;
        }
        try {
            deploymentService.deployMod(selectedMod);
            jfxUiService.displayInformationDialog("Mod deployed!");
        } catch (NotValidException e) {
            jfxUiService.displayWarnDialog(e.getMessage());
        } catch (IOException e) {
            jfxUiService.displayErrorDialog(e);
        }
    }

}
