package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    public void createInternalDataSubDir(CharacterModCreationInfo creationInfo) throws IOException, URISyntaxException {
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

    private void createEntityContainersSbc(CharacterModCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, creationInfo.getInternalKeyName()),
                Map.entry(CHAR_FEMALE_TEMPLATE, creationInfo.getInternalKeyName()));
        if (creationInfo.getGender() == Gender.MALE) {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_EntityContainers.sbc"),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_EntityContainers.sbc"),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        }
    }

    private void createCharactersSbc(CharacterModCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, creationInfo.getInternalKeyName()),
                Map.entry(CHAR_FEMALE_TEMPLATE, creationInfo.getInternalKeyName()));
        if (creationInfo.getGender() == Gender.MALE) {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_Characters.sbc"),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_Characters.sbc"),
                    creationInfo.getDataInternalKeyDir(),
                    replacements);
        }
    }

    private void createStatsSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Stats.sbc"),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createEntityComponentsSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_EntityComponents.sbc"),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createBotsSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Bots.sbc"),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createAIBehaviorSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_AIBehavior.sbc"),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createContainerTypesSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHARACTER_DEFAULT_TEMPLATE, creationInfo.getInternalKeyName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_ContainerTypes.sbc"),
                creationInfo.getDataInternalKeyDir(),
                replacements);
    }

    private void createModifiedSbcFileInto(
            URL templateFileUrl, Path targetDirectory,
            Map<String, String> originalValueToNewValueMap)
            throws IOException, URISyntaxException {

        Path templateFile = Path.of(Objects.requireNonNull(templateFileUrl).toURI());

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
