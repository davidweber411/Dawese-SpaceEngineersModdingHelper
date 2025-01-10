package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UiController implements Initializable {

    private final JfxUiService jfxUiService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onExitMenuItemClick() {
        Platform.exit();
        System.exit(0);
    }

    public void onPreferencesMenuItemClick() throws IOException {
        jfxUiService.createAndShowFxmlDialog(
                "Preferences",
                true,
                true,
                getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/preferences.fxml"),
                null,
                dialogController -> ((PreferencesController) dialogController).init());
    }

    public void onDeployModIntoSeModsDirectoryButtonClick() throws IOException {
        jfxUiService.createAndShowFxmlDialog(
                "Deploy mod into SE \"Mods\" directory",
                true,
                true,
                getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/deployMod.fxml"),
                null,
                dialogController -> ((DeployModController) dialogController).init());
    }

}
