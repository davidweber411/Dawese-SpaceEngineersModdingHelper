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

        /* create thumbnail */
        createThumbnail(creationInfo);

        /* create directories only */
        createDataModScopeDir(creationInfo);
        createModelsModScopeDir(creationInfo);
        createTexturesModScopeDir(creationInfo);
        if (createDevDataDir) {
            createDevDataDir(creationInfo);
        }

        /* create sbc files only */
        sbcFileCreator.createEntityContainersSbc(creationInfo);
        sbcFileCreator.createCharactersSbc(creationInfo);
        if (creationInfo.isCreateAdditionalFilesForAnAnimalBot()) {
            sbcFileCreator.createStatsSbc(creationInfo);
            sbcFileCreator.createEntityComponentsSbc(creationInfo);
            sbcFileCreator.createBotsSbc(creationInfo);
            sbcFileCreator.createAIBehaviorSbc(creationInfo);
            sbcFileCreator.createContainerTypesSbc(creationInfo);
        }
    }

    private void createDataModScopeDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getDataModScopeDir());
    }

    private boolean modExistsAlreadyInModsWorkspace(String modName) throws NotValidException {
        final Path modsWorkspacePath = Paths.get(configurationsRepository.loadAndValidateConfigurations().getPathToModsWorkspace());
        return Files.exists(modsWorkspacePath.resolve(modName));
    }

    private void createThumbnail(CharacterModCreationInfo creationInfo) throws IOException {
        fileSystemRepository.createJpgWithTextContentInto(creationInfo.getModName(), creationInfo.getModRootDirectory());
    }

    public void createModelsModScopeDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getModelsModScopeDir());
    }

    private void createTexturesModScopeDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getTexturesModScopeDir());
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
