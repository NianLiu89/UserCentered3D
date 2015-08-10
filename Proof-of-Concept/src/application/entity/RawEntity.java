package application.entity;

import org.lwjgl.util.vector.Vector3f;

import application.models.RawModel;

public class RawEntity extends Entity {

    private RawModel model;

    public RawEntity(RawModel model, Vector3f position, float rx,
            float ry, float rz, float scale) {
        super(position, rx, ry, rz, scale);
        this.model = model;
    }

    public RawModel getModel() {
        return model;
    }

    public void setModel(RawModel model) {
        this.model = model;
    }

}
