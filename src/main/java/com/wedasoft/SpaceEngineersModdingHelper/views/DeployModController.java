package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.DeploymentService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
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
    private ListView<File> modsListView;

    public void init() {
        ConfigurationsEntity configurationsEntity = configurationsService.loadConfigurations();
        if (configurationsEntity == null) {
            jfxUiService.displayWarnDialog("You must set the configurations first.");
            return;
        }
        File pathToModsWorkspace = new File(configurationsEntity.getPathToModsWorkspace());
        if (!pathToModsWorkspace.exists() || !pathToModsWorkspace.isDirectory()) {
            jfxUiService.displayWarnDialog("The path to your configured mod workspace doesn't exist or isn't a directory: " + pathToModsWorkspace);
            return;
        }

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
        ObservableList<File> allModDirectories = FXCollections.observableArrayList(
                Arrays.stream(Objects.requireNonNull(pathToModsWorkspace.listFiles())).filter(File::isDirectory).toList());
        modsListView.setItems(allModDirectories);
    }

    public void onCancelButtonClick() {
        ((Stage) modsListView.getScene().getWindow()).close();
    }

    public void onDeploySelectedModButtonClick() {
        File selectedMod = modsListView.getSelectionModel().getSelectedItem();
        if (selectedMod == null) {
            jfxUiService.displayWarnDialog("You must select a mod first.");
            return;
        }
        try {
            deploymentService.deployMod(selectedMod);
        } catch (NotValidException e) {
            jfxUiService.displayWarnDialog(e.getMessage());
        } catch (IOException e) {
            jfxUiService.displayErrorDialog(e);
        }
    }

}
