package application.shaders;

import org.lwjgl.util.vector.Matrix4f;

import application.entity.Camera;
import application.entity.Light;
import application.utility.Maths;

public class TextureShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/application/shaders/txt_vertex.glsl";
    private static final String FRAGMENT_FILE = "src/application/shaders/txt_fragment.glsl";

    private int location_transformationMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    
    private int location_lightPosition;
    private int location_lightColor;

    public TextureShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super
                .getUniformLocation("transformationMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_projectionMatrix = super
                .getUniformLocation("projectionMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
    }

    // Load to the shader
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    
    // Load to the shader
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    // Load to the shader
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    
    public void loadLight(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }
    
}
