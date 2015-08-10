package application.entity;

import org.lwjgl.util.vector.Vector3f;

import application.models.TexturedModel;

public class TexturedEntity extends Entity {

    private TexturedModel model;

    public TexturedEntity(TexturedModel model, Vector3f position,
            float rx, float ry, float rz, float scale) {
        super(position, rx, ry, rz, scale);
        this.model = model;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

}
