package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CreateANewCharacterModController implements Initializable {

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

    }

}
