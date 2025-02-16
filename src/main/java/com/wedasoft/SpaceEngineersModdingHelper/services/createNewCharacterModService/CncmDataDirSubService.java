package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
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

    public void createInternalDataSubDir(CncmCreationInfo creationInfo) throws IOException, URISyntaxException {
        Files.createDirectories(creationInfo.getDataInternalKeyDir());

        createEntityContainersSbc(creationInfo);
        createCharactersSbc(creationInfo);

        if (creationInfo.isCreateAdditionalFilesForAnAnimalBot()) {
            createStatsSbc(creationInfo);
            createEntityComponentsSbc(creationInfo);
            createBotsSbc(creationInfo);
            createAIBehaviorSbc(creationInfo);
            createContainerTypesSbc(creationInfo);
        }
    }

    private void createEntityContainersSbc(CncmCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, creationInfo.getInternalKeyName()),
                Map.entry(CHAR_FEMALE_TEMPLATE, creationInfo.getInternalKeyName()));
        if (creationInfo.getGender() == Gender.MALE) {
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_EntityContainers.sbc")).toURI()),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_EntityContainers.sbc")).toURI()),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        }
    }

    private void createCharactersSbc(CncmCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, creationInfo.getInternalKeyName()),
                Map.entry(CHAR_FEMALE_TEMPLATE, creationInfo.getInternalKeyName()));
        if (creationInfo.getGender() == Gender.MALE) {
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_EntityContainers.sbc")).toURI()),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_EntityContainers.sbc")).toURI()),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        }
    }

    private void createStatsSbc(CncmCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Stats.sbc")).toURI()),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createEntityComponentsSbc(CncmCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_EntityComponents.sbc")).toURI()),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createBotsSbc(CncmCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Bots.sbc")).toURI()),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createAIBehaviorSbc(CncmCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_AIBehavior.sbc")).toURI()),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createContainerTypesSbc(CncmCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                Path.of(Objects.requireNonNull(getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_ContainerTypes.sbc")).toURI()),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createModifiedSbcFileInto(
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
