package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PreferencesController {

    private final ConfigurationsService configurationsService;
    private final JfxUiService jfxUiService;

    @FXML
    private TextField pathToModdingWorkingSpaceTextField;
    @FXML
    private TextField pathToSpaceEngineersAppdataModsDirectoryTextField;

    private ConfigurationsEntity configurationsEntity;

    public void init() {
        configurationsEntity = configurationsService.loadConfigurations();
        pathToModdingWorkingSpaceTextField.setText(configurationsEntity.getPathToModdingWorkingSpace());
        pathToSpaceEngineersAppdataModsDirectoryTextField.setText(configurationsEntity.getPathToSpaceEngineersAppdataModsDirectory());
    }

    public void onSaveButtonClick() {
        configurationsEntity.setPathToModdingWorkingSpace(pathToModdingWorkingSpaceTextField.getText());
        configurationsEntity.setPathToSpaceEngineersAppdataModsDirectory(pathToSpaceEngineersAppdataModsDirectoryTextField.getText());
        configurationsService.saveConfigurations(configurationsEntity);
        jfxUiService.displayInformationDialog("Saved configurations!");
        ((Stage) pathToModdingWorkingSpaceTextField.getScene().getWindow()).close();
    }

    public void onCancelButtonClick() {
        ((Stage) pathToModdingWorkingSpaceTextField.getScene().getWindow()).close();
    }

}
