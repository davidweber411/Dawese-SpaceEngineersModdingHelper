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
        final ConfigurationsEntity configurations = configurationsRepository.loadAndValidateConfigurations();
        Path modRootDir = fileSystemRepository.createDirectoryIn(modname, Paths.get(configurations.getPathToModsWorkspace()));

        createDirectoryStructure(modRootDir, modname);
        addAdditionFilesForIde(modRootDir);
        createDotnetProjectForSpaceEngineers(modRootDir, modname);
        moveClassOneIntoScriptsDirectory(modRootDir, modname);
        createGitProject(modRootDir);
    }

    private void addAdditionFilesForIde(Path modRootDir) throws IOException {
        fileSystemRepository.createFileFromResource(getClass().getResourceAsStream("/newProjectFiles/.editorconfig"), modRootDir.resolve(".editorconfig"));
        fileSystemRepository.createFileFromResource(getClass().getResourceAsStream("/newProjectFiles/gitignore"), modRootDir.resolve(".gitignore"));
    }

    private void moveClassOneIntoScriptsDirectory(Path modRootDir, String modname) throws IOException {
        final String filename = "Class1.cs";
        Files.move(
                modRootDir.resolve(filename),
                modRootDir.resolve("Data").resolve("Scripts").resolve(modname).resolve(filename));
    }

    private void createGitProject(Path modRootDir) throws NotValidException {
        try {
            if (!CommandLineHelper.runCommandAndWait(modRootDir, "git", "init")
                || !CommandLineHelper.runCommandAndWait(modRootDir, "git", "add", ".")
                || !CommandLineHelper.runCommandAndWait(modRootDir, "git", "commit", "-m", "Initial commit")) {
                throw new Exception("Git project couldn't be created.");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            JfxDialogUtil.createErrorDialog("Error creating the git project!", e);
        }
    }

    private void createDotnetProjectForSpaceEngineers(Path modRootDir, String modname) throws NotValidException {
        try {
            if (!CommandLineHelper.runCommandAndWait(
                    modRootDir.getParent(),
                    "dotnet", "new", "classlib", "-n", modname)) {
                throw new Exception("Dotnet project couldn't be created.");
            }
            Files.deleteIfExists(modRootDir.resolve(modname + ".csproj"));
            Path csproj = fileSystemRepository.createFileFromResource(getClass().getResourceAsStream("/newProjectFiles/modname.csproj"), modRootDir.resolve(modname + ".csproj"));
            fileSystemRepository.replaceInFileContent(csproj, "[[MOD_NAME]]", modname.replace(" ", "_"));
            fileSystemRepository.replaceInFileContent(csproj, "[[SPACE_ENGINEERS_BIN_64]]", "C:\\Program Files (x86)\\Steam\\steamapps\\common\\SpaceEngineers\\Bin64");
        } catch (Exception e) {
            e.printStackTrace();
//            JfxDialogUtil.createErrorDialog("Error creating the dotnet project!", e);
        }
    }

    private void createDirectoryStructure(Path modRootDir, String modname) throws IOException {
        fileSystemRepository.createJpgWithTextContentInto(modname, modRootDir);
        fileSystemRepository.createDirectoryIn("_devData", modRootDir);
        fileSystemRepository.createDirectoryIn("Models", modRootDir);
        fileSystemRepository.createDirectoryIn("Textures", modRootDir);
        Path dataDir = fileSystemRepository.createDirectoryIn("Data", modRootDir);
        Path scriptsDir = fileSystemRepository.createDirectoryIn("Scripts", dataDir);
        fileSystemRepository.createDirectoryIn(modname, scriptsDir);
    }

    private boolean modExistsAlreadyInModsWorkspace(String modName) throws NotValidException {
        final Path modsWorkspacePath = Paths.get(configurationsRepository.loadAndValidateConfigurations().getPathToModsWorkspace());
        return Files.exists(modsWorkspacePath.resolve(modName));
    }

}
