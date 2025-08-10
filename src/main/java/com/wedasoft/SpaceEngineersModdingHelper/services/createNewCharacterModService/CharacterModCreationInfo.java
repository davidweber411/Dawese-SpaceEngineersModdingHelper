package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@RequiredArgsConstructor
public class CharacterModCreationInfo {

    private final String modName;
    private final String wishedSubtype;
    private final Gender gender;
    private final boolean devDataDirShallBeCreated;
    private final boolean createAdditionalFilesForAnAnimalBot;
    private final Path modRootDirectory;

    public String getInternalKeyName() {
        return wishedSubtype;
    }

    public Path getDataInternalKeyDir() {
        return modRootDirectory
                .resolve("Data")
                .resolve(getInternalKeyName());
    }

    public Path getTexturesInternalKeyDir() {
        return modRootDirectory
                .resolve("Textures")
                .resolve(getInternalKeyName());
    }

    public Path getModelsInternalKeyDir() {
        return modRootDirectory
                .resolve("Models")
                .resolve(getInternalKeyName());
    }

}

