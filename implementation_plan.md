# DoorDasH: Milestone 3 GUI Implementation Plan

Based on the milestone requirements and the current codebase state, here is the detailed plan of what is left to implement for the GUI.

## User Review Required
> [!IMPORTANT]
> Please review this plan. As requested, **no code has been edited**. Once you approve this plan, I can begin executing these tasks.

## Open Questions
- Do you have specific UI preferences (e.g., specific colors, animations, or popup styles) for the Game Over screen or Alert dialogs?
- How would you like the "How To Play" instructions displayed (e.g., a popup, a separate scene)?

## Proposed Changes (What is left to do)

### 1. Navigation & Main Menu Actions
The Main Menu buttons currently lack action handlers, and there is no way to switch between screens.
- **`NavigationManager.java`**: Implement scene switching logic to manage transitions between Main Menu, Choose Side, Game, and Game Over scenes.
- **`MainMenuView.java`**: Attach `setOnAction` handlers to:
  - **Start Game**: Navigate to `ChooseSideView`.
  - **How To Play**: Display game instructions (either in a popup or a new scene).

### 2. Side Selection (ChooseSideView)
`ChooseSideView.java` is currently empty.
- **`ChooseSideView.java`**: 
  - Build a scene displaying the two sides: **SCARER** and **LAUGHER**.
  - Allow the player to choose a side and initiate the `Game` engine.
  - Transition to `GameView` after selection.

### 3. Game Interaction Controls (GameView)
`GameView.java` currently displays stats but lacks player interaction buttons.
- **`GameView.java`**:
  - Add a **Roll Dice** button.
  - Add a **Use Powerup** toggle or button before rolling.
  - Show the result of each dice roll.

### 4. Status Effects & Visual Indicators
The game needs to clearly show when effects happen.
- **`GameView.java` & `GameBoardView.java`**:
  - Display active status effects (Shield, Confusion, Momentum Rush, Focus Mode, Freeze) and their remaining durations.
  - Indicate when a player skips their turn (Freeze effect).
  - Indicate role confusion visually when a monster's role is temporarily swapped.
  - Indicate energy changes to monsters and when a shield blocks an energy loss.

### 5. Card Cell Interactions
- **`GameView.java`**:
  - Display a popup or an animated indicator showing the **Card Name** and **Card Effect** whenever a Card Cell is landed on.

### 6. Exception & Validation Handling
- **Alert System**:
  - Catch game engine exceptions (e.g., invalid actions).
  - Display an indicator/popup explaining why the action could not be performed.
  - Ensure the game does NOT terminate on invalid actions (closing the popup must return to the game).

### 7. Game Over Screen
- **`GameOverView.java` (New Class)**:
  - Create a Game Won / Game Over screen displayed when a player wins.
  - Announce the winning monster's name and role.
  - Display the final energy of both monsters.
  - Add a button to return to the `MainMenuView` (start window).

## Verification Plan

### Manual Verification
- Launch the application and test the Main Menu buttons.
- Select a side and ensure the game initializes correctly.
- Play a few turns: roll the dice, activate powerups, and verify that status effects and role confusions are properly displayed.
- Purposefully trigger invalid actions to verify exception popups appear without crashing the game.
- Complete a game to ensure the Game Over screen displays correctly and allows returning to the start window.
