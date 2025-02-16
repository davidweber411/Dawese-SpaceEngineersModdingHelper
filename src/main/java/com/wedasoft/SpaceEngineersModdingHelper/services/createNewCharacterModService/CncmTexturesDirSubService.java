package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CncmTexturesDirSubService {

    private final FileSystemRepository fileSystemRepository;

    public void createInternalTexturesSubDir(CncmCreationInfo creationInfo) throws IOException {
        fileSystemRepository.createDirectoryIn(
                creationInfo.getInternalKeyName(),
                creationInfo.getTexturesInternalKeyDir());
    }

}
