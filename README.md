# Pawns-Only Chess

A console-based implementation of the Hyperskill **Pawns-Only Chess** project written in Kotlin.

This game uses a simplified chess ruleset where each player controls only pawns. Moves are entered in coordinate form, the board is rendered in the terminal, and the game handles captures, en passant, win conditions, and stalemate detection.

## Features

- Two-player terminal gameplay
- Text-based board rendering
- Pawn movement validation
- First-move double-step support
- Diagonal captures
- En passant capture
- Win detection
- Stalemate detection
- `exit` command to quit the game

## How to Run

Make sure you have a compatible Kotlin and Gradle setup, then run the application from your IDE or with Gradle.

## How to Play

1. Start the game.
2. Enter the names of both players.
3. Take turns entering moves in coordinate notation, for example:
```text
a2a4
4.
Type exit to end the game at any time.
Rules
•
Pawns move forward one square.
•
On their first move, a pawn may move forward two squares if both squares are empty.
•
Pawns capture one square diagonally forward.
•
En passant is supported.
•
A player wins by promoting a pawn to the opposite end or by leaving the opponent with no pawns or no legal moves.
•
The game ends in stalemate when no valid moves remain for the current player.
Example
Pawns-Only Chess
First Player's name:
Alice
Second Player's name:
Bob
    +---+---+---+---+---+---+---+---+
8   |   |   |   |   |   |   |   |   |
    +---+---+---+---+---+---+---+---+
7   | B | B | B | B | B | B | B | B |
    +---+---+---+---+---+---+---+---+
...
Alice's turn:
a2a4
Tech Stack
•
Kotlin
•
Gradle
Project Origin
This project was created as part of the Hyperskill learning platform.
