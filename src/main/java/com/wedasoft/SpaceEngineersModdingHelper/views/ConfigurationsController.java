package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ConfigurationsController {

    private final ConfigurationsService configurationsService;
    private final JfxUiService jfxUiService;

    @FXML
    private Label pathToYourModsWorkspaceLabel;
    @FXML
    private Label pathToAppdataSpaceEngineersDirectoryLabel;

    @FXML
    private TextField pathToModsWorkspaceTextField;
    @FXML
    private TextField pathToAppdataSpaceEngineersDirectoryTextField;

    private ConfigurationsEntity configurationsEntity;
    private Runnable onSaveButtonClickCallback;

    public void init() {
        jfxUiService.createTooltipFor(pathToYourModsWorkspaceLabel,
                "This is the directory which contains all of your mods.");
        jfxUiService.createTooltipFor(pathToAppdataSpaceEngineersDirectoryLabel,
                "This is a directory of your Space Engineers installation, located somewhere in your %APPDATA% directory. " +
                "E.g. 'C:\\Users\\username\\AppData\\Roaming\\SpaceEngineers'.");

        configurationsEntity = configurationsService.loadConfigurations();
        if (configurationsEntity == null) {
            configurationsEntity = new ConfigurationsEntity();
        }
        pathToModsWorkspaceTextField.setText(configurationsEntity.getPathToModsWorkspace());
        pathToAppdataSpaceEngineersDirectoryTextField.setText(configurationsEntity.getPathToAppdataSpaceEngineersDirectory());
    }

    public void init(Runnable onSaveButtonClickCallback) {
        init();
        this.onSaveButtonClickCallback = onSaveButtonClickCallback;
    }

    public void onSaveButtonClick() {
        configurationsEntity.setPathToModsWorkspace(pathToModsWorkspaceTextField.getText());
        configurationsEntity.setPathToAppdataSpaceEngineersDirectory(pathToAppdataSpaceEngineersDirectoryTextField.getText());
        configurationsService.saveConfigurations(configurationsEntity);
        jfxUiService.displayInformationDialog("Saved configurations!");
        ((Stage) pathToModsWorkspaceTextField.getScene().getWindow()).close();
        if (onSaveButtonClickCallback != null) {
            onSaveButtonClickCallback.run();
        }
    }

    public void onCancelButtonClick() {
        ((Stage) pathToModsWorkspaceTextField.getScene().getWindow()).close();
    }

    public void onChoosePathToModsWorkspaceButtonClick() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the path to your mods workspace directory");
        File path = dc.showDialog(null);
        if (path != null) {
            pathToModsWorkspaceTextField.setText(path.getAbsolutePath());
        }
    }

    public void onChoosePathToAppdataSpaceEngineersDirectoryButtonClick() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the path to your '%APPDATA%/.../SpaceEngineers' directory");
        File path = dc.showDialog(null);
        if (path != null) {
            pathToAppdataSpaceEngineersDirectoryTextField.setText(path.getAbsolutePath());
        }
    }

}
