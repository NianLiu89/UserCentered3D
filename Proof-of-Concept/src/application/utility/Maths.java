package application.utility;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.leapmotion.leap.Vector;

import application.entity.Camera;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation,
            float rx, float ry, float rz, float scale) {

        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0),
                matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0),
                matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1),
                matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        return matrix;
    }

    // Here everything is opposite
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()),
                new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(
                0, 1, 0), matrix, matrix);
        Vector3f position = camera.getPosition();
        Vector3f negativePosition = new Vector3f(-position.x, -position.y,
                -position.z);
        Matrix4f.translate(negativePosition, matrix, matrix);

        return matrix;
    }

    /**
     * 
     * @param v0
     *            start position
     * @param v1
     *            end position
     * @return delta distance
     */
    public static Vector3f deltaTranslation(Vector vStart, Vector vEnd) {
        float deltaX = vEnd.getX() - vStart.getX();
        float deltaY = vEnd.getY() - vStart.getY();
        float deltaZ = vEnd.getZ() - vStart.getZ();
        return new Vector3f(deltaX, deltaY, deltaZ);
    }

    public static float deltaDegreeX(Vector start, Vector end) {
        Vector xAxis = new Vector(0, 1, 0);
        return xAxis.angleTo(new Vector(0, end.getY(), end.getZ()))
                - xAxis.angleTo(new Vector(0, start.getY(), start.getZ()));
    }
    
    public static float deltaDegreeY(Vector start, Vector end) {
        Vector yAxis = new Vector(0, 0, 1);
        return yAxis.angleTo(new Vector(end.getX(), 0, end.getZ()))
                - yAxis.angleTo(new Vector(start.getX(), 0, start.getZ()));
    }
    
    public static float deltaDegreeZ(Vector start, Vector end) {
        Vector zAxis = new Vector(1, 0, 0);
        return zAxis.angleTo(new Vector(end.getX(), end.getY(), 0))
                - zAxis.angleTo(new Vector(start.getX(), start.getY(), 0));
    }
}
