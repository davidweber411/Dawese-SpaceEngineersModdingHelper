package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationsService {

    private final ConfigurationsRepository configurationsRepository;

    public ConfigurationsEntity loadConfigurations() {
        return configurationsRepository.loadConfigurations();
    }

    public ConfigurationsEntity loadAndValidateConfigurations() throws NotValidException {
        return configurationsRepository.loadAndValidateConfigurations();
    }

    public void saveConfigurations(ConfigurationsEntity configurationsEntity) {
        configurationsRepository.saveConfigurations(configurationsEntity);
    }

}
