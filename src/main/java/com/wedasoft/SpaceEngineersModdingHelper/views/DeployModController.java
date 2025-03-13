package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.helper.RedirectionHelper;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.DeploymentService;
import com.wedasoft.SpaceEngineersModdingHelper.services.FileSystemService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DeployModController {

    private final ConfigurationsService configurationsService;
    private final DeploymentService deploymentService;
    private final FileSystemService fileSystemService;
    private final JfxUiService jfxUiService;

    @FXML
    private BorderPane deployModBorderPane;
    @FXML
    private ListView<File> modsListView;
    @FXML
    private ToggleButton autoDeploySelectedModToggleButton;

    private ScheduledExecutorService autoDeployExecutorService;
    private long lastModSize = 0;

    public void init() {
        ConfigurationsEntity configurations;
        try {
            configurations = configurationsService.loadAndValidateConfigurations();
        } catch (NotValidException e) {
            jfxUiService.displayWarnDialog(e.getMessage());
            RedirectionHelper.redirectToDashboard();
            return;
        }

        deployModBorderPane.getScene().getWindow().setOnCloseRequest(e -> resetAutoDeployFeature());
        deployModBorderPane.parentProperty().addListener((observable, oldParent, newParent) -> {
            if (newParent == null) {
                resetAutoDeployFeature();
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
        modsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("ListView selection changed.");
            resetAutoDeployFeature();
        });
    }

    private void resetAutoDeployFeature() {
        autoDeploySelectedModToggleButton.setSelected(false);
        if (autoDeployExecutorService != null) {
            System.out.println("Shutdown executor service 'autoDeployExecutorService' now...");
            autoDeployExecutorService.shutdown();
        }
    }

    public void onAutoDeploySelectedModToggleButtonClick(ActionEvent event) {
        File selectedMod = modsListView.getSelectionModel().getSelectedItem();
        if (selectedMod == null) {
            autoDeploySelectedModToggleButton.setSelected(false);
            jfxUiService.displayWarnDialog("You must select a mod first.");
            return;
        }

        ToggleButton button = (ToggleButton) event.getSource();
        if (button.isSelected()) {
            if (!((ToggleButton) event.getSource()).isSelected()) {
                System.out.println("Shutdown executor service 'autoDeployExecutorService' now...");
                autoDeployExecutorService.shutdown();
            }

            autoDeployExecutorService = Executors.newScheduledThreadPool(1);
            autoDeployExecutorService.scheduleAtFixedRate(() -> {
                try {
                    System.out.println("Checking for edited files...");
                    if (lastModSize != fileSystemService.getSizeInBytes(selectedMod.toPath())) {
                        System.out.println("Deploying automatically...");
                        deploymentService.deployMod(selectedMod);
                        lastModSize = fileSystemService.getSizeInBytes(selectedMod.toPath());
                    }
                } catch (NotValidException e) {
                    jfxUiService.displayWarnDialog(e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, 0, 7, SECONDS);
        } else {
            autoDeployExecutorService.shutdown();
        }
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
