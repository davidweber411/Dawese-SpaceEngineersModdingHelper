package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.services.CreateNewCharacterModService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CreateANewCharacterModController implements Initializable {

    private final CreateNewCharacterModService createNewCharacterModService;
    private final JfxUiService jfxUiService;

    @FXML
    private TextField modNameTextField;
    @FXML
    private TextField wishedSubtypeTextField;
    @FXML
    private ChoiceBox<Gender> genderChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderChoiceBox.setItems(FXCollections.observableArrayList(Arrays.asList(Gender.values())));
        genderChoiceBox.getSelectionModel().select(0);
    }

    public void init() {
    }

    public void onCreateModInWorkspaceButtonClick() {
        try {
            createNewCharacterModService.createNewCharacterMod(
                    modNameTextField,
                    wishedSubtypeTextField,
                    genderChoiceBox.getValue()
            );
            jfxUiService.displayInformationDialog("Character mod created in your workspace!");
        } catch (NotValidException e) {
            jfxUiService.displayWarnDialog(e.getMessage());
        } catch (IOException | URISyntaxException e) {
            jfxUiService.displayErrorDialog(e);
        }
    }

}
