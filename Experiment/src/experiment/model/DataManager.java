package experiment.model;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

public class DataManager {

    private static final String H2_URL = "jdbc:h2:file:E:/Dropbox/Thesis@CWI 2015/Code/Java/LeapExperiment/data/experiment_database";
    private static final String H2_USER = "NIAN_LIU";
    private static final String H2_PWD = "1234qwer";
    
    private Record experiment;
    private List<Frame> outFrames;
    private List<Frame> inFrames;
    
    private static final String FILE_URL = "data/raw/";
    
    private Path outPath;
    private OutputStream out; 
    
    private int counter;
    
    public DataManager() {
        counter = 0;
        initOutFrames();
    }
    
    public final void initExperiment(int dominantHand) {
        experiment = new Record(dominantHand);
        System.out.println("New Experiment[" + dominantHand + "]");
    }
    
    public final void initOutFrames() {
        outFrames = new ArrayList<Frame>();
    }
    
    public final void saveToDatabase() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            //Register JDBC driver
            Class.forName("org.h2.Driver");
            
            //Connect database
            System.out.println("Connecting to database.");
            conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PWD);
            System.out.println("Connected to database.");
            
            //Insert data
            System.out.println("Inserting data.");
            stmt = conn.createStatement();
            
            String sql = "INSERT INTO EXPERIMENT (DOMINANT_HAND, FILE, "
                    + "MODE1, MODE2, MODE3, MODE4, MODE5, MODE6, MODE7, MODE8, MODE9,"
                    + "TIME1, TIME2 , TIME3 , TIME4 , TIME5 , TIME6 , TIME7 , TIME8 , TIME9)"
                    + "VALUES (" + experiment.getDominantHand() + ",'" + experiment.getFile() + "',"
                    + experiment.getModeRecord(0) + "," + experiment.getModeRecord(1) + "," + experiment.getModeRecord(2) + "," 
                    + experiment.getModeRecord(3) + "," + experiment.getModeRecord(4) + "," + experiment.getModeRecord(5) + "," 
                    + experiment.getModeRecord(6) + "," + experiment.getModeRecord(7) + "," + experiment.getModeRecord(8) + ","
                    + experiment.getTimeRecord(0) + "," + experiment.getTimeRecord(1) + "," + experiment.getTimeRecord(2) + "," 
                    + experiment.getTimeRecord(3) + "," + experiment.getTimeRecord(4) + "," + experiment.getTimeRecord(5) + "," 
                    + experiment.getTimeRecord(6) + "," + experiment.getTimeRecord(7) + "," + experiment.getTimeRecord(8) + ");";
            
            stmt.executeUpdate(sql);
            System.out.println("Inserted data.");
            
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
               if(stmt!=null)
                  conn.close();
            }catch(SQLException se){
            }// do nothing
            
            try{
               if(conn!=null)
                  conn.close();
            }catch(SQLException se){
               se.printStackTrace();
            }
        }

        System.out.println("Connection Closed.");
        
    }
    
    public final void loadFromDatabase() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            Class.forName("org.h2.Driver");
            
            conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PWD);
            stmt = conn.createStatement();
            
            String sql = "SELECT * FROM EXPERIMENT";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                
                String file = rs.getString("FILE");
                boolean dominantHand = rs.getBoolean("DOMINANT_HAND");
                
                System.out.println("FILE " + file);
                System.out.println("DH " + dominantHand);
            }
            
            rs.close();
            
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
        
        System.out.println("Connection Closed.");
        
    }
    
    public final void initFileWriter() {
        if (experiment != null) {
            outPath = Paths.get(FILE_URL + experiment.getFile());
            try {
                out = Files.newOutputStream(outPath);
                System.out.println("Initialized file writer.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Experiment is not instantiated.");
        }
    }
    
    public final void writeToFile(Frame frame) {
        counter++;
        try {
            byte[] serializedFrame = frame.serialize();
            out.write(ByteBuffer.allocate(4).putInt(serializedFrame.length).array());
            out.write(serializedFrame);
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public final void closeFileWriter() {
        System.out.println("Total data count: " + counter);
        counter = 0;
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Null OutputStream! Nothing to close");
        }
    }
    
    
    public final void saveToFile() {
        if (experiment == null) {
            throw new NullPointerException("Experiment is not instantiated.");
        }
        
        if (!outFrames.isEmpty()) {
            try {
                System.out.println("Writing [" + experiment.getFile() + "]");
                
                Path outPath = Paths.get(FILE_URL + experiment.getFile());
                OutputStream out = Files.newOutputStream(outPath);
                
                for (Frame frame : outFrames) {
                    byte[] serializedFrame = frame.serialize();
                    out.write(ByteBuffer.allocate(4).putInt(serializedFrame.length).array());
                    out.write(serializedFrame);
                }
                
                out.flush();
                out.close();
                System.out.println("[" + experiment.getFile() + "] written successfully.");
                
                
                //Reset data container for next use
                //outFrames = new ArrayList<Frame>();
                
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e);
                e.printStackTrace();
            }
            
        } else {
            System.out.println("No data to be saved.");
        }
        
    }
    
    public final void loadFromFile(String file) {
        // just for deserialization
        Controller controller = new Controller();
        //Lazy instantiate
        inFrames = new ArrayList<Frame>();
        
        try {
            Path inFilepath = Paths.get(FILE_URL + file);
            System.out.println(inFilepath);
            byte[] data = Files.readAllBytes(inFilepath);
              
            int c = 0;
            int nextBlockSize = 0;
            
            if(data.length > 4) {
                nextBlockSize = (data[c++] & 0x000000ff) << 24 |
                        (data[c++] & 0x000000ff) << 16 |
                        (data[c++] & 0x000000ff) <<  8 |
                        (data[c++] & 0x000000ff);
            } else {
                System.out.println("Not sufficient data.");
                return;
            }
            
            while (c + nextBlockSize <= data.length) {
                byte[] frameData = Arrays.copyOfRange(data, c, c + nextBlockSize);
                c += nextBlockSize;
                
                Frame newFrame = new Frame();
                newFrame.deserialize(frameData);
                inFrames.add(newFrame);
                
                System.out.println(newFrame.hands().count());
                System.out.println(newFrame.fingers().count());
              
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
        
        System.out.println(inFrames.size() + " frames loaded.");
    }

    public final Record getExperiment() {
        return experiment;
    }
    
    public final void addOutFrame(Frame frame) {
        outFrames.add(frame);
    }

    public final Frame getFrame(int index) {
        return outFrames.get(index);
    }
    
    public final int getOutSize() {
        return outFrames.size();
    }
    
    public final List<Frame> getOutFrames() {
        return outFrames;
    }
    
    public final List<Frame> getInFrames() {
        return inFrames;
    }
    
    public int getCurrentStep() {
        if (experiment != null) {
            return experiment.getStep();
        } else {
            throw new NullPointerException("Experiment does not exisit.");
        }
    }
    
    public void updateExperimentRecords(int mode, int time) {
        experiment.setRecords(mode, time);
    }
    
}

