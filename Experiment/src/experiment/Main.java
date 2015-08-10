package experiment;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture.Type;

import experiment.model.DataManager;
import experiment.model.Triangle;
import experiment.model.TrianglePair;
import experiment.model.Record;
import experiment.model.LeapListener;
import experiment.utility.CoordinateMapper;
import experiment.utility.Mathematics;
import experiment.view.InitializationController;


public class Main extends Application {
	
	private DataManager dataManager;
	private Controller controller;
	private LeapListener listener;
	
	private Polygon polyLeft;
	private Polygon polyRight;
	private CoordinateMapper mapper;
	
	@Override
	public void start(Stage primaryStage) {
	    initLeapMotion();
        registerModeSwicher(primaryStage);
	        
	    primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, (e) -> {
	        if (e.getCode() == KeyCode.ESCAPE) {
	            listener.disableRecord();
	            
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(Mathematics.FXML_URL + Mathematics.INITIALIZATION_SCREEN));
	            
	            BorderPane screen;
                try {
                    screen = loader.load();
                    StackPane root = (StackPane) primaryStage.getScene().getRoot();
                    root.getChildren().clear();
                    root.getChildren().add(screen);     
                    
                    InitializationController ic = loader.getController();
                    ic.initController(primaryStage, listener);
                    
                    polyLeft = new Polygon();
                    polyLeft.setFill(Color.web("#1e90ff"));
                    polyRight = new Polygon();
                    polyRight.setFill(Color.web("#ffff00"));
                    screen.getChildren().addAll(polyLeft, polyRight);
                    
                    listener.startDetect();
                    listener.handPairProperty().addListener(handPairListener);
                    mapper = new CoordinateMapper(primaryStage.getWidth(), primaryStage.getHeight(), 500);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
	        }
	    });
	    
	    //registerSwicher(primaryStage);
	    
		try {
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		    
		    StackPane root = new StackPane();
		    FXMLLoader loader = new FXMLLoader(getClass().getResource(Mathematics.FXML_URL + Mathematics.INITIALIZATION_SCREEN));
		    
		    BorderPane screen = loader.load();
		    root.getChildren().add(screen);
		    
		    InitializationController ic = loader.getController();
		    ic.initController(primaryStage, listener);
		    
		    polyLeft = new Polygon();
		    polyLeft.setFill(Color.web("#1e90ff"));
		    polyRight = new Polygon();
		    polyRight.setFill(Color.web("#ffff00"));
		    screen.getChildren().addAll(polyLeft, polyRight);
		    
		    Scene scene = new Scene(root);
		    
		    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		    primaryStage.setFullScreen(true);
		    primaryStage.setScene(scene);
		    primaryStage.show();
		    
		    listener.startDetect();
		    listener.handPairProperty().addListener(handPairListener);
		    mapper = new CoordinateMapper(primaryStage.getWidth(), primaryStage.getHeight(), 500);
		    
		    
		    
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
	    controller.removeListener(listener);
	    dataManager.closeFileWriter();
	    System.out.println("Window Closed");
	}
	
	public static void main(String[] args) {
		launch(args);
//	    DataManager dm = new DataManager();
//	    dm.loadFromFile("RM20150503125022.leap");
//	    System.exit(0);
	}
	
	private void initLeapMotion() {
	    controller = new Controller();
	    dataManager = new DataManager();
	    listener = new LeapListener(dataManager);
	    
        controller.enableGesture(Type.TYPE_CIRCLE);
        controller.enableGesture(Type.TYPE_KEY_TAP);
        controller.enableGesture(Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Type.TYPE_SWIPE);
        
        controller.addListener(listener);
	}
	
	/**
	 * Press ENTER to switch between Detect/Record Mode and Detect-Only Mode.
	 */
	private void registerModeSwicher(Stage stage) {
	    stage.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
	        if (event.getCode() == KeyCode.ENTER) {
	            listener.switchMode();
	        }
	        
	        if (event.getCode() == KeyCode.DIGIT1) {
	            stage.setFullScreen(true);
	        }
	        
	        if (event.getCode() == KeyCode.DIGIT2) {
	            stage.setFullScreen(false);
	        }
	    });
	}

	 private ChangeListener<TrianglePair> handPairListener = (observable, oldValue, newValue) -> {
	        Platform.runLater(() -> {
	            if (mapper != null) {
	                updatePlaneCenter(polyLeft, newValue.getLeft());
	                updatePlaneCenter(polyRight, newValue.getRight());
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
    
    private void clearPlane(Polygon plane) {
        plane.getPoints().clear();
    }
	
	
    private void registerSwicher(Stage stage) {
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                if (listener.isDetecting()) {
                    listener.stopDetect();
                } else {
                    listener.startDetect();
                }
            }
        });
    }
	
}
