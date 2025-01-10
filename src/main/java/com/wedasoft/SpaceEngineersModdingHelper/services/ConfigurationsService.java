package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationsService {

    private final ConfigurationsRepository configurationsRepository;

    public ConfigurationsEntity loadConfigurations() {
        if (configurationsRepository.findAll().size() > 1) {
            throw new RuntimeException("There mustn't be more than one configurationsEntity!");
        }
        return configurationsRepository.findAll().stream().findFirst().orElse(new ConfigurationsEntity());
    }

    public void saveConfigurations(ConfigurationsEntity configurationsEntity) {
        if (configurationsRepository.findAll().size() > 1) {
            throw new RuntimeException("There mustn't be more than one configurationsEntity!");
        }
        configurationsRepository.save(configurationsEntity);
    }

}
