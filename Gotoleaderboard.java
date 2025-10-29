package Quizzproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.control.Label;
import java.util.*;
import java.util.stream.Collectors;
import static Quizzproject.Quizzer.fadeSceneTransition;

public class Gotoleaderboard implements EventHandler<ActionEvent> {
    private Label leaderboardLabel;
    private Scene scene3;
    private Stage stage;
    private String user;
    private int score;
    private int timeTaken;
    private String leaderboardFile;

    public Gotoleaderboard(Label leaderboardLabel, Scene scene, Stage stage, String user, int score, int timeTaken, String leaderboardFile) {
        this.leaderboardLabel = leaderboardLabel;
        this.scene3 = scene;
        this.stage = stage;
        this.user = user;
        this.score = score;
        this.timeTaken = timeTaken;
        this.leaderboardFile = leaderboardFile;
    }

    // Inner class to store both score and time
    private static class LeaderboardEntry {
        int score;
        int timeTaken;
        
        LeaderboardEntry(int score, int timeTaken) {
            this.score = score;
            this.timeTaken = timeTaken;
        }
    }

    public Map<String, LeaderboardEntry> loadLeaderboard() {
        Map<String, LeaderboardEntry> leaderboard = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
             new FileReader(leaderboardFile))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    leaderboard.put(parts[0], 
                        new LeaderboardEntry(
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2])
                        ));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading leaderboard: " + e.getMessage());
        }
        return leaderboard;
    }

    public void saveLeaderboard(Map<String, LeaderboardEntry> leaderboard) {
        try (BufferedWriter bw = new BufferedWriter(
             new FileWriter(leaderboardFile))) {
            
            // Sort by score (descending) then by time (ascending)
            leaderboard.entrySet().stream()
                .sorted((e1, e2) -> {
                    int scoreCompare = Integer.compare(e2.getValue().score, e1.getValue().score);
                    if (scoreCompare != 0) return scoreCompare;
                    return Integer.compare(e1.getValue().timeTaken, e2.getValue().timeTaken);
                })
                .forEach(entry -> {
                    try {
                        bw.write(String.format("%s,%d,%d",
                            entry.getKey(),
                            entry.getValue().score,
                            entry.getValue().timeTaken));
                        bw.newLine();
                    } catch (IOException e) {
                        System.out.println("Error writing leaderboard: " + e.getMessage());
                    }
                });
        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    @Override
    public void handle(ActionEvent event) {
        Map<String, LeaderboardEntry> leaderboard = loadLeaderboard();
        
        // Update with current result if better
        LeaderboardEntry currentEntry = leaderboard.get(user);
        if (currentEntry == null || score > currentEntry.score || 
            (score == currentEntry.score && timeTaken < currentEntry.timeTaken)) {
            leaderboard.put(user, new LeaderboardEntry(score, timeTaken));
            saveLeaderboard(leaderboard);
        }
        
        // Generate display text
        String leaderboardText = leaderboard.entrySet().stream()
            .sorted((e1, e2) -> {
                int scoreCompare = Integer.compare(e2.getValue().score, e1.getValue().score);
                if (scoreCompare != 0) return scoreCompare;
                return Integer.compare(e1.getValue().timeTaken, e2.getValue().timeTaken);
            })
            .limit(10) // Show top 10
            .map(entry -> String.format("%s - Score: %d (Time: %ds)", 
                 entry.getKey(), entry.getValue().score, entry.getValue().timeTaken))
            .collect(Collectors.joining("\n"));
        
        leaderboardLabel.setText("TOP 10 Leaderboard:\n" + leaderboardText);
        fadeSceneTransition(stage, scene3);;
    }
}