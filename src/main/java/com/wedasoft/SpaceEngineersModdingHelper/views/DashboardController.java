package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DashboardController {

    private final ConfigurationsService configurationsService;

    @FXML
    private BorderPane dashboardBorderPane;

    public void init() {
        ConfigurationsEntity configurations = configurationsService.loadConfigurations();
        if (configurations != null) {
            dashboardBorderPane.setCenter(new Label("""
                    asd
                    asdd
                    a
                    sdd
                    """));
        }
    }

}
