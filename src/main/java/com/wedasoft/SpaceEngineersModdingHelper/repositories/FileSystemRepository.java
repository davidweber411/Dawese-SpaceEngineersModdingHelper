package com.wedasoft.SpaceEngineersModdingHelper.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileSystemRepository {

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

}
