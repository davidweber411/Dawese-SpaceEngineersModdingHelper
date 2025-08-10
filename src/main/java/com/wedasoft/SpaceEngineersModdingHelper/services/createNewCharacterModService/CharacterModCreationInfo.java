package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.AiBehavior;
import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@RequiredArgsConstructor
public class CharacterModCreationInfo {

    private final String modName;
    private final String newSubtype;
    private final Gender gender;
    private final AiBehavior aiBehavior;
    private final boolean devDataDirShallBeCreated;
    private final boolean createAdditionalFilesForAnAnimalBot;
    private final Path modRootDirectory;

    public String getModScopeName() {
        return newSubtype;
    }

    public Path getDataModScopeDir() {
        return modRootDirectory
                .resolve("Data")
                .resolve(getModScopeName());
    }

    public Path getTexturesModScopeDir() {
        return modRootDirectory
                .resolve("Textures")
                .resolve(getModScopeName());
    }

    public Path getModelsModScopeDir() {
        return modRootDirectory
                .resolve("Models")
                .resolve(getModScopeName());
    }

}

