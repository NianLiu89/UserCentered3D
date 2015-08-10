package analyzer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import analyzer.chart.ChartAllFingerDistance;
import analyzer.chart.ChartAllFingerPosition;
import analyzer.data.ExData;

public class Analyzer extends Application{

    private double width = 1400;
    private double height = 800;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Reader reader = new Reader(false);
        // [expID, taskID, hand]
        int[] config = {2, 5, 1};
        ExData data = reader.getExData(config[0]);

        LostTrackAnalyser lta = new LostTrackAnalyser();
        lta.analyzeByExperiment(data);
        
//        ChartAllFingerVelocity c = new ChartAllFingerVelocity(data.getDataByTaskID(1), 0);
        
//        ChartAllFingerDistance c = new ChartAllFingerDistance(data.getDataByTaskID(1), 0);
//        AnchorPane root = new AnchorPane();
//        root.getChildren().add(c);
//
//        Scene scene = new Scene(root, width, height);
//        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
        
        
        plotPosition(data, config);
        //plotDistance(data, config);
        
        config[2] = 1;
//        plotPosition(data, config);
        //plotDistance(data, config);
    }
    
    private void plotPosition(ExData data, int[] config) {
        for (int i=0; i < 3; i++) {
            AnchorPane root = new AnchorPane();
            ChartAllFingerPosition chart = new ChartAllFingerPosition(data.getDataByTaskID(config[1]), config[2], i);

            String hand = config[2] == 1 ? "Right" : "Left";
            String dimension = i == 2 ? "Z" : i == 1 ? "Y" : "X"; 
            chart.getChart().setTitle("Experiment " + (config[0] + 1) + " - Task " + (config[1] + 1) + " - " + data.getTimeOf(config[1]) + " secs - " + hand + " - " + dimension);

            root.getChildren().add(chart);

            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
        }
//        LostTrackAnalyser lta = new LostTrackAnalyser();
//        lta.analyzeByTask(data.getDataByTaskID(config[1]), 0);
//        lta.analyzeByTask(data.getDataByTaskID(config[1]), 1);
        
        
        System.out.println("time " + data.getTimeOf(config[1]));
    }
    
    private void plotDistance(ExData data, int[] config) {
        ChartAllFingerDistance c = new ChartAllFingerDistance(data.getDataByTaskID(config[1]), config[2]);
        AnchorPane root = new AnchorPane();
        
        String hand = config[2] == 1 ? "Right" : "Left";
        c.getChart().setTitle("Distance Between Fingers: Experiment " + (config[0] + 1) + " - Task " + (config[1] + 1) + " - " + data.getTimeOf(config[1]) + " secs - " + hand);
        root.getChildren().add(c);

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    
}
