# Escape the Island

A 2D tile-based game built for UC Berkeley's CS61BL *Build Your Own World* (BYOW)
project. You play as the avatar stranded on a psuedorandomly generated island
of rooms and hallways. Collect all **20 shells** scattered across the map to
escape — but a pirate hunts you down using A\* pathfinding. Grab every shell to
win; get caught and you lose.

## Features

- **Procedural generation** — 14–17 non-overlapping rooms connected by
  randomized L-shaped hallways, seeded so the same seed always produces the same
  island.
- **A\* pirate AI** — the chaser recomputes the shortest path to you every few
  ticks; press `P` to visualize its route.
- **Difficulty levels** — Easy through Master, which control how fast the pirate
  moves.
- **Save & resume** — quit with `:Q` and pick up exactly where you left off.
- **Live HUD** — shells remaining, controls, and a description of whatever tile
  your mouse is hovering over.

## Requirements

- **JDK 20** or newer (the project targets Amazon Corretto 20).
  Check yours with `java -version`.
- That's it — the one external dependency (Princeton's `algs4.jar`, which
  provides the `StdDraw` graphics library) is bundled in [`lib/`](lib/), so there
  is nothing else to download.

> The game opens a graphics window, so it needs a desktop environment — it can't
> run on a headless server.

## Run it

You only need a **JDK 20+** — no IDE, no build tool. The project is plain Java
plus the bundled `lib/algs4.jar`, so it builds and runs anywhere Java is
installed. Pick whichever option suits you; they all run the same code.

### Quickest — run script

From the repo root:

```bash
./run.sh      # macOS / Linux
run.bat       # Windows
```

This compiles everything and launches the game in one step.

### Option A — IntelliJ IDEA

1. Clone the repo and open the folder in IntelliJ (**File → Open**, select the
   project folder).
2. IntelliJ reads the bundled project config and wires up the `lib/` library
   automatically. If prompted, set the Project SDK to a JDK 20+ install
   (**File → Project Structure → Project**).
3. Open [`proj3/src/core/Main.java`](proj3/src/core/Main.java) and click the
   green ▶ next to `main`.

### Option B — VS Code

1. Install the **Extension Pack for Java** (Microsoft) if you haven't already.
2. Open the project folder. The bundled [`.vscode/settings.json`](.vscode/settings.json)
   tells the Java extension where the source and `lib/algs4.jar` are, so imports
   resolve automatically. Make sure a JDK 20+ is selected (command palette →
   *Java: Configure Java Runtime*).
3. Open [`proj3/src/core/Main.java`](proj3/src/core/Main.java) and click **Run**
   above `main`.

### Option C — Command line (any OS, no IDE)

From the repo root:

```bash
# 1. Compile
javac -cp "lib/*" -d out/production/project-3-byow-Sunrise \
  $(find proj3/src -name "*.java")

# 2. Run
java -cp "out/production/project-3-byow-Sunrise:lib/*" core.Main
```

> On Windows, use `;` instead of `:` as the classpath separator, and
> `dir /s /b proj3\src\*.java` in place of the `find` command.

## Controls

| Key        | Action                          |
|------------|---------------------------------|
| `W A S D`  | Move the avatar                 |
| `P`        | Toggle the pirate's path overlay |
| `:Q`       | Save and quit                   |
| Mouse hover | Inspect the tile under the cursor |

From the main menu: **N** to start a new game (then type a seed), **L** to load
your saved game, **Q** to quit.

## Project layout

```
.
├── lib/                       # bundled algs4.jar (StdDraw) — the only dependency
├── proj3/src/
│   ├── core/                  # game logic
│   │   ├── Main.java          # entry point: menus + main game loop
│   │   ├── World.java         # grid, generation pipeline, entity movement
│   │   ├── PathFinder.java    # A* pathfinding for the pirate
│   │   ├── SavedWorld.java    # save/load (save.txt)
│   │   ├── HUD.java           # top status bar
│   │   └── Room.java          # room rectangle helper
│   └── tileengine/            # provided rendering layer (TERenderer, TETile, Tileset)
│   └── utils/                 # provided RandomUtils
└── save.txt                   # current saved game (created/overwritten on :Q)
```

The world is an 80×50 grid of `TETile` objects. The game determines tile
identity by reference-equality against shared `Tileset` constants, so map edits
reuse those exact instances. Saves store only the seed plus entity positions —
the map is regenerated deterministically from the seed on load.

## Credits

Course scaffolding (`tileengine/`, `utils/`, `algs4.jar`) is provided by UC
Berkeley CS61BL. Game design and implementation by the project authors.
