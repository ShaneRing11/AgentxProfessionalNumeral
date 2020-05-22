package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

public class Game {

    private QuestionBuilder questionBuilder;
    private Question question;
    private int score;
    private int detection;
    private int bombsRemaining;
    private int incorrectGuesses;
    private int timeBonus;

    public Game(Difficulty difficulty) {
        questionBuilder = new QuestionBuilder(difficulty);
        score = 0;
        detection = 0;
        bombsRemaining = 3;
    }

    public void generateQuestion() {
        incorrectGuesses = 0;
        timeBonus = 15;
        question = questionBuilder.buildQuestion();
    }

    public void updateDetection(int addedDetection) {
        detection += addedDetection;
    }

    public void updateScore() {
        int points = question.getLength() + timeBonus;
        if (question.hasMultiplication()) {
            points *= 2;
        }
        if (question.hasDivision()) {
            points *= 2;
        }
        if (question.resultIsX()) {
            points /= 2;
        }
        score += points;
    }

    // Increment the time bonus and threat values
    public void tick() {
        if (timeBonus > 0) {
            --timeBonus;
        }
        updateDetection(1);
    }

    public boolean checkGuess(int guess) {
        return question.checkGuess(guess);
    }

    public void useBomb() {
        if (bombsRemaining > 0) {
            bombsRemaining -= 1;
            updateDetection(5);
        }
    }

    public int getScore() {
        return score;
    }


    public int getDetection() {
        return detection;
    }

    public int
    getBombsRemaining(){
        return bombsRemaining;
    }

    public String displayQuestion() {
        return question.display();
    }
}