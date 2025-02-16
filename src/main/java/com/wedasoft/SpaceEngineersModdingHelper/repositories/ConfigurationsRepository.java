package com.wedasoft.SpaceEngineersModdingHelper.repositories;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntityRepository;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (existingConfigurations != null && !Objects.equals(existingConfigurations.getId(), configurationsEntity.getId())) {
            throw new RuntimeException("There mustn't be more than one configurationsEntity!");
        }
        configurationsEntityRepository.save(configurationsEntity);
    }

    public void checkForProblems(ConfigurationsEntity configurations) throws NotValidException {
        if (configurations == null) {
            throw new NotValidException("You haven't set the configurations!");
        }

        List<String> problems = new ArrayList<>();

        File modsWorkspaceDir = new File(configurations.getPathToModsWorkspace());
        if (!modsWorkspaceDir.exists() || !modsWorkspaceDir.isDirectory()) {
            problems.add("Your set path to your mods workspace directory doesn't exist or isn't pointing to a directory: "
                         + configurations.getPathToModsWorkspace());
        }

        File appDataSeDir = new File(configurations.getPathToAppdataSpaceEngineersDirectory());
        if (!appDataSeDir.exists() || !appDataSeDir.isDirectory()) {
            problems.add("Your set path to your appdata Space Engineers directory doesn't exist or isn't pointing to a directory: "
                         + configurations.getPathToAppdataSpaceEngineersDirectory());
        }

        if (problems.isEmpty()) {
            return;
        }
        throw new NotValidException(String.join("\n\n", problems));
    }

    public ConfigurationsEntity loadAndValidateConfigurations() throws NotValidException {
        ConfigurationsEntity configurations = loadConfigurations();
        checkForProblems(configurations);
        return configurations;
    }

}
