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

    private final DevDataDirSubService devDataDirSubService;
    private final ModelsDirSubService modelsDirSubService;
    private final DataDirSubService dataDirSubService;

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

        dataDirSubService.createInternalDataSubDir(creationInfo);
        modelsDirSubService.createInternalModelsSubDir(creationInfo);
        createInternalTexturesSubDir(creationInfo);
        if (createDevDataDir) {
            devDataDirSubService.createDevDataDir(creationInfo);
        }
    }

    private boolean modExistsAlreadyInModsWorkspace(String modName) throws NotValidException {
        final Path modsWorkspacePath = Paths.get(configurationsRepository.loadAndValidateConfigurations().getPathToModsWorkspace());
        return Files.exists(modsWorkspacePath.resolve(modName));
    }

    private void createThumbnail(CharacterModCreationInfo creationInfo) throws IOException {
        fileSystemRepository.createJpgWithTextContentInto(creationInfo.getModName(), creationInfo.getModRootDirectory());
    }

    private void createInternalTexturesSubDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getTexturesInternalKeyDir());
    }

}
