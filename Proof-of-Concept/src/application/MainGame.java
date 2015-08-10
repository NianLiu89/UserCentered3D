package application;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.leapmotion.leap.Controller;

import application.entity.Camera;
import application.entity.Cube;
import application.entity.Entity;
import application.entity.RawEntity;
import application.entity.TexturedEntity;
import application.models.TextureModel;
import application.models.RawModel;
import application.models.TexturedModel;
import application.renderEngine.DisplayManager;
import application.renderEngine.Loader;
import application.renderEngine.OBJLoader;
import application.renderEngine.Renderer;
import application.shaders.RgbShader;
import application.shaders.ShaderProgram;
import application.shaders.TextureShader;
import application.utility.Data;

public class MainGame {

    public static void main(String[] args) {

        Controller leapController = new Controller();
        DisplayManager.createDisplay();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Loader loader = new Loader();
        TextureShader shader = new TextureShader();
        Renderer renderer = new Renderer(shader);

        RawModel rawModel = loader.loadToVAOWithTexture(Data.VERTICES,
                Data.TEXTURES, Data.INDICES);
        TexturedModel texturedModel = new TexturedModel(rawModel,
                new TextureModel(loader.loadTexture("glass")));
        TexturedEntity texturedEntity = new TexturedEntity(
                texturedModel, new Vector3f(0, 0, 0f), 0, 0, 0, 1);

        TexturedModel texturedModel1 = new TexturedModel(rawModel,
                new TextureModel(loader.loadTexture("brick")));

        Camera camera = new Camera(new Vector3f(0, 0, 2f));
        Cube cube = new Cube(texturedModel, new Vector3f(0f, 0f, 0f), 45, 45,
                0, .5f);
//        Cube cubeOrigin = new Cube(texturedModel1, new Vector3f(0f, 0, 0f), 0, 0, 0,
//                .2f);
        
        while (!Display.isCloseRequested()) {
            camera.update();
            cube.update(leapController);
            
//            long time = System.nanoTime();
            renderer.prepare(leapController);

            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(cube, shader);
            
//            renderer.render(cubeOrigin, shader);
//            renderer.renderWithNormal(texturedEntity, shader);
            shader.stop();

            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
