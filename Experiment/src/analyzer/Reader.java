package analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javafx.geometry.Point3D;
import analyzer.data.ExData;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;

public class Reader {

    private File[] files;
    
    public Reader(boolean isTop) {
        String path = isTop ? "data/raw/" : "data/rawBot/";
        File folder = new File(path);
        files = folder.listFiles();
    }
    
    public File[] getFiles() {
        return files;
    }
    
    public File getFile(int index) {
        return files[index];
    }
    
    public ExData getExData(int index) {
        Path path = files[index].toPath();
        return getExData(path);
    }
    
    public ExData getExData(String pathString) {
        Path path = Paths.get(pathString);
        return getExData(path);
    }
    
    public ExData getExData(Path path) {
        // just for deserialization
        Controller controller = new Controller();
        
        ExData exData = new ExData();
        int counter = 0;
        try {
            byte[] data = Files.readAllBytes(path);
           
            int c = 0;
            int nextBlockSize = 0;
            
            if(data.length > 4) {
                nextBlockSize = (data[c++] & 0x000000ff) << 24 |
                        (data[c++] & 0x000000ff) << 16 |
                        (data[c++] & 0x000000ff) <<  8 |
                        (data[c++] & 0x000000ff);
            } else {
                System.out.println("Not sufficient data.");
                return null;
            }
            
            while (c + nextBlockSize <= data.length) {
                byte[] frameData = Arrays.copyOfRange(data, c, c + nextBlockSize);
                c += nextBlockSize;
                
                Frame newFrame = new Frame();
                newFrame.deserialize(frameData);
                exData.addData(newFrame);
                counter++;
                Vector v = newFrame.fingers().get(0).tipVelocity();
                Point3D p = new Point3D(v.getX(), v.getY(), v.getZ());
//                System.out.println(newFrame.hands().count());
              
                if(data.length - c > 4) {
                    nextBlockSize = (data[c++] & 0x000000ff) << 24 |
                            (data[c++] & 0x000000ff) << 16 |
                            (data[c++] & 0x000000ff) <<  8 |
                            (data[c++] & 0x000000ff);
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error reading file: " + e);
        }
        
        System.out.println(counter + " frames loaded.");
        return exData;
    }
    
    
}
