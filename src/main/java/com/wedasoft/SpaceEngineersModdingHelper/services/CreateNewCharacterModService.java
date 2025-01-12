package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CreateNewCharacterModService {

    private final ConfigurationsRepository configurationsRepository;
    private final FileSystemRepository fileSystemRepository;

    public void createNewCharacterMod(
            TextField modNameTextField,
            TextField wishedSubtypeTextField,
            Gender gender) throws NotValidException, IOException {

        if (modNameTextField.getText().isBlank()) {
            throw new NotValidException("You must enter a name for your mod.");
        }
        if (wishedSubtypeTextField.getText().isBlank() || !wishedSubtypeTextField.getText().matches("[a-zA-Z_]+")) {
            throw new NotValidException("Your entered subtype for your character is invalid.\nOnly alphabetic characters and the underscore are allowed.");
        }
        if (gender == null) {
            throw new NotValidException("Your entered gender is invalid.");
        }

        final ConfigurationsEntity configurations = configurationsRepository.loadConfigurations();
        if (configurations == null) {
            throw new NotValidException("You must set the configurations first.");
        }
        final Path modsWorkspacePath = Paths.get(configurations.getPathToModsWorkspace());
        if (!Files.exists(modsWorkspacePath)) {
            throw new NotValidException(String.format("""
                    Your configured path to your modding workspace doesn't exist.
                    Your configured path: '%s'""", modsWorkspacePath));
        }

        fileSystemRepository.createDirectoryIn(modNameTextField.getText(), modsWorkspacePath);

    }

}
