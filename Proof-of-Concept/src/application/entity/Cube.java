package application.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;

import application.models.TexturedModel;
import application.utility.Leap;
import application.utility.Maths;

public class Cube extends TexturedEntity {

    private final float keyboardTranslateSpeed = 0.01f;
    private final float keyboardRotateSpeed = 0.02f;
    private final float keyboardScaleSpeed = 0.01f;

    private Frame frameOnStart;

    private Matrix4f translationMatrixOnStart;
    private Matrix4f rotationMatrixOnStart;
    private Matrix4f scaleMatrixOnStart;

    private boolean isTranslating;
    private boolean isRotating;
    private boolean isScaling;

    private float leapToAppFactor = 0.005f;

    public Cube(TexturedModel model, Vector3f position, float rx, float ry,
            float rz, float scale) {
        super(model, position, rx, ry, rz, scale);
        frameOnStart = null;
        translationMatrixOnStart = new Matrix4f();
        rotationMatrixOnStart = new Matrix4f();
        scaleMatrixOnStart = new Matrix4f();
        isTranslating = false;
        isRotating = false;
        isScaling = false;
    }

    public void update(Controller leapController) {
        checkKeyboardInputs();
        checkLeapInputs(leapController);

    }

    private void checkKeyboardInputs() {
        // Reset
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            translationMatrix.setIdentity();
            rotationMatrix.setIdentity();
            scaleMatrix.setIdentity();
            updateTransformationMatrix();
        }

