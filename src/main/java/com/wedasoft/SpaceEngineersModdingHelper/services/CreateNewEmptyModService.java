package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.helper.CommandLineHelper;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CreateNewEmptyModService {

    private final ConfigurationsRepository configurationsRepository;
    private final FileSystemRepository fileSystemRepository;

    public void createNewEmptyMod(String modname) throws NotValidException, IOException {
        if (modname.isBlank()) {
            throw new NotValidException("You must enter a name for your mod.");
        }
        if (modExistsAlreadyInModsWorkspace(modname)) {
            throw new NotValidException("A mod with this name already exists in your modding workspace.");
        }

        final Path modRootDir = createDirectoryStructure(modname);
        createDotnetProjectForSpaceEngineers(modRootDir, modname);
    }

    private void createDotnetProjectForSpaceEngineers(Path modRootDir, String modname) throws NotValidException {
        try {
            if (!CommandLineHelper.runCommandAndWait(
                    modRootDir.getParent(),
                    "dotnet", "new", "classlib", "-n", modname)) {
                throw new Exception("Dotnet project couldn't be created.");
            }

        } catch (Exception e) {
            throw new NotValidException(e.getMessage(), e);
        }
    }

    private Path createDirectoryStructure(String modname) throws IOException, NotValidException {
        final ConfigurationsEntity configurations = configurationsRepository.loadAndValidateConfigurations();

        Path modRootDir = fileSystemRepository.createDirectoryIn(modname, Paths.get(configurations.getPathToModsWorkspace()));
        fileSystemRepository.createJpgWithTextContentInto(modname, modRootDir);
        fileSystemRepository.createDirectoryIn("_devData", modRootDir);
        fileSystemRepository.createDirectoryIn("Models", modRootDir);
        fileSystemRepository.createDirectoryIn("Textures", modRootDir);
        Path dataDir = fileSystemRepository.createDirectoryIn("Data", modRootDir);
        Path scriptsDir = fileSystemRepository.createDirectoryIn("Scripts", dataDir);
        fileSystemRepository.createDirectoryIn(modname, scriptsDir);
        return modRootDir;
    }

    private boolean modExistsAlreadyInModsWorkspace(String modName) throws NotValidException {
        final Path modsWorkspacePath = Paths.get(configurationsRepository.loadAndValidateConfigurations().getPathToModsWorkspace());
        return Files.exists(modsWorkspacePath.resolve(modName));
    }

}
