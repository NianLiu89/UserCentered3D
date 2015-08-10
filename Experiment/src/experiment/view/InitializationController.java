package experiment.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import experiment.model.LeapListener;
import experiment.utility.Mathematics;

public class InitializationController implements Initializable {

    @FXML
    private RadioButton radLeftHand;
    
    @FXML
    private RadioButton radRightHand;
    
    @FXML
    private Button btnConfirm;
    
    private final ToggleGroup dominantHandGroup;
    
    private Stage stage;
    private LeapListener listener;
    
    public InitializationController() {
        dominantHandGroup = new ToggleGroup();
    }
    
    /**
     * This method is automatically called 
     * after the fxml file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radLeftHand.setToggleGroup(dominantHandGroup);
        radRightHand.setToggleGroup(dominantHandGroup);
        //dominantHandGroup.selectToggle(radRightHand);
        
    }
    
    @FXML
    private void handleConfirm(ActionEvent e) {
        if (dominantHandGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Selection");
            alert.setHeaderText(null);
            alert.setContentText("The selection cannot be empty!");
            alert.showAndWait();
            return;
        }
        
        listener.stopDetect();
        initExperiment();
        switchScreen();
    }
    
    private void switchScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + Mathematics.FXML_URL + Mathematics.TRANSLATION_SCREEN1));
            
            BorderPane screen = loader.load();
            TranslationController tc = loader.getController();
            tc.initController(stage, listener);
            
            StackPane root = (StackPane) stage.getScene().getRoot();
            root.getChildren().clear();
            root.getChildren().add(screen);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initExperiment() {
        int dominantHand = radLeftHand.isSelected() ? Mathematics.LEFT : Mathematics.RIGHT;
        listener.getDataManager().initExperiment(dominantHand);
        listener.getDataManager().initFileWriter();
    }
    
    public void initController(Stage stage, LeapListener listener) {
        this.stage = stage;
        this.listener = listener;
    }
    
    
}
