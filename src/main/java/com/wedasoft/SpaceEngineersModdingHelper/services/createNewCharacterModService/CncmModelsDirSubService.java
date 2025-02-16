package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CncmModelsDirSubService {

    private final FileSystemRepository fileSystemRepository;

    public void createInternalModelsSubDir(CncmCreationInfo creationInfo) throws IOException {
        final Path models = fileSystemRepository.createDirectoryIn("Models", creationInfo.getModRootDirectory());
        fileSystemRepository.createDirectoryIn(creationInfo.getInternalKeyName(), models);
    }

}
