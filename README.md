# DoorDash Monsters Inc. Board Game

This is a JavaFX board game project for CSEN401. The game uses Java source files, CSV data files, image assets, sound assets, and JavaFX media support.

## Project Location

Example project path:

```txt
DoorDash
```

All commands below should be run from this folder unless stated otherwise.

In PowerShell:

```powershell
cd "DoorDash"
```

## Required Software

Install:

- Java JDK 21
- JavaFX SDK 21.0.11

Example JDK path:

```txt
jdk-21\
```

JavaFX is expected at:

```txt
DoorDash\lib\javafx-sdk-21.0.11\
```

The JavaFX `lib` folder should therefore be:

```txt
DoorDash\lib\javafx-sdk-21.0.11\lib
```

## Required Project Files

The project root must contain these files and folders:

```txt
DoorDash/
  src/
  cards.csv
  cells.csv
  monsters.csv
```

The `src/game/assets/` folder must stay inside `src` because the game loads images, sounds, fonts, CSS files, and FXML files from there.

## Files Not Required in GitHub Submission

These files/folders are generated or local-only and should not be committed:

```txt
bin/
lib/
.vscode/
.settings/
.classpath
.project
```

They are already listed in `.gitignore`.

## JavaFX Download

If `lib/javafx-sdk-21.0.11` is missing, download JavaFX SDK 21 from:

```txt
https://openjfx.io/
```

Extract it so the folder structure becomes:

```txt
\DoorDash\lib\javafx-sdk-21.0.11\lib
```

If the folder name is different, either rename it to `javafx-sdk-21.0.11` or update the commands below.

## Compile the Project

Run this command from the project root:

```powershell
javac --module-path "\DoorDash\lib\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -cp "\DoorDash\src" -d "\DoorDash\bin" (Get-ChildItem -Recurse -Filter *.java "\DoorDash\src" | ForEach-Object { $_.FullName })
```

This creates compiled `.class` files in:

```txt
\DoorDash\bin
```

## Copy Runtime Assets

After compiling, copy the assets and FXML file into `bin`:

```powershell
Copy-Item -Path "\DoorDash\src\game\assets" -Destination "\DoorDash\bin\game" -Recurse -Force
```

```powershell
Copy-Item -Path "\DoorDash\src\game\view\FullscreenWarningView.fxml" -Destination "\DoorDash\bin\game\view" -Force
```

This is important because the game uses:

- images
- sounds
- CSS files
- fonts
- FXML files

## Run the Game

Run this command from the project root:

```powershell
java --module-path "\DoorDash\lib\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -cp "\DoorDash\bin;\DoorDash" game.view.Main
```

The project root is included in the classpath because the game reads the CSV files directly:

```txt
cards.csv
cells.csv
monsters.csv
```

## Quick Run Commands

If you are already inside:

```txt
\DoorDash
```

you can use the shorter commands:

```powershell
javac --module-path lib\javafx-sdk-21.0.11\lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -cp src -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

```powershell
Copy-Item -Path src\game\assets -Destination bin\game -Recurse -Force
Copy-Item -Path src\game\view\FullscreenWarningView.fxml -Destination bin\game\view -Force
```

```powershell
java --module-path lib\javafx-sdk-21.0.11\lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -cp "bin;." game.view.Main
```

## Running in VS Code

If running from VS Code, make sure the launch configuration includes these VM arguments:

```txt
--module-path "\DoorDash\lib\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media
```

Also make sure the working directory is:

```txt
\DoorDash
```

The working directory matters because the CSV files are read from the project root.

## Common Errors and Fixes

### Error: JavaFX runtime components are missing

This means the run command does not include the JavaFX module path.

Fix it by using:

```powershell
--module-path "\DoorDash\lib\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media
```

### Error: javafx.scene.media.MediaPlayer not found

This means `javafx.media` is missing.

Make sure both compile and run commands include:

```txt
javafx.media
```

### Error: Music not found

Make sure assets were copied:

```powershell
Copy-Item -Path src\game\assets -Destination bin\game -Recurse -Force
```

Also make sure this folder exists:

```txt
\DoorDash\src\game\assets\soundTrack
```

### Error: FullscreenWarningView.fxml not found

Copy the FXML file:

```powershell
Copy-Item -Path src\game\view\FullscreenWarningView.fxml -Destination bin\game\view -Force
```

### CSV files not found

Run the game from the project root:

```txt
\DoorDash
```

The following files must be beside the command:

```txt
cards.csv
cells.csv
monsters.csv
```

## Themes

The game includes three themes:

- Monstropolis
- Vice City
- Giza

Theme assets are stored under:

```txt
src\game\assets\
src\game\assets\retro\
src\game\assets\ancientEgypt\
```

## Main Class

The application starts from:

```txt
game.view.Main
```

Source file:

```txt
\DoorDash\src\game\view\Main.java
```
