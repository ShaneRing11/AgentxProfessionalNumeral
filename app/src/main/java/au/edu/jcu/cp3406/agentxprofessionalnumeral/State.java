package au.edu.jcu.cp3406.agentxprofessionalnumeral;

/**
 * Enum used to communicate game state to activities controlling game behaviour
 */
public enum State {
    NEW_GAME,
    INCORRECT_GUESS,
    CORRECT_GUESS,
    BOMB_THROWN,
    GAME_OVER,
    MAIN_MENU
}
