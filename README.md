### Description

This application is used to simplify several steps when modding the game "Space Engineers".

Please backup your files before using it for security.

### Features: Run and package

| Feature                                                 | Additional informations                                                                                                                                                                                    |
|---------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Dashboard                                               | Helps you to setup your environment.                                                                                                                                                                       |
| Deploy mods from your working dir into the SE mods dir. | Select the mod you want to deploy. It just includes all directories which are present in the "SpaceEngineers/Content" directory and the files "thumb.jpg", "thumb.png", "modinfo.sbmi" and "metadata.mod". |
| Create a template mod for a new character.              | Creates a complete mod structure. Includes configurable subtype id and gender, preconfigured SBC files and dev files creation (FBX, XML) in your mod directory.                                            |

### Used technologies

| Technology    | Version              |
|---------------|----------------------|
| Spring Boot   | 3.3.1                |
| Java          | 17                   |
| JavaFX        | 17.0.8               |
| JDK           | Open JDK 17.0.2      |
| Gradle        | 8.8 (Gradle Wrapper) |
| Module system | Non modular          |

### Todos

create git repo checkbox

    git add *
    git commit -a -m "initial commit"