package com.wedasoft.SpaceEngineersModdingHelper.views;

import com.wedasoft.SpaceEngineersModdingHelper.services.ConfigurationsService;
import com.wedasoft.SpaceEngineersModdingHelper.services.JfxUiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

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
    private ScheduledExecutorService autoUpdateExecutorService;

    public void init() {
        logScannerBorderPane.parentProperty().addListener((observable, oldParent, newParent) -> {
            if (newParent == null) {
                System.out.println("Node has been removed from the scene or layout");
                if (autoUpdateExecutorService != null) {
                    System.out.println("Shutdown executor service now...");
                    autoUpdateExecutorService.shutdown();
                }
            }
        });
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

    public void onActivateAutoUpdateButtonClick(ActionEvent event) {
        ToggleButton toggleButton = (ToggleButton) event.getSource();
        if (toggleButton.isSelected()) {
            autoUpdateExecutorService = Executors.newScheduledThreadPool(1);
            autoUpdateExecutorService.scheduleAtFixedRate(() -> {
                try {
                    updateLogsTextArea();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, 0, 5, SECONDS);
        } else {
            System.out.println("Shutdown executor service now...");
            autoUpdateExecutorService.shutdown();
        }
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
            lastLogFileReadPosition = 0; // read complete file again if it's smaller than before.
        }

        StringBuilder newLogLines = new StringBuilder();
        try (RandomAccessFile raf = new RandomAccessFile(currentLog.toString(), "r")) {
            // find the last "Loading duration:" position when the file is read the first time
            if (lastLogFileReadPosition == 0) {
                long lastLoadingDurationPos = -1;
                String lastLoadingDurationLine = "";
                String line;
                while ((line = raf.readLine()) != null) {
                    if (line.contains("Loading duration:")) {
                        lastLoadingDurationPos = raf.getFilePointer();
                        lastLoadingDurationLine = line;
                    }
                }
                if (lastLoadingDurationPos != -1) {
                    lastLogFileReadPosition = lastLoadingDurationPos;
                    newLogLines.append(lastLoadingDurationLine).append("\n");
                }
            }

            // append lines
            raf.seek(lastLogFileReadPosition);
            String line;
            while ((line = raf.readLine()) != null) {
                newLogLines.append(line).append("\n");
            }
            lastLogFileReadPosition = raf.getFilePointer();
        }
        if (!newLogLines.isEmpty()) {
            logsTextArea.appendText(newLogLines.toString());
        }
    }

}
