package experiment.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import experiment.model.Triangle;
import experiment.model.TrianglePair;
import experiment.model.LeapListener;
import experiment.utility.CoordinateMapper;
import experiment.utility.Mathematics;

public class ScalingController implements Initializable {

    @FXML
    BorderPane root;
    
    @FXML
    AnchorPane playground;
    
    @FXML
    TextFlow middleMessage;
    
    @FXML
    Polygon polyLeft;
    
    @FXML
    Polygon polyRight;
    
    @FXML
    Rectangle rectStart;
    
    @FXML
    Rectangle rectEnd;
    
    private Stage stage;
    private LeapListener listener;
    private CoordinateMapper mapper;
    private boolean isTwoAttached;
    private double distanceOnGrab;
    private double scaleOnGrab;
    
    private long timeStart;
    private long timeEnd;
    private int modeID;
    private int fadeOffDuration = Mathematics.FADEOFF_DURATION;
    private boolean isSuccess;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isTwoAttached = false;
        timeStart = 0;
        timeEnd = 0;
        isSuccess = false;
    }

    public void initController(Stage stage, LeapListener listener) {
        this.stage = stage;
        this.listener = listener;
        registerListeners();
        
    }
    
    private EventHandler<KeyEvent> spaceHandle = event -> {
        if (event.getCode() == KeyCode.SPACE) {
            polyLeft.setLayoutX(0);
            polyLeft.setLayoutY(0);
            polyRight.setLayoutX(0);
            polyRight.setLayoutY(0);
            polyLeft.getPoints().clear();
            polyRight.getPoints().clear();
            
            mapper = new CoordinateMapper(playground.getWidth(), playground.getHeight(), 500);
            
            middleMessage.setVisible(false);
            
            listener.enableRecord();
            listener.startDetect();
            timeStart = System.currentTimeMillis();
            System.out.println("Space pressed");
        }
    };
    
    private ChangeListener<TrianglePair> handPairListener = (observable, oldValue, newValue) -> {
        Platform.runLater(() -> {
            if (mapper != null) {
                Point3D centerLeft = updatePlaneCenter(polyLeft, newValue.getLeft());
                Point3D centerRight = updatePlaneCenter(polyRight, newValue.getRight());
                
                // Both hands
                if (centerLeft != null && centerRight != null) {
                    modeID = 3;
                    
                    Point3D leftIndex = mapper.leapToApp(newValue.getLeft().getIndex());
                    Point3D rightIndex = mapper.leapToApp(newValue.getRight().getIndex());
                    
                    
                    if (isTwoAttached) {
                        // Rotate
                        if (oldValue.getLeft() != null && oldValue.getRight() != null) {
                            Point3D thumbOldL = mapper.leapToApp(oldValue.getLeft().getThumb());
                            Point3D indexOldL = mapper.leapToApp(oldValue.getLeft().getIndex());
                            Point3D middleOldL = mapper.leapToApp(oldValue.getLeft().getMiddle());
                            Point3D thumbOldR = mapper.leapToApp(oldValue.getRight().getThumb());
                            Point3D indexOldR = mapper.leapToApp(oldValue.getRight().getIndex());
                            Point3D middleOldR = mapper.leapToApp(oldValue.getRight().getMiddle());
                            
                            Point3D centerLeftOld = Mathematics.CalculateCenterPoint(thumbOldL, indexOldL, middleOldL);
                            Point3D centerRightOld = Mathematics.CalculateCenterPoint(thumbOldR, indexOldR, middleOldR);
                            double distanceOld = centerLeftOld.distance(centerRightOld);
                            double distanceNew = centerLeft.distance(centerRight);
                            
                            
                            double scalar = (distanceNew - distanceOld) * 0.01;
                            
                            rectStart.setScaleX(rectStart.getScaleX() + scalar);
                            rectStart.setScaleY(rectStart.getScaleY() + scalar);
                            
                            checkSuccess();
                        }
                    } else {
                        isTwoAttached = isTwoHandsAttarched(leftIndex, rightIndex);
                        distanceOnGrab = leftIndex.distance(rightIndex);
                        scaleOnGrab = rectStart.getScaleX();
                    }
                    
                } else {
                    clearPlane(polyLeft);
                    clearPlane(polyRight);
                }
            } else {
                clearPlane(polyLeft);
                clearPlane(polyRight);
                System.out.println("Coordinate Mapper is not available.");
            }
        });
    };
    
    private Point3D updatePlaneCenter(Polygon plane, Triangle hand) {
        if (hand != null) {
            Point3D thumb = mapper.leapToApp(hand.getThumb());
            Point3D index = mapper.leapToApp(hand.getIndex());
            Point3D middle = mapper.leapToApp(hand.getMiddle());
            
            updatePlane(plane, thumb, index, middle);
            
            return Mathematics.CalculateCenterPoint(thumb, index, middle);
        } else {
            clearPlane(plane);
            return null;
        }
    }
    
    private void updatePlane(Polygon plane, Point3D thumb, Point3D index, Point3D middle) {
        plane.getPoints().clear();
        plane.getPoints().addAll(thumb.getX(),thumb.getY(),index.getX(),index.getY(),middle.getX(), middle.getY());
    }
    
    /**
     * Use local position
     * @param position
     * @return
     */
    private boolean isTwoHandsAttarched(Point3D leftIndex, Point3D rightIndex) {
        Point3D localLeftIndex = rectStart.parentToLocal(leftIndex);
        Point3D localRightIndex = rectStart.parentToLocal(rightIndex);
        boolean isAttachedL = rectStart.contains(localLeftIndex.getX(), localLeftIndex.getY());
        boolean isAttachedR = rectStart.contains(localRightIndex.getX(), localRightIndex.getY());
        return isAttachedL && isAttachedR;
    }
    
    private void clearPlane(Polygon plane) {
        rectStart.setStrokeWidth(1);
        plane.getPoints().clear();
    }
    
    private void registerListeners() {
        stage.addEventFilter(KeyEvent.KEY_RELEASED, spaceHandle);
        listener.handPairProperty().addListener(handPairListener);
    }
    
    private void removeListeners() {
        listener.handPairProperty().removeListener(handPairListener);
        stage.removeEventFilter(KeyEvent.KEY_RELEASED, spaceHandle);
    }
    
    private void checkSuccess() {
        if ( isSuccess == false && rectStart.getScaleX() < rectEnd.getScaleX() + 0.05 && rectStart.getScaleX() > rectEnd.getScaleX() - 0.05) {
            isSuccess = true;
            removeListeners();
            listener.disableRecord();
            listener.stopDetect();
            
            timeEnd = System.currentTimeMillis();
            int time = (int) (timeEnd - timeStart);
            
            
            System.out.println("[S] Current step: " + listener.getDataManager().getExperiment().getStep());
            listener.getDataManager().updateExperimentRecords(modeID, time);
            
            int currentStep = listener.getDataManager().getCurrentStep();
            System.out.println("[S] Mode " + listener.getDataManager().getExperiment().getModeRecord(currentStep-1));
            System.out.println("[S] Time " + listener.getDataManager().getExperiment().getTimeRecord(currentStep-1));
            
            FadeTransition ft = new FadeTransition(Duration.millis(fadeOffDuration), root);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setOnFinished((event) -> {
                try {
                    switch (currentStep) {
                    case 7: {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.SCALING_SCREEN2));
                        
                        BorderPane screen = loader.load();
                        ScalingController tc = loader.getController();
                        tc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                          
                        break;
                    }
                    
                    case 8:{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.SCALING_SCREEN3));
                        
                        BorderPane screen = loader.load();
                        ScalingController tc = loader.getController();
                        tc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                           
                        break;                        
                    }
                    
                    case 9: {
                        
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.THANKS_SCREEN));
                        BorderPane screen = loader.load();
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);
                        
                        listener.getDataManager().saveToDatabase();
                        listener.getDataManager().closeFileWriter();
                        break;
                    }
                    default:
                        System.out.println("Invalid step");
                        break;
                    }
                    
                } catch(Exception e) {
                    e.printStackTrace();
                }
                
            });
            ft.play();
           
            
        }
    }
    
}
