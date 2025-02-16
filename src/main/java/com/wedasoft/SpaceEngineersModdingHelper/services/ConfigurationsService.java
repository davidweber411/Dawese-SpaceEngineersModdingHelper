package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationsService {

    private final ConfigurationsRepository configurationsRepository;

    public ConfigurationsEntity loadConfigurations() {
        return configurationsRepository.loadConfigurations();
    }

    public ConfigurationsEntity loadAndValidateConfigurations() throws NotValidException {
        ConfigurationsEntity configurations = loadConfigurations();
        checkForProblems(configurations);
        return configurations;
    }

    public void checkForProblems(ConfigurationsEntity configurations) throws NotValidException {
        if (configurations == null) {
            throw new NotValidException("You haven't set the configurations!");
        }

        List<String> problems = new ArrayList<>();

        File modsWorkspaceDir = new File(configurations.getPathToModsWorkspace());
        if (!modsWorkspaceDir.exists() || !modsWorkspaceDir.isDirectory()) {
            problems.add("Your set path to your mods workspace directory doesn't exist or isn't pointing to a directory!");
        }

        File appDataSeDir = new File(configurations.getPathToAppdataSpaceEngineersDirectory());
        if (!appDataSeDir.exists() || !appDataSeDir.isDirectory()) {
            problems.add("Your set path to your appdata Space Engineers directory doesn't exist or isn't pointing to a directory!");
        }

        if (problems.isEmpty()) {
            return;
        }
        throw new NotValidException(String.join("\n", problems));
    }

    public void saveConfigurations(ConfigurationsEntity configurationsEntity) {
        configurationsRepository.saveConfigurations(configurationsEntity);
    }

}
