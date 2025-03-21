package com.wedasoft.SpaceEngineersModdingHelper;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class JfxSpringBootAppLauncher {

    public static final String LOG_PREFIX = "JavaFx Spring Boot lifecycle: ";
    public static final String SPLASHSCREEN_CLASS_PATH = "com.wedasoft.SpaceEngineersModdingHelper.SplashScreenPreloader";

    public static void main(String[] args) {
        log.info(LOG_PREFIX + "Starting application via main method...");

        log.info(LOG_PREFIX + "Set full qualified class name as system property for defining the splash screen preloader.");
        System.setProperty("javafx.preloader", SPLASHSCREEN_CLASS_PATH);

        log.info(LOG_PREFIX + "Launching the JavaFx application.");
        Application.launch(JfxSpringBootApp.class, args);
    }

    public static class JfxSpringBootApp extends Application {

        private static ConfigurableApplicationContext springApplicationContext;

        @Override
        public void init() {
            log.info(LOG_PREFIX + "Executing overridden 'init()' of JavaFx Application...");
            log.info(LOG_PREFIX + "Creating the spring application context.");
            try {
                springApplicationContext = new SpringApplicationBuilder()
                        .sources(JfxSpringBootAppLauncher.class)
                        .initializers((ApplicationContextInitializer<GenericApplicationContext>) applicationContext -> {
                            applicationContext.registerBean(Application.class, () -> this);
                            applicationContext.registerBean(Parameters.class, this::getParameters);
                            applicationContext.registerBean(HostServices.class, this::getHostServices);
                        })
                        .run(getParameters().getRaw().toArray(new String[0]));
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                throw new RuntimeException(t);
            }
        }

        @Override
        public void start(Stage primaryStage) {
            log.info(LOG_PREFIX + "Executing overridden 'start()' of JavaFx Application...");
            log.info(LOG_PREFIX + "Publishing event 'JfxApplicationStartEvent'.");
            try {
                springApplicationContext.publishEvent(new JfxApplicationStartEvent(primaryStage));
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                throw new RuntimeException(t);
            }
        }

        @Override
        public void stop() {
            log.info(LOG_PREFIX + "Executing overridden 'stop()' of JavaFx Application...");
            springApplicationContext.close();
            Platform.exit();
            System.exit(0);
        }

    }

    @Component
    public static class JfxApplicationStartEventListener implements ApplicationListener<JfxApplicationStartEvent> {

        @Value("${spring.application.name}")
        private final String applicationTitle;
        @Value("${app.version}")
        private String appVersion;
        private final ApplicationContext springApplicationContext;

        public JfxApplicationStartEventListener(
                @Value("${spring.application.name}") String applicationTitle,
                ApplicationContext springApplicationContext) {

            this.applicationTitle = applicationTitle;
            this.springApplicationContext = springApplicationContext;
        }

        @Override
        public void onApplicationEvent(JfxApplicationStartEvent event) {
            try {
                log.info(LOG_PREFIX + "Computing 'JfxApplicationStartEvent'...");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/wedasoft/SpaceEngineersModdingHelper/views/ui.fxml"));
                fxmlLoader.setControllerFactory(springApplicationContext::getBean);
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = event.getStage();
                stage.setScene(scene);
                stage.setTitle(this.applicationTitle + " " + appVersion);
                stage.show();
                log.info(LOG_PREFIX + "JavaFx Spring boot application started.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (SplashScreenPreloader.stage != null) {
                log.info(LOG_PREFIX + "Closing splash screen.");
                SplashScreenPreloader.stage.close();
            }
        }

    }

    public static class JfxApplicationStartEvent extends ApplicationEvent {
        public Stage getStage() {
            return (Stage) getSource();
        }

        public JfxApplicationStartEvent(Stage source) {
            super(source);
        }
    }

}
