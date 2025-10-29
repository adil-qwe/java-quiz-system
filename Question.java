package Quizzproject;


import java.util.ArrayList;

public interface Question {
    String getQuestion();
    ArrayList<String> getAllAnswers();
    String getCorrectAnswer();
}
