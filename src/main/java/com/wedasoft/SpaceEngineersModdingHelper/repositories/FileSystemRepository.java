package com.wedasoft.SpaceEngineersModdingHelper.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileSystemRepository {

    public Path createDirectoryIn(String directoryName, Path parentDir) throws IOException {
        Files.createDirectories(parentDir);
        Path newDir = parentDir.resolve(directoryName);
        Files.createDirectory(newDir);
        return newDir;
    }

}
