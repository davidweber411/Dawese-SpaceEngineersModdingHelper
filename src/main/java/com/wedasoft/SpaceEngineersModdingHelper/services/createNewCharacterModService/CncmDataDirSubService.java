package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CncmDataDirSubService {

    private static final String CHAR_MALE_TEMPLATE = "CharacterMaleTemplate";
    private static final String CHAR_FEMALE_TEMPLATE = "CharacterFemaleTemplate";
    private static final String CHARACTER_DEFAULT_TEMPLATE = "CharacterDefaultTemplate";

    private final FileSystemRepository fileSystemRepository;

    public void createInternalDataSubDir(CncmCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Path dataDir = fileSystemRepository.createDirectoryIn("Data", creationInfo.getModRootDirectory());
        final Path internalNameSubdir = fileSystemRepository.createDirectoryIn(creationInfo.getInternalKeyName(), dataDir);

        createCharactersSbcAndEntityContainersSbc(creationInfo.getGender(), creationInfo.getInternalKeyName(), internalNameSubdir);
        if (creationInfo.isCreateAdditionalFilesForAnAnimalBot()) {
            createAdditionalFilesForAnAnimalBot(creationInfo.getInternalKeyName(), internalNameSubdir);
        }
    }

    private void createCharactersSbcAndEntityContainersSbc(
            Gender gender, String internalNameForReplacements, Path targetDir)
            throws IOException, URISyntaxException {

        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, internalNameForReplacements),
                Map.entry(CHAR_FEMALE_TEMPLATE, internalNameForReplacements));
        if (gender == Gender.MALE) {
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_EntityContainers.sbc")).toURI()),
                    targetDir,
                    replacements);
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_Characters.sbc")).toURI()),
                    targetDir,
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_EntityContainers.sbc")).toURI()),
                    targetDir,
                    replacements);
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_Characters.sbc")).toURI()),
                    targetDir,
                    replacements);
        }
    }

    private void createAdditionalFilesForAnAnimalBot(
            final String internalNameForReplacements, Path targetDir)
            throws URISyntaxException, IOException {

        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, internalNameForReplacements));

        // create Stats.sbc
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Stats.sbc")).toURI()),
                targetDir,
                replacements);

        // create EntityComponents.sbc
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_EntityComponents.sbc")).toURI()),
                targetDir,
                replacements);

        // create Bots.sbc
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Bots.sbc")).toURI()),
                targetDir,
                replacements);

        // create AIBehavior.sbc
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_AIBehavior.sbc")).toURI()),
                targetDir,
                replacements);

        // create ContainerTypes.sbc
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_ContainerTypes.sbc")).toURI()),
                targetDir,
                replacements);
    }

    public void createModifiedSbcFileInto(
            Path templateFile,
            Path targetDirectory,
            Map<String, String> originalValueToNewValueMap) throws IOException {
        String modifiedContent = Files.readString(templateFile);
        for (Map.Entry<String, String> oldToNewEntry : originalValueToNewValueMap.entrySet()) {
            modifiedContent = modifiedContent.replaceAll(oldToNewEntry.getKey(), oldToNewEntry.getValue());
        }

        String modifiedFileName = templateFile.getFileName().toString();
        for (Map.Entry<String, String> oldToNewEntry : originalValueToNewValueMap.entrySet()) {
            modifiedFileName = modifiedFileName.replaceAll(oldToNewEntry.getKey(), oldToNewEntry.getValue());
        }

        Files.writeString(targetDirectory.resolve(modifiedFileName), modifiedContent);
    }

}
