package au.edu.jcu.cp3406.agentxprofessionalnumeral;

class Question {

    private int[] numbers;
    private int[] operations;
    private int answer;
    private int missingValue;

    public Question(int[] numbers, int[] operations, int answer, int missingValue) {
        this.numbers = numbers;
        this.operations = operations;
        this.answer = answer;
        this.missingValue = missingValue;
    }

}


