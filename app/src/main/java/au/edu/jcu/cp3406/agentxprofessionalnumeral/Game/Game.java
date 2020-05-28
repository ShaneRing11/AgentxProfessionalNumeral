package au.edu.jcu.cp3406.agentxprofessionalnumeral.Game;

/**
 * Object containing game information and current question
 */
public class Game {

    private Question question;
    private int score;
    private int bombsRemaining;

    public Game() {
        score = 0;
        bombsRemaining = 3;
    }

    public Game(int score, int bombsRemaining, Question question) {
        this.score = score;
        this.bombsRemaining = bombsRemaining;
        this.question = question;
    }

    public int updateScore(int bonus) {
        // Add the time bonus to the number of operations to get the base score
        int points = question.getLength() + bonus;
        // Modify base score based on which operations are present
        if (question.hasMultiplication()) {
            points *= 2;
        }
        if (question.hasDivision()) {
            points *= 2;
        }
        // Half the score if the RHS is x as question is easier to answer
        if (question.resultIsX()) {
            points /= 2;
        }
        score += points;
        return score;
    }

    public boolean checkGuess(int guess) {
        return question.checkGuess(guess);
    }

    public void useBomb() {
        if (bombsRemaining > 0) {
            bombsRemaining -= 1;
        }
    }

    public int getScore() {
        return score;
    }

    public int getBombsRemaining(){
        return bombsRemaining;
    }

    public String displayQuestion() {
        return question.display();
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}