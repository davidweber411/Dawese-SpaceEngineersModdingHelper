package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@RequiredArgsConstructor
public class CncmCreationInfo {

    private final String modName;
    private final String wishedSubtype;
    private final Gender gender;
    private final boolean devDataDirShallBeCreated;
    private final boolean createAdditionalFilesForAnAnimalBot;
    private final Path modRootDirectory;

    public String getInternalKeyName() {
        return wishedSubtype;
    }

}

