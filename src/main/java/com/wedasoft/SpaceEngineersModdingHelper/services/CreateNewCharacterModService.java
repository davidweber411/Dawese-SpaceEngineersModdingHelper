package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CreateNewCharacterModService {

    private final ConfigurationsRepository configurationsRepository;

    public void createNewCharacterMod() throws NotValidException {
        ConfigurationsEntity configurations = configurationsRepository.loadConfigurations();
        if (configurations == null) {
            throw new NotValidException("You must set the configurations first.");
        }
        Path modsWorkspacePath = Paths.get(configurations.getPathToModsWorkspace());
        if (!Files.exists(modsWorkspacePath)) {
            throw new NotValidException(String.format("""
                    Your configured path to your modding workspace doesn't exist.
                    Your configured path: '%s'""", modsWorkspacePath));
        }
        System.out.println("YOLO");
    }


}
