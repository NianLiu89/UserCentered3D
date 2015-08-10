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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import experiment.model.Triangle;
import experiment.model.TrianglePair;
import experiment.model.LeapListener;
import experiment.utility.CoordinateMapper;
import experiment.utility.Mathematics;

public class RotationController implements Initializable {

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
    Arc rectStart;
    
    @FXML
    Arc rectEnd;
    
    private Stage stage;
    private LeapListener listener;
    private CoordinateMapper mapper;
    private boolean isLeftAttached;
    private boolean isRightAttached;
    private boolean isTwoAttached;
    
    private double degreeOnGrab;
    private Point3D leftNormalOnGrab;
    private Point3D rightNormalOnGrab;
    
    private long timeStart;
    private long timeEnd;
    private int modeID;
    private int fadeOffDuration = Mathematics.FADEOFF_DURATION;
    private boolean isSuccess;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isLeftAttached = false;
        isRightAttached = false;
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
//                if (centerLeft != null && centerRight != null) {
//                    modeID = 3;
//                    Point3D newNormalL = getNormalVectorFromLeapHand(newValue.getLeftHand());
//                    Point3D newNormalR = getNormalVectorFromLeapHand(newValue.getRightHand());
//                    
//                    Point3D centerTwoHands = centerLeft.midpoint(centerRight);
//                    Point3D localCenterTwoHands = rectStart.parentToLocal(centerTwoHands);
//                    
//                    if (isTwoAttached) {
//                        if (rectStart.contains(localCenterTwoHands.getX(), localCenterTwoHands.getY())) {
//                            // Rotate
//                            Point3D oldNormalL = getNormalVectorFromLeapHand(oldValue.getLeftHand());
//                            Point3D oldNormalR = getNormalVectorFromLeapHand(oldValue.getRightHand());
//
//                            Point2D oldNormal2DL = new Point2D(oldNormalL.getX(), oldNormalL.getY());
//                            Point2D oldNormal2DRotate90DegreeL = new Point2D(-oldNormalL.getY(), oldNormalL.getX());
//                            Point2D newNormal2DL = new Point2D(newNormalL.getX(), newNormalL.getY());
//                            double degreeChangedL = newNormal2DL.dotProduct(oldNormal2DRotate90DegreeL) > 0 ? newNormal2DL.angle(oldNormal2DL) : -newNormal2DL.angle(oldNormal2DL);
//                            
//                            Point2D oldNormal2DR = new Point2D(oldNormalR.getX(), oldNormalR.getY());
//                            Point2D oldNormal2DRotate90DegreeR = new Point2D(-oldNormalR.getY(), oldNormalR.getX());
//                            Point2D newNormal2DR = new Point2D(newNormalR.getX(), newNormalR.getY());
//                            double degreeChangedR = newNormal2DR.dotProduct(oldNormal2DRotate90DegreeR) > 0 ? newNormal2DR.angle(oldNormal2DR) : -newNormal2DR.angle(oldNormal2DR);
//                            
//                            if (!Double.isNaN(degreeChangedL) && !Double.isNaN(degreeChangedR)) {
//                                rectStart.setRotate(rectStart.getRotate() + (degreeChangedL + degreeChangedR) / 2);
//                            }
//                            
//                            
//                            checkSuccess();
//                        }
//                    } else {
//                        // On Attached
//                        if (rectStart.contains(localCenterTwoHands.getX(), localCenterTwoHands.getY())) {
//                            leftNormalOnGrab = newNormalL;
//                            rightNormalOnGrab = newNormalR;
//                            degreeOnGrab = rectStart.getRotate();
//                            isTwoAttached = true;
//                        }
//                    }
//                    
//                } else {
                    
                    // Left hand
                    if (centerLeft != null) {
                        modeID = 1;
                        Point3D localPosition = rectStart.parentToLocal(centerLeft);
                        Point3D newNormal = getNormalVectorFromLeapHand(newValue.getLeft());
                        
                        if (isLeftAttached) {
                            rectStart.setStrokeWidth(4);
                            if (rectStart.contains(localPosition.getX(), localPosition.getY())) {
                                // Rotate
                                Point3D oldNormal = getNormalVectorFromLeapHand(oldValue.getLeft());
                                
                                Point2D oldNormal2D = new Point2D(oldNormal.getX(), oldNormal.getY());
                                Point2D oldNormal2DRotate90Degree = new Point2D(-oldNormal.getY(), oldNormal.getX());
                                Point2D newNormal2D = new Point2D(newNormal.getX(), newNormal.getY());
                                
                                double degreeChanged = newNormal2D.angle(oldNormal2D);
                                
                                if (!Double.isNaN(degreeChanged)) {
                                    if (newNormal2D.dotProduct(oldNormal2DRotate90Degree) > 0) {
                                        rectStart.setRotate(rectStart.getRotate() + degreeChanged);
                                    } else {
                                        rectStart.setRotate(rectStart.getRotate() - degreeChanged);
                                    }
                                }
                                
                                checkSuccess();
                            } else {
                                isLeftAttached = false;
                            }
                        } else {
                            rectStart.setStrokeWidth(1);
                            // On Attached
                            if (rectStart.contains(localPosition.getX(), localPosition.getY())) {
                                leftNormalOnGrab = newNormal;
                                degreeOnGrab = rectStart.getRotate();
                                isLeftAttached = true;
                            }
                        }
                    }
                    
