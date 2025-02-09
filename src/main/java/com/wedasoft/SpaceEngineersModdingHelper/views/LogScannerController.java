package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class LogScannerController {

    private final JfxUiService jfxUiService;
    private final ConfigurationsService configurationsService;

    @FXML
    private BorderPane logScannerBorderPane;
    @FXML
    private TextArea logsTextArea;

    private long lastLogFileReadPosition = 0;

    public void init() {
        Thread t = new Thread(() -> {
            try {
                updateLogsTextArea();
            } catch (IOException e) {
                jfxUiService.displayErrorDialog(e);
            }
        });
        t.setName("Read logs initially thread");
        t.setDaemon(true);
        t.start();
    }

    public void onUpdateLogsButtonClick() throws IOException {
        updateLogsTextArea();
    }

    private Path getCurrentLogFile() {
        final String logsDirectory = configurationsService.loadConfigurations().getPathToAppdataSpaceEngineersDirectory();
        List<File> allLogs = Arrays.stream(Objects.requireNonNull(new File(logsDirectory).listFiles()))
                .filter(File::isFile)
                .filter(f -> f.getName().startsWith("SpaceEngineers_"))
                .filter(f -> f.getName().endsWith(".log"))
                .sorted((o1, o2) -> o2.getName().compareTo(o1.getName()))
                .toList();
        return allLogs.isEmpty() ? null : allLogs.get(0).toPath();
    }

    private void updateLogsTextArea() throws IOException {
        final Path currentLog = getCurrentLogFile();
        if (currentLog == null) {
            jfxUiService.displayWarnDialog("There isn't any log file yet!");
            return;
        }
        long newFileSize = Files.size(currentLog);

        if (newFileSize < lastLogFileReadPosition) {
            lastLogFileReadPosition = 0;
        }

        StringBuilder newLogLines = new StringBuilder();
        try (RandomAccessFile raf = new RandomAccessFile(currentLog.toString(), "r")) {
            raf.seek(lastLogFileReadPosition);
            String line;
            boolean skipLines = lastLogFileReadPosition == 0; // only ignore on the first read attempt

            while ((line = raf.readLine()) != null) {
                if (skipLines) {
                    if (line.contains("Loading duration:")) {
                        skipLines = false;
                    }
                    continue;
                }
                newLogLines.append(line).append("\n");
            }
            lastLogFileReadPosition = raf.getFilePointer();
        }
        if (!newLogLines.isEmpty()) {
            logsTextArea.appendText(newLogLines.toString());
        }
    }

}
