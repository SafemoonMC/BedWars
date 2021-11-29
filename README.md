# BedWars

BedWars is a strategic game mode where players must carefully defend their own bed, whilst bravely
attacking and destroying other players' beds!
It can be played in Solo, Duos, Trios or even Quads.

## Building

BedWars uses Gradle to handle dependencies and building.

**Requirements:**

- Java 16 JDK
- Git

**Compiling from source:**

```sh
git clone https://github.com/SafemoonMC/BedWars.git
cd BedWars/
./gradlew buildAll
```

You can find the output artifacts in the `/COMPILED_JARS` directory.

**Other Gradle custom tasks:**

- **buildBase**, it gets just the jar without any dependency in;
- **buildSources**, it gets just the jar with project files in;
- **buildJavadoc**, it gets just the final JavaDoc;
- **buildShadowjar**, it gets just the final jar with all necessary dependencies;

## Contributing

BedWars follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
Generally, you can import the style from the `java-google-style.xml` file you can find at the root of
the project.

#### Project Layout

The project is split up into a few separate modules:

- **Common**, the common module contains most of the code which implements the respective BedWars
  plugins. This abstract module reduces duplicated code throughout the project.
- **Lobby**, the lobby module implements BedWars lobby & matchmaking functionalities;
- **Game**, the game module implements BedWars game functionality in all its aspects;