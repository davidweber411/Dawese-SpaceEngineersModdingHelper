package com.wedasoft.SpaceEngineersModdingHelper.repositories;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConfigurationsRepository {

    private final ConfigurationsEntityRepository configurationsEntityRepository;

    public ConfigurationsEntity loadConfigurations() {
        if (configurationsEntityRepository.findAll().size() > 1) {
            throw new RuntimeException("There mustn't be more than one configurationsEntity!");
        }
        return configurationsEntityRepository.findAll().stream().findFirst().orElse(null);
    }

    public void saveConfigurations(ConfigurationsEntity configurationsEntity) {
        ConfigurationsEntity existingConfigurations = configurationsEntityRepository.findAll().stream().findFirst().orElse(null);
        if (existingConfigurations != null && existingConfigurations.getId() != configurationsEntity.getId()) {
            throw new RuntimeException("There mustn't be more than one configurationsEntity!");
        }
        configurationsEntityRepository.save(configurationsEntity);
    }

}
