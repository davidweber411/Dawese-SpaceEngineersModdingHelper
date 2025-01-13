package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final ConfigurationsRepository configurationsRepository;
    private final FileSystemRepository fileSystemRepository;

    public void deployMod(File modToDeploy) throws NotValidException, IOException {
        ConfigurationsEntity configurations = configurationsRepository.loadConfigurations();
        if (configurations == null) {
            throw new NotValidException("You must set the configurations first.");
        }
        if (!new File(configurations.getPathToModsWorkspace()).exists()) {
            throw new NotValidException("Your set path to mods workspace doesn't exist.");
        }
        if (!new File(configurations.getPathToAppdataModsDirectory()).exists()) {
            throw new NotValidException("Your set path to appdata mods directory doesn't exist.");
        }

        deleteCurrentOfflineModIfNeccessary(modToDeploy.toPath());
        copyRelevantFilesAndDirectories(modToDeploy.toPath(), new File(configurations.getPathToAppdataModsDirectory()).toPath());
    }

    private void deleteCurrentOfflineModIfNeccessary(Path modToDeploy) throws IOException {
        final String modName = modToDeploy.getFileName().toString();
        final String appdataModsDirPath = configurationsRepository.loadConfigurations().getPathToAppdataModsDirectory();
        final Path modToDelete = new File(appdataModsDirPath).toPath().resolve(modName);

        if (Files.exists(modToDelete)) {
            fileSystemRepository.deleteDirectory(modToDelete);
        }
    }

    private void copyRelevantFilesAndDirectories(Path dirToCopy, Path intoTargetParentDir) throws IOException {
        final Set<String> allowedNames = Set.of(
                "thumb.jpg", "thumb.png", "modinfo.sbmi", "metadata.mod",
                "Audio", "Brains", "CustomWorlds", "Data", "DataPlatform", "Fonts", "InventoryScenes",
                "Models", "Mods", "Particles", "Scenarios", "ShaderCache", "Shaders", "Textures", "Videos",
                "VisualScripts", "VoxelMaps");

        Path targetModDir = intoTargetParentDir.resolve(dirToCopy.getFileName());
        Files.createDirectories(targetModDir);

        for (File fileOrDir : Objects.requireNonNull(dirToCopy.toFile().listFiles())) {
            if (allowedNames.contains(fileOrDir.getName())) {
                fileSystemRepository.copyFileOrDirectoryInto(fileOrDir, targetModDir);
            }
        }

    }

}
