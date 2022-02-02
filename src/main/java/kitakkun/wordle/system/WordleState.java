package kitakkun.wordle.system;

public enum WordleState {
    INPUT, NO_INPUT,
    BACKSPACE,
    NOT_ENOUGH_LETTERS, CHECKED,
    FINISHED, NOT_ON_DICTIONARY,
    NULL
}
