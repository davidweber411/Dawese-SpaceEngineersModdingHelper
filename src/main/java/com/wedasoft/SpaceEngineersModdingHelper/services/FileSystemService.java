package com.wedasoft.SpaceEngineersModdingHelper.services;

import com.wedasoft.SpaceEngineersModdingHelper.repositories.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileSystemService {

    private final FileSystemRepository fileSystemRepository;

    public long getSizeInBytes(Path fileOrDirectory) throws IOException {
        return fileSystemRepository.getSizeInBytes(fileOrDirectory);
    }

}
