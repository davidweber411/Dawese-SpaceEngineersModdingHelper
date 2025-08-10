package com.wedasoft.SpaceEngineersModdingHelper.services.createNewCharacterModService;

import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SbcFileCreator {

    private static final String CHAR_MALE_TEMPLATE = "CharacterMaleTemplate";
    private static final String CHAR_FEMALE_TEMPLATE = "CharacterFemaleTemplate";
    private static final String UNIQUE_PREFIX_FOR_SBC_FILES = "CharacterDefaultTemplate";

    public void createEntityContainersSbc(CharacterModCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, creationInfo.getModScopeName()),
                Map.entry(CHAR_FEMALE_TEMPLATE, creationInfo.getModScopeName()));
        if (creationInfo.getGender() == Gender.MALE) {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_EntityContainers.sbc"),
                    creationInfo.getDataModScopeDir(),
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_EntityContainers.sbc"),
                    creationInfo.getDataModScopeDir(),
                    replacements);
        }
    }

    public void createCharactersSbc(CharacterModCreationInfo creationInfo) throws IOException, URISyntaxException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(CHAR_MALE_TEMPLATE, creationInfo.getModScopeName()),
                Map.entry(CHAR_FEMALE_TEMPLATE, creationInfo.getModScopeName()));
        if (creationInfo.getGender() == Gender.MALE) {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/male/CharacterMaleTemplate_Characters.sbc"),
                    creationInfo.getDataModScopeDir(),
                    replacements);
        } else {
            createModifiedSbcFileInto(
                    getClass().getResource("/seFiles/characterCreation/female/CharacterFemaleTemplate_Characters.sbc"),
                    creationInfo.getDataModScopeDir(),
                    replacements);
        }
    }

    public void createStatsSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(UNIQUE_PREFIX_FOR_SBC_FILES, creationInfo.getModScopeName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Stats.sbc"),
                creationInfo.getDataModScopeDir(),
                replacements);
    }

    public void createEntityComponentsSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(UNIQUE_PREFIX_FOR_SBC_FILES, creationInfo.getModScopeName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_EntityComponents.sbc"),
                creationInfo.getDataModScopeDir(),
                replacements);
    }

    public void createBotsSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(UNIQUE_PREFIX_FOR_SBC_FILES, creationInfo.getModScopeName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_Bots.sbc"),
                creationInfo.getDataModScopeDir(),
                replacements);
    }

    public void createAIBehaviorSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(UNIQUE_PREFIX_FOR_SBC_FILES, creationInfo.getModScopeName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_AIBehavior.sbc"),
                creationInfo.getDataModScopeDir(),
                replacements);
    }

    public void createContainerTypesSbc(CharacterModCreationInfo creationInfo) throws URISyntaxException, IOException {
        final Map<String, String> replacements = Map.ofEntries(
                Map.entry(UNIQUE_PREFIX_FOR_SBC_FILES, creationInfo.getModScopeName()));
        createModifiedSbcFileInto(
                getClass().getResource("/seFiles/characterCreation/extraFilesForBots/CharacterDefaultTemplate_ContainerTypes.sbc"),
                creationInfo.getDataModScopeDir(),
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
