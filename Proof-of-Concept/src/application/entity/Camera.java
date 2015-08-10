package application.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import sun.security.acl.WorldGroupImpl;

public class Camera {

    private Vector3f position;

    private float pitch;
    private float yaw;
    private float roll;

    public Camera(Vector3f position, float pitch, float yaw, float roll) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public Camera(Vector3f position) {
        this(position, 0, 0, 0);
    }

    public Camera() {
        this(new Vector3f(0, 0, 0), 0, 0, 0);
    }

    public void update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.02f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}
