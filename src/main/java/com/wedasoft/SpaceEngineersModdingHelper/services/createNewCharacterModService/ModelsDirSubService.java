package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ModelsDirSubService {

    public void createInternalModelsSubDir(CharacterModCreationInfo creationInfo) throws IOException {
        Files.createDirectories(creationInfo.getModelsInternalKeyDir());
    }

}
