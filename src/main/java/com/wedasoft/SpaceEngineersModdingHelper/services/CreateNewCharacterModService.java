package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.data.configurations.ConfigurationsEntity;
import com.wedasoft.SpaceEngineersModdingHelper.enums.Gender;
import com.wedasoft.SpaceEngineersModdingHelper.exceptions.NotValidException;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.ConfigurationsRepository;
import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateNewCharacterModService {

    private final ConfigurationsRepository configurationsRepository;
    private final FileSystemRepository fileSystemRepository;

    private static final List<String> TEMPLATE_MARKERS = List.of("CharacterFemaleTemplate", "CharacterMaleTemplate");

    public void createNewCharacterMod(
            TextField modNameTextField,
            TextField wishedSubtypeTextField,
            Gender gender) throws NotValidException, IOException {

        if (modNameTextField.getText().isBlank()) {
            throw new NotValidException("You must enter a name for your mod.");
        }
        if (wishedSubtypeTextField.getText().isBlank() || !wishedSubtypeTextField.getText().matches("[a-zA-Z_]+")) {
            throw new NotValidException("Your entered subtype for your character is invalid.\nOnly alphabetic characters and the underscore are allowed.");
        }
        if (gender == null) {
            throw new NotValidException("Your entered gender is invalid.");
        }

        final ConfigurationsEntity configurations = configurationsRepository.loadConfigurations();
        if (configurations == null) {
            throw new NotValidException("You must set the configurations first.");
        }
        final Path modsWorkspacePath = Paths.get(configurations.getPathToModsWorkspace());
        if (!Files.exists(modsWorkspacePath)) {
            throw new NotValidException(String.format("""
                    Your configured path to your modding workspace doesn't exist.
                    Your configured path: '%s'""", modsWorkspacePath));
        }

        final Path modDir = fileSystemRepository.createDirectoryIn(modNameTextField.getText(), modsWorkspacePath);
        ImageIO.write(createJpgImageWithText(modNameTextField.getText()), "jpg", modDir.resolve("thumb.jpg").toFile());
        final Path data = fileSystemRepository.createDirectoryIn("Data", modDir);

        final Path models = fileSystemRepository.createDirectoryIn("Models", modDir);

        final Path textures = fileSystemRepository.createDirectoryIn("Textures", modDir);

    }

    private BufferedImage createJpgImageWithText(String text) {
        int width = 800;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();
        int textPosX = width / 2 - textWidth / 2;
        int textPosY = height / 2 - textHeight / 2 + fontMetrics.getAscent();
        g2d.drawString(text, textPosX, textPosY);

        g2d.dispose();
        return image;
    }

}
