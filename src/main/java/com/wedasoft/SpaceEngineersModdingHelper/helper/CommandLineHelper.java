package com.wedasoft.SpaceEngineersModdingHelper.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class CommandLineHelper {

    public static boolean runCommandAndWait(Path workingDir, String... processBuilderCommand) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(processBuilderCommand);
        processBuilder.directory(workingDir.toFile());
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // debug logging
                }
            }
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            throw new Exception(e);
        }
    }

}
