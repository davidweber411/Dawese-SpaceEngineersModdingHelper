package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CncmThumbnailSubService {

    private final FileSystemRepository fileSystemRepository;

    public void createThumbnail(CharacterModCreationInfo creationInfo) throws IOException {
        fileSystemRepository.createJpgWithTextContentInto(creationInfo.getModName(), creationInfo.getModRootDirectory());
    }

}
