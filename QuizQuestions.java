package Quizzproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class QuizQuestions {
    private ArrayList<Question> allQuestions;
    private ArrayList<Question> selectedQuestions;

    public QuizQuestions() {
        this.allQuestions = new ArrayList<>(); // No need to specify initial capacity
        this.selectedQuestions = new ArrayList<>();
    }

    public void load(String filename) {
        File questionFile = new File(filename);
        BufferedReader br = null; // Declare br outside the try block
        try {
            FileReader fr = new FileReader(questionFile);
            br = new BufferedReader(fr);
            String line;
            int i = 0;
            String questionString = null;
            ArrayList<String> answers = null;

            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    questionString = line;
                    i++;
                } else if (i == 1) {
                    answers = new ArrayList<>(Arrays.asList(line.split(",")));
                    i++;
                } else if (i == 2) {
                    if (line.equals("False") || line.equals("True")) {
                        TFQ aquestion = new TFQ(questionString, answers, line);
                        allQuestions.add(aquestion);
                    } else {
                        MCQ aquestion = new MCQ(questionString, answers, line);
                        allQuestions.add(aquestion);
                    }
                    i++;
                } else {
                    i = 0;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            // Handle the error more gracefully:
            allQuestions.add(new TFQ("Default Question", new ArrayList<>(Arrays.asList("Yes", "No")), "True")); // Add a default question
            System.out.println("Loaded default question due to file error.");
        } finally {
            // Ensure the BufferedReader is always closed
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Error closing BufferedReader: " + e.getMessage());
                }
            }
        }
    }

    public ArrayList<Question> select(int NumberOfQuestion) {
        selectedQuestions.clear(); // Clear previous selections
        Random rand = new Random();
        ArrayList<Integer> already = new ArrayList<>(NumberOfQuestion);

        // Check if there are any questions to select from
        if (allQuestions.isEmpty()) {
            System.err.println("No questions available to select.");
            return selectedQuestions; // Return an empty list
        }

        // Ensure NumberOfQuestion is not greater than the number of available questions
        int numQuestionsToSelect = Math.min(NumberOfQuestion, allQuestions.size());

        for (int i = 0; i < numQuestionsToSelect; i++) {
            int randomINT = rand.nextInt(allQuestions.size());
            while (already.contains(randomINT)) {
                randomINT = rand.nextInt(allQuestions.size());
            }
            selectedQuestions.add(allQuestions.get(randomINT));
            already.add(randomINT);
        }
        return selectedQuestions;
    }
}
