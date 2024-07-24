import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FitnessTrackerApp extends JFrame {
    private JTextField stepsField;
    private JTextField distanceField;
    private JComboBox<String> activityComboBox;
    private JButton trackButton;
    private JLabel resultLabel;
    private JTextArea summaryArea;
    private List<ActivityRecord> activityRecords;

    private static final double CALORIES_PER_STEP = 0.04; // Sample value
    private static final double CALORIES_PER_KM = 0.1; // Sample value

    public FitnessTrackerApp() {
        setTitle("Health and Fitness Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        activityRecords = new ArrayList<>();

        // Create UI components
        stepsField = new JTextField(10);
        distanceField = new JTextField(10);
        activityComboBox = new JComboBox<>(new String[]{"Walking", "Running", "Cycling"});
        trackButton = new JButton("Track Activity");
        resultLabel = new JLabel();
        summaryArea = new JTextArea(5, 30);
        summaryArea.setEditable(false);
        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);

        // Set up the action listener
        trackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int steps = Integer.parseInt(stepsField.getText());
                    double distance = Double.parseDouble(distanceField.getText());

                    // Calculate calories burned
                    double caloriesBurned = calculateCaloriesBurned(steps, distance);
                    String activity = (String) activityComboBox.getSelectedItem();

                    // Record activity
                    activityRecords.add(new ActivityRecord(activity, steps, distance, caloriesBurned));

                    // Display the results
                    resultLabel.setText("Calories Burned: " + new DecimalFormat("#.##").format(caloriesBurned));
                    updateSummary();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(FitnessTrackerApp.this, "Please enter valid numbers.");
                }
            }
        });

        // Layout setup
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Activity Type:"));
        inputPanel.add(activityComboBox);
        inputPanel.add(new JLabel("Steps taken:"));
        inputPanel.add(stepsField);
        inputPanel.add(new JLabel("Distance (km):"));
        inputPanel.add(distanceField);
        inputPanel.add(new JLabel());
        inputPanel.add(trackButton);

        JPanel resultPanel = new JPanel();
        resultPanel.add(resultLabel);

        JPanel summaryPanel = new JPanel();
        summaryPanel.add(new JLabel("Weekly Summary:"));
        summaryPanel.add(summaryScrollPane);

        add(inputPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    // Method to calculate estimated calories burned
    private double calculateCaloriesBurned(int steps, double distance) {
        return (steps * CALORIES_PER_STEP) + (distance * CALORIES_PER_KM);
    }

    // Method to update the weekly summary
    private void updateSummary() {
        StringBuilder summary = new StringBuilder();
        double totalCalories = 0;

        for (ActivityRecord record : activityRecords) {
            summary.append(String.format("%s: %d steps, %.2f km, %.2f calories\n",
                    record.getActivityType(), record.getSteps(), record.getDistance(), record.getCaloriesBurned()));
            totalCalories += record.getCaloriesBurned();
        }

        summary.append(String.format("\nTotal Calories Burned: %.2f", totalCalories));
        summaryArea.setText(summary.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FitnessTrackerApp().setVisible(true);
            }
        });
    }

    // Helper class to store activity records
    private static class ActivityRecord {
        private String activityType;
        private int steps;
        private double distance;
        private double caloriesBurned;

        public ActivityRecord(String activityType, int steps, double distance, double caloriesBurned) {
            this.activityType = activityType;
            this.steps = steps;
            this.distance = distance;
            this.caloriesBurned = caloriesBurned;
        }

        public String getActivityType() {
            return activityType;
        }

        public int getSteps() {
            return steps;
        }

        public double getDistance() {
            return distance;
        }

        public double getCaloriesBurned() {
            return caloriesBurned;
        }
    }
}