                    // Right Hand
                    if (centerRight != null) {
                        modeID = 2;
                        Point3D localPosition = rectStart.parentToLocal(centerRight);
                        Point3D newNormal = getNormalVectorFromLeapHand(newValue.getRight());
                        
                        if (isRightAttached) {
                            rectStart.setStrokeWidth(4);
                            if (rectStart.contains(localPosition.getX(), localPosition.getY())) {
                                // Rotate
                                Point3D oldNormal = getNormalVectorFromLeapHand(oldValue.getRight());
                                
                                Point2D oldNormal2D = new Point2D(oldNormal.getX(), oldNormal.getY());
                                Point2D oldNormal2DRotate90Degree = new Point2D(-oldNormal.getY(), oldNormal.getX());
                                Point2D newNormal2D = new Point2D(newNormal.getX(), newNormal.getY());
                                
                                double degreeChanged = newNormal2D.angle(oldNormal2D);
                                if (!Double.isNaN(degreeChanged)) {
                                    if (newNormal2D.dotProduct(oldNormal2DRotate90Degree) > 0) {
                                        rectStart.setRotate(rectStart.getRotate() + degreeChanged);
                                    } else {
                                        rectStart.setRotate(rectStart.getRotate() - degreeChanged);
                                    }
                                }
                                
                                checkSuccess();
                            }
                        } else {
                            rectStart.setStrokeWidth(1);
                            // On Attached
                            if (rectStart.contains(localPosition.getX(), localPosition.getY())) {
                                leftNormalOnGrab = newNormal;
                                degreeOnGrab = rectStart.getRotate();
                                isRightAttached = true;
                            }
                        }
                    }
//                }
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
    
    private Point3D getNormalVectorFromLeapHand(Triangle hand) {
        Point3D thumb = mapper.leapToApp(hand.getThumb());
        Point3D index = mapper.leapToApp(hand.getIndex());
        Point3D middle = mapper.leapToApp(hand.getMiddle());
        return Mathematics.CalculateNormalVector(thumb, index, middle);
    }
    
    private void clearPlane(Polygon plane) {
        rectStart.setStrokeWidth(1);
        if (plane == polyLeft) {
            isLeftAttached = false;
        }
        
        if (plane == polyRight) {
            isRightAttached = false;
        }
        
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
        double degreeEnd = (rectEnd.getRotate() + 360)% 360; // make sure the value is positive
        double degreeStart = (rectStart.getRotate() +360) % 360;
        
        if ( isSuccess == false && degreeStart < degreeEnd + 1 && degreeStart > degreeEnd - 1) {
            isSuccess = true;
            removeListeners();
            listener.disableRecord();
            listener.stopDetect();
            
            timeEnd = System.currentTimeMillis();
            int time = (int) (timeEnd - timeStart);
            
            System.out.println("[R] Current step: " + listener.getDataManager().getExperiment().getStep());
            listener.getDataManager().updateExperimentRecords(modeID, time);
            
            int currentStep = listener.getDataManager().getCurrentStep();
            System.out.println("[R] Mode " + listener.getDataManager().getExperiment().getModeRecord(currentStep-1));
            System.out.println("[R] Time " + listener.getDataManager().getExperiment().getTimeRecord(currentStep-1));
            
            FadeTransition ft = new FadeTransition(Duration.millis(fadeOffDuration), root);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setOnFinished((event) -> {
                
                try {
                    System.out.println("case : " + currentStep);
                    switch (currentStep) {
                    case 4: {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.ROTATION_SCREEN2));
                        
                        BorderPane screen = loader.load();
                        RotationController rc = loader.getController();
                        rc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                         
                        break;
                    }
                    
                    case 5:{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.ROTATION_SCREEN3));
                        
                        BorderPane screen = loader.load();
                        RotationController rc = loader.getController();
                        rc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                     
                        break;                        
                    }
                    
                    case 6: {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.SCALING_SCREEN1));
                        
                        BorderPane screen = loader.load();
                        ScalingController sc = loader.getController();
                        sc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                        
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
