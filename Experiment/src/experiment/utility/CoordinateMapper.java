package experiment.utility;

import javafx.geometry.Point3D;

public class CoordinateMapper {

//    Default interaction box
    private static final double LEAP_DEFAULT_WIDTH = 250;
    private static final double LEAP_DEFAULT_HEIGHT = 200;
    private static final double LEAP_DEFAULT_DEPTH = 200;
    private static final Point3D LEAP_DEFAULT_CENTER= new Point3D(0, 200, 0);
    
    private double leapWidth;
    private double leapHeight;
    private double leapDepth;
    private Point3D leapCenter;
    
    private double appWidth;
    private double appHeight;
    private double appDepth;
    private Point3D appCenter;
    
    public CoordinateMapper(double appWidth, double appHeight, double appDepth) {
        leapWidth = LEAP_DEFAULT_WIDTH;
        leapHeight = LEAP_DEFAULT_HEIGHT;
        leapDepth = LEAP_DEFAULT_DEPTH;
        leapCenter = LEAP_DEFAULT_CENTER;
        
        setApp(appWidth, appHeight, appDepth);
    }
    
    public Point3D getLeapCenter() {
        return leapCenter;
    }

    public void setLeapCenter(Point3D leapCenter) {
        this.leapCenter = leapCenter;
    }

    public double getLeapWidth() {
        return leapWidth;
    }

    public void setLeapWidth(double leapWidth) {
        this.leapWidth = leapWidth;
    }

    public double getLeapHeight() {
        return leapHeight;
    }

    public void setLeapHeight(double leapHeight) {
        this.leapHeight = leapHeight;
    }

    public double getLeapDepth() {
        return leapDepth;
    }

    public void setLeapDepth(double leapDepth) {
        this.leapDepth = leapDepth;
    }

    public double getAppWidth() {
        return appWidth;
    }

    public void setAppWidth(double appWidth) {
        this.appWidth = appWidth;
    }

    public double getAppHeight() {
        return appHeight;
    }

    public void setAppHeight(double appHeight) {
        this.appHeight = appHeight;
    }

    public double getAppDepth() {
        return appDepth;
    }

    public void setAppDepth(double appDepth) {
        this.appDepth = appDepth;
    }

    public Point3D leapToApp(Point3D pLeap) {
        double pX = 
            pLeap.getX() < (leapCenter.getX() - leapWidth / 2) ? (leapCenter.getX() - leapWidth / 2) : 
            pLeap.getX() > (leapCenter.getX() + leapWidth / 2) ? (leapCenter.getX() + leapWidth / 2) : pLeap.getX();
            
        double pY = 
            pLeap.getY() < (leapCenter.getY() - leapHeight / 2) ? (leapCenter.getY() - leapHeight / 2) : 
            pLeap.getY() > (leapCenter.getY() + leapHeight / 2) ? (leapCenter.getY() + leapHeight / 2) : pLeap.getY();
            
            
        double pZ = 
            pLeap.getZ() < (leapCenter.getZ() - leapDepth / 2) ? (leapCenter.getZ() - leapDepth / 2) : 
            pLeap.getZ() > (leapCenter.getZ() + leapDepth / 2) ? (leapCenter.getZ() + leapDepth / 2) : pLeap.getZ();
            
        double x = (pX - leapCenter.getX()) * appWidth / leapWidth + appCenter.getX();
        double y = appCenter.getY() - (pY - leapCenter.getY()) * appHeight / leapHeight; //The direction of Y dimension is opposite.
        double z = (pZ - leapCenter.getZ()) * appDepth / leapDepth + appCenter.getZ();
        return new Point3D(x, y, z); 
    }
    
    public Point3D appToLeap(Point3D pApp) {
        double x = (pApp.getX() - appCenter.getX()) * leapWidth / appWidth + leapCenter.getX();
        double y = (appCenter.getY() - pApp.getY()) * leapHeight / appHeight + leapCenter.getY(); //The direction of Y dimension is opposite.
        double z = (pApp.getZ() - appCenter.getZ()) * leapDepth / appDepth + leapCenter.getZ();
        return new Point3D(x, y, z);
    }
    
    public final void setApp(double appWidth, double appHeight, double appDepth) {
        this.appWidth = appWidth;
        this.appHeight = appHeight;
        this.appDepth = appDepth;
        appCenter = new Point3D(appWidth/2, appHeight/2 ,0);
    }
    
}
