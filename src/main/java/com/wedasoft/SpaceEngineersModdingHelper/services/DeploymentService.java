package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final ConfigurationsRepository configurationsRepository;

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

        Path copiedModPath = copyDirectory(modToDeploy.toPath(), new File(configurations.getPathToAppdataModsDirectory()).toPath());
        deleteUnrelevantThings(copiedModPath);
    }

    private Path copyDirectory(Path dirToCopy, Path intoTargetParentDir) throws IOException {
        Files.walk(dirToCopy).forEach(sourcePath -> {
            try {
                Path targetPath = intoTargetParentDir.resolve(dirToCopy.getFileName()).resolve(dirToCopy.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath);
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error copying file: " + sourcePath, e);
            }
        });
        return intoTargetParentDir.resolve(dirToCopy.getFileName());
    }

    private void deleteUnrelevantThings(Path copiedModPath) throws IOException {
        final Set<String> allowedNames = Set.of(
                "thumb.png", "modinfo.sbmi", "metadata.mod",
                "Audio", "Brains", "CustomWorlds", "Data", "DataPlatform", "Fonts", "InventoryScenes",
                "Models", "Mods", "Particles", "Scenarios", "ShaderCache", "Shaders", "Textures", "Videos",
                "VisualScripts", "VoxelMaps");

        Files.walk(copiedModPath)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        if (!p.equals(copiedModPath)) { // don't delete the root director of the mod
                            if (!allowedNames.contains(p.getFileName().toString())) {
                                if (Files.isRegularFile(p)) {
                                    Files.delete(p);
                                } else if (Files.isDirectory(p)) {
                                    Files.delete(p);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
