package com.wedasoft.SpaceEngineersModdingHelper.views;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DeployModController {

    @FXML
    private ListView<String> modsListView;

    public void init() {
    }

    public void onCancelButtonClick() {
        System.out.println("CANCEL");
    }

    public void onDeploySelectedModButtonClick() {
        System.out.println("DEPLOY");
    }

}
