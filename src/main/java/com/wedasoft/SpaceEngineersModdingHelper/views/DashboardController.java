package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.DeploymentService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DashboardController {

    private final ConfigurationsService configurationsService;
    private final DeploymentService deploymentService;
    private final JfxUiService jfxUiService;

    public void init() {

    }

}
