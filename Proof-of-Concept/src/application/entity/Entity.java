package application.entity;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import application.models.RawModel;
import application.utility.Maths;

public abstract class Entity {

    protected Vector3f position;
    protected float rx, ry, rz, scale;

    protected Matrix4f translationMatrix;
    protected Matrix4f rotationMatrix;
    protected Matrix4f scaleMatrix;
    protected Matrix4f transformationMatrix;

    public Entity(Vector3f position, float rx, float ry, float rz, float scale) {
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;

        translationMatrix = new Matrix4f();
        translationMatrix.setIdentity();
        translationMatrix.translate(position);

        rotationMatrix = new Matrix4f();
        rotationMatrix.setIdentity();
        rotationMatrix
                .rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        rotationMatrix
                .rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        rotationMatrix
                .rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));

        scaleMatrix = new Matrix4f();
        scaleMatrix.setIdentity();
        scaleMatrix.scale(new Vector3f(scale, scale, scale));

        transformationMatrix = new Matrix4f();
        transformationMatrix.setIdentity();
        updateTransformationMatrix();
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    protected void updateTransformationMatrix() {
        // T >> R >> S
        Matrix4f.mul(translationMatrix, rotationMatrix, transformationMatrix);
        Matrix4f.mul(transformationMatrix, scaleMatrix, transformationMatrix);
    }

    public void globalTranslate(Vector3f vec) {
        Matrix4f temp = new Matrix4f();
        temp.setIdentity();
        temp.translate(vec);
        
//        Matrix4f.mul(translationMatrix, temp, translationMatrix);
        Matrix4f.mul(temp, translationMatrix, translationMatrix);
        updateTransformationMatrix();
    }

    public void globalRotate(float angle, Vector3f axis) {
        Matrix4f temp = new Matrix4f();
        temp.setIdentity();
        temp.rotate(angle, axis);

        // the order applying this matrix is important!
        Matrix4f.mul(temp, rotationMatrix, rotationMatrix);
        updateTransformationMatrix();
    }

    public void gloableScale(float factor) {
        scaleMatrix.scale(new Vector3f(1 + factor, 1 + factor, 1 + factor));
        updateTransformationMatrix();
    }

    public void localRotate(float angle, Vector3f axis) {
        transformationMatrix.rotate(angle, axis);
        // updateTransformationMatrix();
    }

    public void increasePosition(float deltaX, float deltaY, float deltaZ) {
        position.x += deltaX;
        position.y += deltaY;
        position.z += deltaZ;
    }

    public void increaseRotation(float deltaX, float deltaY, float deltaZ) {
        rx += deltaX;
        ry += deltaY;
        rz += deltaZ;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
