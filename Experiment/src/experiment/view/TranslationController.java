package experiment.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import experiment.model.Triangle;
import experiment.model.TrianglePair;
import experiment.model.LeapListener;
import experiment.utility.CoordinateMapper;
import experiment.utility.Mathematics;

public class TranslationController implements Initializable {

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
    private boolean isLeftAttached;
    private boolean isRightAttached;
    private boolean isTwoAttached;
    
    private double localDistanceToOriginX;
    private double localDistanceToOriginY;
    
    private long timeStart;
    private long timeEnd;
    private int modeID;     // 1 left, 2 right, 3 both
    private int fadeOffDuration = Mathematics.FADEOFF_DURATION;
    private boolean isSuccess;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isLeftAttached = false;
        isRightAttached = false;
        isTwoAttached = false;
        timeStart = 0;
        timeEnd = 0;
        modeID = 0;
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
                
                // Two hands
                if (centerLeft != null && centerRight != null) {

                    modeID = 3;
                    Point3D middlePoint = centerLeft.midpoint(centerRight);
                    Point3D localCenter = rectStart.parentToLocal(middlePoint);
                    
                    Point3D leftIndex = mapper.leapToApp(newValue.getLeft().getIndex());
                    Point3D rightIndex = mapper.leapToApp(newValue.getRight().getIndex());
                    
                    isTwoAttached = isTwoHandsAttarched(leftIndex, rightIndex);
                    
                    if (isTwoAttached) {
                        rectStart.setStrokeWidth(4);
                        // Move
                        double x = getValidX(middlePoint);
                        double y = getValidY(middlePoint);
                        rectStart.setLayoutX(x);
                        rectStart.setLayoutY(y);
                        checkSuccess();
                    } else {
                        rectStart.setStrokeWidth(1);
                        // The distance from Attached Point to local origin
                        localDistanceToOriginX = localCenter.getX();
                        localDistanceToOriginY = localCenter.getY();
                    }
                    
                } else {
                    
                    // One hand
                    if (centerLeft != null) {
                        modeID = 1;
                        Point3D localPosition = rectStart.parentToLocal(centerLeft);
                        
                        if (isLeftAttached) {
                            rectStart.setStrokeWidth(4);
                            double x = getValidX(centerLeft);
                            double y = getValidY(centerLeft);
                            rectStart.setLayoutX(x);
                            rectStart.setLayoutY(y);
                            checkSuccess();
                        } else {
                            if (rectStart.contains(localPosition.getX(), localPosition.getY())) {
                                localDistanceToOriginX = localPosition.getX();
                                localDistanceToOriginY = localPosition.getY();
                                isLeftAttached = true;
                            }
                        }
                    }
                    
                    if (centerRight != null) {
                        modeID = 2;
                        Point3D localPosition = rectStart.parentToLocal(centerRight);
                        
                        if (isRightAttached) {
                            rectStart.setStrokeWidth(4);
                            double x = getValidX(centerRight);
                            double y = getValidY(centerRight);
                            rectStart.setLayoutX(x);
                            rectStart.setLayoutY(y);
                            checkSuccess();
                        } else {
                            if (rectStart.contains(localPosition.getX(), localPosition.getY())) {
                                localDistanceToOriginX = localPosition.getX();
                                localDistanceToOriginY = localPosition.getY();
                                isRightAttached = true;
                            }
                        }
                    }
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
    
    private double getValidX(Point3D position) {
        double x = position.getX() - localDistanceToOriginX;
        return x < (playground.getWidth() - rectStart.getWidth()) && x > 0 ? x : x > (playground.getWidth() - rectStart.getWidth()) ? (playground.getWidth() - rectStart.getWidth()) : 0;
    }
    
    private double getValidY(Point3D position) {
        double y = position.getY() - localDistanceToOriginY;
        return y < (playground.getHeight() - rectStart.getHeight()) && y > 0 ? y : y > (playground.getHeight() - rectStart.getHeight()) ? (playground.getHeight() - rectStart.getHeight()) : 0;
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
        stage.removeEventFilter(KeyEvent.KEY_RELEASED, spaceHandle);
        listener.handPairProperty().removeListener(handPairListener);
    }
    
    private void checkSuccess() {
        Bounds inter = Shape.intersect(rectStart, rectEnd).getBoundsInLocal();
        Bounds end = rectEnd.getBoundsInLocal();
        double interArea = inter.getHeight() * inter.getWidth();
        double endArea = end.getHeight() * end.getWidth();
        if (isSuccess == false && interArea < endArea + 1500 && interArea > endArea - 1500) {
            isSuccess = true;
            removeListeners();
            listener.disableRecord();
            listener.stopDetect();
            
            timeEnd = System.currentTimeMillis();
            int time = (int) (timeEnd - timeStart);
            
            System.out.println("[T] Current step: " + listener.getDataManager().getExperiment().getStep());
            listener.getDataManager().updateExperimentRecords(modeID, time);
            
            int currentStep = listener.getDataManager().getCurrentStep();
            System.out.println("[T] Mode " + listener.getDataManager().getExperiment().getModeRecord(currentStep-1));
            System.out.println("[T] Time " + listener.getDataManager().getExperiment().getTimeRecord(currentStep-1));
            
            
            FadeTransition ft = new FadeTransition(Duration.millis(fadeOffDuration), root);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setOnFinished((event) -> {
                
                try {
                    switch (currentStep) {
                    case 1: {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.TRANSLATION_SCREEN2));
                        
                        BorderPane screen = loader.load();
                        TranslationController tc = loader.getController();
                        tc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                    
                        break;
                    }
                    
                    case 2:{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.TRANSLATION_SCREEN3));
                        
                        BorderPane screen = loader.load();
                        TranslationController tc = loader.getController();
                        tc.initController(stage, listener);
                        
                        StackPane root = (StackPane) stage.getScene().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(screen);                      
                        break;                        
                    }
                    
                    case 3: {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.ROTATION_SCREEN1));
                        
                        BorderPane screen = loader.load();
                        RotationController rc = loader.getController();
                        rc.initController(stage, listener);
                        
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
