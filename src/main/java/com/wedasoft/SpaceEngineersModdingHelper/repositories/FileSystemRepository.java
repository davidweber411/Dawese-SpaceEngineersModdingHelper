package com.wedasoft.SpaceEngineersModdingHelper.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class FileSystemRepository {

    public long getSizeInBytes(Path fileOrDirectory) throws IOException {
        final long[] size = {0};
        Files.walkFileTree(fileOrDirectory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size[0] += attrs.size();
                return FileVisitResult.CONTINUE;
            }
        });
        return size[0];
    }

    public Path createDirectoryIn(String directoryName, Path parentDir) throws IOException {
        Files.createDirectories(parentDir);
        Path newDir = parentDir.resolve(directoryName);
        Files.createDirectory(newDir);
        return newDir;
    }

    public void deleteDirectory(Path modToDelete) throws IOException {
        try (Stream<Path> paths = Files.walk(modToDelete)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    if (Files.isRegularFile(path)) {
                        Files.delete(path);
                    } else if (Files.isDirectory(path)) {
                        Files.delete(path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void copyFileOrDirectoryInto(File fileOrDir, Path targetDir) throws IOException {
        if (fileOrDir.isDirectory()) {
            copyDirectoryInto(fileOrDir.toPath(), targetDir);
        } else if (fileOrDir.isFile()) {
            Files.copy(fileOrDir.toPath(), targetDir.resolve(fileOrDir.getName()));
        }
    }

    private void copyDirectoryInto(Path dirToCopy, Path targetDir) throws IOException {
        try (Stream<Path> paths = Files.walk(dirToCopy)) {
            paths.forEach(path -> {
                try {
                    Path targetPath = targetDir.resolve(dirToCopy.getFileName()).resolve(dirToCopy.relativize(path));
                    if (Files.isDirectory(path)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public Path createFileFromResource(InputStream resourceFile, Path targetPathInclFile) throws IOException {
        if (resourceFile == null) {
            throw new FileNotFoundException("File not found in resource directory!");
        }

        Files.createDirectories(targetPathInclFile.getParent());

        try (OutputStream outputStream = new FileOutputStream(targetPathInclFile.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = resourceFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            resourceFile.close();
        }
        return targetPathInclFile;
    }

    public Path replaceInFileContent(Path file, String patternToFind, String replacement) throws IOException {
        Files.writeString(file, Files.readString(file).replace(patternToFind, replacement));
        return file;
    }

    public Path copyResourceFileInto(URL resourceFileUrl, Path targetDir) throws IOException {
        File file = getTempFileFromURL(resourceFileUrl);
        Path copiedFile = Files.copy(file.toPath(), targetDir.resolve(file.getName()));
        String fileName = new File(resourceFileUrl.getPath()).getName();
        copiedFile.toFile().renameTo(new File(copiedFile.toFile().getParent(), fileName));
        return copiedFile;
    }

    private File getTempFileFromURL(URL resourceUrl) throws IOException {
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL cannot be null");
        }

        String fileName = new File(resourceUrl.getPath()).getName();
        int dotIndex = fileName.lastIndexOf('.');
        String namePart = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? ".tmp" : fileName.substring(dotIndex);

        File tempFile = File.createTempFile(namePart, extension);

        try (InputStream inputStream = resourceUrl.openStream()) {
            if (inputStream == null) {
                throw new FileNotFoundException("Unable to open stream for: " + resourceUrl);
            }
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }

    public void createJpgWithTextContentInto(String text, Path modDir) throws IOException {
        ImageIO.write(createJpgImageWithText(text), "jpg", modDir.resolve("thumb.jpg").toFile());
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
