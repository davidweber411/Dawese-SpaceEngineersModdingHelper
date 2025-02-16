package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CncmTexturesDirSubService {

    private final FileSystemRepository fileSystemRepository;

    public void createInternalTexturesSubDir(CncmCreationInfo creationInfo) throws IOException {
        final Path textures = fileSystemRepository.createDirectoryIn("Textures", creationInfo.getModRootDirectory());
        fileSystemRepository.createDirectoryIn(creationInfo.getInternalKeyName(), textures);
    }

}