        // Translate
        if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
            globalTranslate(new Vector3f(0, 0, -keyboardTranslateSpeed));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            globalTranslate(new Vector3f(0, 0, +keyboardTranslateSpeed));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            globalTranslate(new Vector3f(-keyboardTranslateSpeed, 0, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            globalTranslate(new Vector3f(+keyboardTranslateSpeed, 0, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
            globalTranslate(new Vector3f(0, +keyboardTranslateSpeed, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            globalTranslate(new Vector3f(0, -keyboardTranslateSpeed, 0));
        }

        // Rotate
        if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
            globalRotate(-keyboardRotateSpeed, new Vector3f(1, 0, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            globalRotate(+keyboardRotateSpeed, new Vector3f(1, 0, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            globalRotate(-keyboardRotateSpeed, new Vector3f(0, 1, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            globalRotate(+keyboardRotateSpeed, new Vector3f(0, 1, 0));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            globalRotate(+keyboardRotateSpeed, new Vector3f(0, 0, 1));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            globalRotate(-keyboardRotateSpeed, new Vector3f(0, 0, 1));
        }

        // Scale
        if (Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
            gloableScale(keyboardScaleSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
            gloableScale(-keyboardScaleSpeed);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
            rx -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
            rx += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
            ry -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            ry += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
            rz -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            rz += 0.5f;
        }
    }

    private void checkLeapInputs(Controller leapController) {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                // Press events
                switch (Keyboard.getEventKey()) {
                case Keyboard.KEY_1:
                    if (!isRotating && !isScaling
                            && getRightHand(leapController.frame()).isValid()) {
                        frameOnStart = leapController.frame();
                        translationMatrixOnStart = translationMatrix;
                        isTranslating = true;
                        System.out.println("Translation started.");
                    }
                    break;
                case Keyboard.KEY_2:
                    if (!isTranslating && !isScaling
                            && getRightHand(leapController.frame()).isValid()) {
                        frameOnStart = leapController.frame();
                        rotationMatrixOnStart = rotationMatrix;
                        isRotating = true;
                        System.out.println("Rotation started.");
                    }
                    break;
                case Keyboard.KEY_3:
                    if (!isTranslating && !isScaling
                            && getRightHand(leapController.frame()).isValid()) {
                        frameOnStart = leapController.frame();
                        scaleMatrixOnStart = scaleMatrix;
                        isScaling = true;
                        System.out.println("Scaling started.");
                    }
                    break;
                default:
                    break;
                }
            } else {
                // Release events
                switch (Keyboard.getEventKey()) {
                case Keyboard.KEY_1:
                    isTranslating = false;
                    System.out.println("Translation stopoed.");
                    break;
                case Keyboard.KEY_2:
                    isRotating = false;
                    System.out.println("Rotation stopped.");
                    break;
                case Keyboard.KEY_3:
                    isScaling = false;
                    System.out.println("Scaling stopped.");
                    break;
                default:
                    break;
                }

                frameOnStart = null;
            }
        }

        if (frameOnStart != null) {
            // Status on start
            InteractionBox iboxOnStart = frameOnStart.interactionBox();
            Hand handOnStart = getRightHand(frameOnStart);
            Finger thumbOnStart = handOnStart.fingers()
                    .fingerType(Type.TYPE_THUMB).get(0);
            Finger indexOnStart = handOnStart.fingers()
                    .fingerType(Type.TYPE_INDEX).get(0);
            Finger middleOnStart = handOnStart.fingers()
                    .fingerType(Type.TYPE_MIDDLE).get(0);

            // Status now
            InteractionBox ibox = leapController.frame().interactionBox();
            Hand hand = getRightHand(leapController.frame());
            Finger thumb = hand.fingers().fingerType(Type.TYPE_THUMB).get(0);
            Finger index = hand.fingers().fingerType(Type.TYPE_INDEX).get(0);
            Finger middle = hand.fingers().fingerType(Type.TYPE_MIDDLE).get(0);

            if (isTranslating) {
                Vector start = iboxOnStart.normalizePoint(
                        indexOnStart.tipPosition(), false);
                Vector end = ibox.normalizePoint(index.tipPosition(), false);

                Vector3f deltaTranslation = Maths.deltaTranslation(
                        start.times(leapToAppFactor),
                        end.times(leapToAppFactor));

                Matrix4f t = new Matrix4f();
                t.setIdentity();
                t.translate(deltaTranslation);
                Matrix4f.mul(t, translationMatrixOnStart, translationMatrix);
                updateTransformationMatrix();
            }

            if (isRotating) {
                // Coordinate System on start
                Vector directIndexToThumbOnStart = thumbOnStart.tipPosition()
                        .minus(indexOnStart.tipPosition());
                Vector directIndexToMiddleOnStart = middleOnStart.tipPosition()
                        .minus(indexOnStart.tipPosition());
                Vector xAxisOnStart = directIndexToThumbOnStart.normalized();
                Vector yAxisOnStart = directIndexToThumbOnStart
                        .cross(directIndexToMiddleOnStart).normalized();
                Vector zAxisOnStart = yAxisOnStart.cross(xAxisOnStart).normalized();

                // Current Coordinate System
                Vector directIndexToThumb = thumb.tipPosition().minus(
                        index.tipPosition());
                Vector directIndexToMiddle = middle.tipPosition().minus(
                        index.tipPosition());
                Vector xAxis = directIndexToThumb.normalized();
                Vector yAxis = directIndexToThumb.cross(directIndexToMiddle).normalized();
                Vector zAxis = yAxis.cross(xAxis).normalized();

                float deltaX = Maths.deltaDegreeX(xAxisOnStart, xAxis);
                float deltaY = Maths.deltaDegreeY(yAxisOnStart, yAxis);
                float deltaZ = Maths.deltaDegreeZ(zAxisOnStart, zAxis);
                
                Matrix4f temp = new Matrix4f();
                temp.setIdentity();
                temp.rotate(deltaX*leapToAppFactor, new Vector3f(1, 0, 0));
                temp.rotate(deltaY*leapToAppFactor, new Vector3f(0, 1, 0));
                temp.rotate(-deltaZ*leapToAppFactor, new Vector3f(0, 0, 1));

                // the order applying this matrix is important!
                Matrix4f.mul(temp, rotationMatrixOnStart, rotationMatrix);
                updateTransformationMatrix();
            }

            if (isScaling) {
                float distanceOnStart = thumbOnStart.tipPosition().normalized().distanceTo(indexOnStart.tipPosition().normalized());
                float distance = thumb.tipPosition().normalized().distanceTo(index.tipPosition().normalized());
                float deltaDistance = distance - distanceOnStart;
                
                scaleMatrix.scale(new Vector3f(1 + deltaDistance*leapToAppFactor, 1 + deltaDistance*leapToAppFactor, 1 + deltaDistance*leapToAppFactor));
                updateTransformationMatrix();
                
            }
        }
    }

    private Hand getRightHand(Frame frame) {
        return frame.hands().get(0).isRight() ? frame.hands().get(0) : frame
                .hands().get(1);
    }
}
