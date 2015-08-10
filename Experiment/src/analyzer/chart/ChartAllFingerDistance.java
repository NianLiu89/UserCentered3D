package analyzer.chart;

import javafx.scene.Group;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import analyzer.data.ExFrame;
import analyzer.data.ExFrames;
import analyzer.data.ExHand;

public class ChartAllFingerDistance extends Group {

    private ExFrames frames; 
    
    private Axis<Number> xAxis;
    private Axis<Number> yAxis;
    private ScatterChart<Number, Number> chart;
    private int hand;
    
    /**
     * 
     * @param frames
     * @param hand 0 is left; 1 is right
     * @param dimension 0 is X; 1 is Y; 2 is Z
     */
    public ChartAllFingerDistance(ExFrames frames, int hand) {
        this.frames = frames;
        this.hand = hand;
        initAxes();
        init();
    }
    
    private void initAxes() {
        xAxis = new NumberAxis(0, frames.size(), 60);
        yAxis = new NumberAxis(0, 150, 10);
    }
    
    public void init() {

        chart = new ScatterChart<Number, Number>(xAxis, yAxis);
        chart.setPrefWidth(1400);
        chart.setPrefHeight(800);
        
        Series<Number, Number> seriesTI = new Series<Number, Number>();
        seriesTI.setName("Thumb - Index");
        Series<Number, Number> seriesIM = new Series<Number, Number>();
        seriesIM.setName("Index - Middle");
        Series<Number, Number> seriesMT = new Series<Number, Number>();
        seriesMT.setName("Middle - Thumb");

        int index = 0;
        for (ExFrame f : frames) {
            ExHand h = hand == 1 ? f.getRight() : f.getLeft();
            seriesTI.getData().add(new Data<Number, Number>(index + 1, h.getThumb().distance(h.getIndex())));
            seriesIM.getData().add(new Data<Number, Number>(index + 1, h.getIndex().distance(h.getMiddle())));
            seriesMT.getData().add(new Data<Number, Number>(index + 1, h.getMiddle().distance(h.getThumb())));
            index++;
        }
        
        chart.getData().addAll(seriesTI,seriesIM, seriesMT);
        
        this.getChildren().add(chart);
    }
    
    public ScatterChart<Number, Number> getChart() {
        return chart;
    }
    

}
