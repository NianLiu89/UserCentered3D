package application.utility;

public class Data {

    public static final boolean IS_TEXTURED = true;
    
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    
    public static final float[] VERTICES = {
//        -0.5f,0.5f,0,   //V0
//        -0.5f,-0.5f,0,  //V1
//        0.5f,-0.5f,0,   //V2
//        0.5f,0.5f,0     //V3
        -0.5f,0.5f,-0.5f,   
        -0.5f,-0.5f,-0.5f,  
        0.5f,-0.5f,-0.5f,   
        0.5f,0.5f,-0.5f,        
        
        -0.5f,0.5f,0.5f,    
        -0.5f,-0.5f,0.5f,   
        0.5f,-0.5f,0.5f,    
        0.5f,0.5f,0.5f,
        
        0.5f,0.5f,-0.5f,    
        0.5f,-0.5f,-0.5f,   
        0.5f,-0.5f,0.5f,    
        0.5f,0.5f,0.5f,
        
        -0.5f,0.5f,-0.5f,   
        -0.5f,-0.5f,-0.5f,  
        -0.5f,-0.5f,0.5f,   
        -0.5f,0.5f,0.5f,
        
        -0.5f,0.5f,0.5f,
        -0.5f,0.5f,-0.5f,
        0.5f,0.5f,-0.5f,
        0.5f,0.5f,0.5f,
        
        -0.5f,-0.5f,0.5f,
        -0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,0.5f
    };
    
    public static final float[] RGBS = {
        1f, 0f, 0f,
        1f, 1f, 0f, 
        0f, 0f, 1f,
        0f, 1f, 1f
    };
    
    public static final float[] TEXTURES = {
//        0,0,
//        0,1,
//        1,1,
//        1,0
        0,0,
        0,1,
        1,1,
        1,0,            
        0,0,
        0,1,
        1,1,
        1,0,            
        0,0,
        0,1,
        1,1,
        1,0,
        0,0,
        0,1,
        1,1,
        1,0,
        0,0,
        0,1,
        1,1,
        1,0,
        0,0,
        0,1,
        1,1,
        1,0
    };
    
    public static final int[] INDICES = {
//        0,1,3,  //Top left triangle (V0,V1,V3)
//        3,1,2   //Bottom right triangle (V3,V1,V2)
        0,1,3,  
        3,1,2,  
        4,5,7,
        7,5,6,
        8,9,11,
        11,9,10,
        12,13,15,
        15,13,14,   
        16,17,19,
        19,17,18,
        20,21,23,
        23,21,22
    };
}
