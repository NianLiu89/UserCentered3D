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

public class ChartAllFingerVelocity extends Group {

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
    public ChartAllFingerVelocity(ExFrames frames, int hand) {
        this.frames = frames;
        this.hand = hand;
        initAxes();
        init();
    }
    
    private void initAxes() {
        xAxis = new NumberAxis(0, frames.size(), 60);
        yAxis = new NumberAxis(0, 1000, 10);
    }
    
    public void init() {

        chart = new ScatterChart<Number, Number>(xAxis, yAxis);
        chart.setPrefWidth(1400);
        chart.setPrefHeight(800);
        
        Series<Number, Number> seriesThumb = new Series<Number, Number>();
        seriesThumb.setName("Thumb");
        Series<Number, Number> seriesIndex = new Series<Number, Number>();
        seriesIndex.setName("Index");
        Series<Number, Number> seriesMiddle = new Series<Number, Number>();
        seriesMiddle.setName("Middle");

        int index = 0;
        for (ExFrame f : frames) {
            ExHand h = hand == 1 ? f.getRight() : f.getLeft();
            seriesThumb.getData().add(new Data<Number, Number>(index + 1, h.getThumbVelocity().magnitude()));
            seriesIndex.getData().add(new Data<Number, Number>(index + 1, h.getIndexVelocity().magnitude()));
            seriesMiddle.getData().add(new Data<Number, Number>(index + 1, h.getMiddleVelocity().magnitude()));
            
            index++;
        }
        
        chart.getData().addAll(seriesThumb,seriesIndex, seriesMiddle);
        
        this.getChildren().add(chart);
    }
    
    public ScatterChart<Number, Number> getChart() {
        return chart;
    }
    

}
