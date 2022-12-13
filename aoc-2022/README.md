# aoc-2022

A Clojure library housing solutions for the 2022 Advent of Code challenge.

## Note on VSCode usage

The [Calva](https://calva.io/paredit/#editing) default keyboard bindings for barfing and slurping forwards clash with `cursorWordPartLeft` and `cursorWordPartRight`, respectively.

As such, an alternative keyboard shortcut can be used by replacing the [settings.json](https://code.visualstudio.com/docs/getstarted/keybindings#_advanced-customization) file with the following:

```json
[
    {
        "key": "ctrl+shift+right",
        "command": "paredit.slurpSexpForward",
        "when": "calva:keybindingsEnabled && editorTextFocus && editorLangId == 'clojure' && paredit:keyMap =~ /original|strict/"
    },
    {
        "key": "ctrl+shift+left",
        "command": "paredit.barfSexpForward",
        "when": "calva:keybindingsEnabled && editorTextFocus && editorLangId == 'clojure' && paredit:keyMap =~ /original|strict/"
    }
]
```
