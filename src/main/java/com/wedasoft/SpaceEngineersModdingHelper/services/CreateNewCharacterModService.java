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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateNewCharacterModService {

    private static final String CHAR_MALE_TEMPLATE = "CharacterMaleTemplate";
    private static final String CHAR_FEMALE_TEMPLATE = "CharacterFemaleTemplate";

    private final ConfigurationsRepository configurationsRepository;
    private final FileSystemRepository fileSystemRepository;

    public void createNewCharacterMod(
            TextField modNameTextField,
            TextField wishedSubtypeTextField,
            Gender gender) throws NotValidException, IOException, URISyntaxException {

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

        if (Files.exists(modsWorkspacePath.resolve(modNameTextField.getText()))) {
            throw new NotValidException("A mod with this name does already exist in your modding workspace.");
        }

        // create mod dir
        final Path modDir = fileSystemRepository.createDirectoryIn(modNameTextField.getText(), modsWorkspacePath);

        // create thumbnail
        fileSystemRepository.createJpgWithTextContentInto(modNameTextField, modDir);

        // create data dir
        final String internalName = wishedSubtypeTextField.getText();

        final Path data = fileSystemRepository.createDirectoryIn("Data", modDir);
        final Path dataSubdir = fileSystemRepository.createDirectoryIn(internalName, data);
        if (gender == Gender.MALE) {
            createModifiedSbcFileInDirectory(
                    Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_EntityContainers.sbc")),
                    internalName, dataSubdir);
            createModifiedSbcFileInDirectory(
                    Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_Characters.sbc")),
                    internalName, dataSubdir);
        } else {
            createModifiedSbcFileInDirectory(
                    Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_EntityContainers.sbc")),
                    internalName, dataSubdir);
            createModifiedSbcFileInDirectory(
                    Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_Characters.sbc")),
                    internalName, dataSubdir);
        }

        // create models dir
        final Path models = fileSystemRepository.createDirectoryIn("Models", modDir);
        fileSystemRepository.createDirectoryIn(internalName, models);

        // create textures dir
        final Path textures = fileSystemRepository.createDirectoryIn("Textures", modDir);
        fileSystemRepository.createDirectoryIn(internalName, textures);
    }

    private void createModifiedSbcFileInDirectory(
            URL urlToTemplateFile,
            String replacementText,
            Path targetDirectory) throws URISyntaxException, IOException {

        final Path templateFile = Path.of(urlToTemplateFile.toURI());
        final List<String> modifiedLines = Files.readAllLines(templateFile)
                .stream()
                .map(line -> {
                    line = line
                            .replaceAll(CHAR_MALE_TEMPLATE, replacementText)
                            .replaceAll(CHAR_FEMALE_TEMPLATE, replacementText);
                    return line;
                }).toList();

        final String modifiedFileName = templateFile.getFileName().toString()
                .replaceAll(CHAR_MALE_TEMPLATE, replacementText)
                .replaceAll(CHAR_FEMALE_TEMPLATE, replacementText);
        final Path modifiedFile = targetDirectory.resolve(modifiedFileName);
        Files.write(modifiedFile, modifiedLines);
    }

}
