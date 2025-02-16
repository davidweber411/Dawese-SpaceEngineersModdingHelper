package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CncmDevDataDirSubService {

    private final FileSystemRepository fileSystemRepository;

    public void createDevDataDir(CncmCreationInfo creationInfo) throws IOException {
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
