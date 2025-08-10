package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CharacterModCreationService {

    private final ConfigurationsRepository configurationsRepository;
    private final FileSystemRepository fileSystemRepository;
    private final SbcFileCreator sbcFileCreator;

    public void createNewCharacterMod(
            String modName, String newSubtype, Gender gender,
            boolean createDevDataDir, boolean createAdditionalFilesForAnAnimalBot)
            throws NotValidException, IOException, URISyntaxException {

        if (modName.isBlank()) {
            throw new NotValidException("You must enter a name for your mod.");
        }
        if (newSubtype.isBlank() || !newSubtype.matches("[a-zA-Z_]+")) {
            throw new NotValidException("Your entered subtype for your character is invalid.\nOnly alphabetic characters and the underscore are allowed.");
        }
        if (gender == null) {
            throw new NotValidException("Your entered gender is invalid.");
        }
        if (modExistsAlreadyInModsWorkspace(modName)) {
            throw new NotValidException("A mod with this name already exists in your modding workspace.");
        }

        final Path modRootDir = fileSystemRepository.createDirectoryIn(
                modName,
                Paths.get(configurationsRepository.loadAndValidateConfigurations().getPathToModsWorkspace()));
        final CharacterModCreationInfo creationInfo = new CharacterModCreationInfo(
                modName,
                newSubtype,
                gender,
                createDevDataDir,
                createAdditionalFilesForAnAnimalBot,
                modRootDir);


        createThumbnail(creationInfo);
        createInternalDataSubDir(creationInfo);
        createInternalModelsSubDir(creationInfo);
        createInternalTexturesSubDir(creationInfo);

        sbcFileCreator.createInternalDataSubDir(creationInfo);
        if (createDevDataDir) {
            createDevDataDir(creationInfo);
        }
    }

    private void createInternalDataSubDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getDataInternalKeyDir());
    }

    private boolean modExistsAlreadyInModsWorkspace(String modName) throws NotValidException {
        final Path modsWorkspacePath = Paths.get(configurationsRepository.loadAndValidateConfigurations().getPathToModsWorkspace());
        return Files.exists(modsWorkspacePath.resolve(modName));
    }

    private void createThumbnail(CharacterModCreationInfo creationInfo) throws IOException {
        fileSystemRepository.createJpgWithTextContentInto(creationInfo.getModName(), creationInfo.getModRootDirectory());
    }

    public void createInternalModelsSubDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getModelsInternalKeyDir());
    }

    private void createInternalTexturesSubDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getTexturesInternalKeyDir());
    }

    private void createDevDataDir(CharacterModCreationInfo creationInfo) throws IOException {
        Path devDataDir = fileSystemRepository.createDirectoryIn("_devData", creationInfo.getModRootDirectory());
        if (creationInfo.getGender() == Gender.FEMALE) {
            fileSystemRepository.copyResourceFileInto(
                    getClass().getResource("/seFiles/characterCreation/female/SE_astronaut_female.FBX"),
                    devDataDir);
        } else {
            fileSystemRepository.copyResourceFileInto(
                    getClass().getResource("/seFiles/characterCreation/male/SE_astronaut_male.FBX"),
                    devDataDir);
        }
    }

}
