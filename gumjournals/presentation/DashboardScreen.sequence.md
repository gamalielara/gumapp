# Gumjournals Dashboard Screen Sequence

Source: `gumjournals/presentation/src/main/java/com/gumrindelwald/gumrjournals/presentation/DashboardScreen.kt`

## What happens when the user opens the screen

```mermaid
sequenceDiagram
    actor User
    participant Caller as Navigation/Caller
    participant Compose as Compose Runtime
    participant Screen as GumjournalsDashboardScreen
    participant LocalState as remember(showDialog)
    participant TopBar as TopBar
    participant Dialog as BasicAlertDialog

    User->>Caller: Open journals dashboard
    Caller->>Compose: Compose GumjournalsDashboardScreen()
    Compose->>Screen: Invoke composable
    Screen->>Compose: remember(MutableInteractionSource)
    Screen->>Compose: remember { mutableStateOf(false) }
    Compose-->>LocalState: Create showDialog = false
    Screen->>Compose: Emit Scaffold and layout tree
    Compose->>TopBar: Render static top bar
    Note over Screen,LocalState: No ViewModel, StateFlow, LiveData, repository, or observer exists in this screen yet.
    Note over TopBar: Back button onClick is empty and the date text is hardcoded.
    Note over Screen: Five emoji icons render, but only the weary emoji has click handling.
    User->>Screen: Tap weary emoji
    Screen->>LocalState: showDialog = true
    LocalState-->>Compose: State changed
    Compose->>Screen: Recompose
    Screen->>Dialog: Include dialog branch in UI tree
    User->>Dialog: Dismiss dialog
    Dialog->>LocalState: showDialog = false
    LocalState-->>Compose: State changed
    Compose->>Screen: Recompose without dialog
```

## Current architecture note

This screen is using only local Compose state.

- `remember { MutableInteractionSource() }` is used for the click interaction source.
- `remember { mutableStateOf(false) }` stores whether the dialog is visible.
- There is currently no `ViewModel`, observer, side effect, asynchronous load, or data fetch when the screen opens.
