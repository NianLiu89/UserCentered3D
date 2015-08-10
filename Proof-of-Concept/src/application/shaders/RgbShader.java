package application.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class RgbShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/application/shaders/rgb_vertex.glsl";
    private static final String FRAGMENT_FILE = "src/application/shaders/rgb_fragment.glsl";
    
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    
    public RgbShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "color");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super
                .getUniformLocation("transformationMatrix");
        location_projectionMatrix = super
                .getUniformLocation("projectionMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }



}
