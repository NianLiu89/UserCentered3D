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

public class ChartAllFingerPosition extends Group {

    private ExFrames frames; 
    
    private Axis<Number> xAxis;
    private Axis<Number> yAxis;
    private ScatterChart<Number, Number> chart;
    private int dimension;
    private int hand;
    
    /**
     * 
     * @param frames
     * @param hand 0 is left; 1 is right
     * @param dimension 0 is X; 1 is Y; 2 is Z
     */
    public ChartAllFingerPosition(ExFrames frames, int hand, int dimension) {
        this.frames = frames;
        this.hand = hand;
        this.dimension = dimension;
        initAxes();
        init();
    }
    
    private void initAxes() {
        xAxis = new NumberAxis(0, frames.size(), 60);
        switch (dimension) {
            case 0:
                yAxis = new NumberAxis(-117.5, 117.5, 10);
                break;
            case 1:
                yAxis = new NumberAxis(-1, 317.5, 10);
                break;
            case 2:
                yAxis = new NumberAxis(-73.5, 73.5, 10);
                break;
            default:
                yAxis = new NumberAxis(-117.5, 117.5, 10);
                break;
        }
    }
    
    public void init() {

        chart = new ScatterChart<Number, Number>(xAxis, yAxis);
        chart.setPrefWidth(1400);
        chart.setPrefHeight(800);
        chart.setTitle("Leap Data");
        
        Series<Number, Number> seriesThumb = new Series<Number, Number>();
        seriesThumb.setName("Thumb");
        Series<Number, Number> seriesIndex = new Series<Number, Number>();
        seriesIndex.setName("Index");
        Series<Number, Number> seriesMiddle = new Series<Number, Number>();
        seriesMiddle.setName("Middle");

        int index = 0;
        for (ExFrame f : frames) {
            ExHand h = hand == 1 ? f.getRight() : f.getLeft();
            switch (dimension) {
            case 0:
                seriesThumb.getData().add(new Data<Number, Number>(index + 1, h.getThumb().getX()));
                seriesIndex.getData().add(new Data<Number, Number>(index + 1, h.getIndex().getX()));
                seriesMiddle.getData().add(new Data<Number, Number>(index + 1, h.getMiddle().getX()));
                break;
            case 1:
                seriesThumb.getData().add(new Data<Number, Number>(index + 1, h.getThumb().getY()));
                seriesIndex.getData().add(new Data<Number, Number>(index + 1, h.getIndex().getY()));
                seriesMiddle.getData().add(new Data<Number, Number>(index + 1, h.getMiddle().getY()));
                break;
            case 2:
                seriesThumb.getData().add(new Data<Number, Number>(index + 1, h.getThumb().getZ()));
                seriesIndex.getData().add(new Data<Number, Number>(index + 1, h.getIndex().getZ()));
                seriesMiddle.getData().add(new Data<Number, Number>(index + 1, h.getMiddle().getZ()));
                break;
            default:
                seriesThumb.getData().add(new Data<Number, Number>(index + 1, h.getThumb().getX()));
                seriesIndex.getData().add(new Data<Number, Number>(index + 1, h.getIndex().getX()));
                seriesMiddle.getData().add(new Data<Number, Number>(index + 1, h.getMiddle().getX()));
                break;
            }
            index++;
        }
        
        chart.getData().addAll(seriesThumb,seriesIndex, seriesMiddle);
        
        this.getChildren().add(chart);
    }
    
    public ScatterChart<Number, Number> getChart() {
        return chart;
    }
    

}
