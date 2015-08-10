package analyzer;

import javafx.geometry.Point3D;
import analyzer.data.ExData;
import analyzer.data.ExFrames;
import analyzer.data.ExHand;
import experiment.utility.Mathematics;

class LostTrackAnalyser {
    
    public LostData analyzeByTask(ExFrames data, int hand) {
        String s = hand == 0 ? "LEFT" : "RIGHT";
        System.out.println("HAND " + s);
        
        int count = 0;
        int countPalmUp = 0;
        int countOutofBox = 0;
        
        double minMinDistance = 0;
        double maxMinDistance = 0;
        double sumMinDistance = 0;
        
        for (int i = 1; i < data.size(); i++) {
            ExHand oldHand = hand == 0 ? data.get(i-1).getLeft() : data.get(i-1).getRight();
            ExHand newHand = hand == 0 ? data.get(i).getLeft() : data.get(i).getRight();

            double newTI = newHand.getThumb().distance(newHand.getIndex());
            double newIM = newHand.getMiddle().distance(newHand.getIndex());
            double newMT = newHand.getThumb().distance(newHand.getMiddle());
            
            double oldTI = oldHand.getThumb().distance(oldHand.getIndex());
            double oldIM = oldHand.getMiddle().distance(oldHand.getIndex());
            double oldMT = oldHand.getThumb().distance(oldHand.getMiddle());
            
            if (newTI + newIM + newMT == 0.0 && oldTI + oldIM + oldMT != 0.0) {
                double currentMinDistance = Math.min(Math.min(oldTI, oldIM), oldMT);
                if (currentMinDistance > 0) {
                    if (minMinDistance > 0 && maxMinDistance > 0) {
                        minMinDistance = currentMinDistance < minMinDistance ? currentMinDistance : minMinDistance;
                        maxMinDistance = currentMinDistance > maxMinDistance ? currentMinDistance : maxMinDistance;
                    } else {
                        minMinDistance = currentMinDistance;
                        maxMinDistance = currentMinDistance;
                    }
                }
                
                System.out.println(Mathematics.CalculateNormalVector(oldHand.getThumb(), oldHand.getIndex(), oldHand.getMiddle()).normalize());
                sumMinDistance += currentMinDistance;
                
                if (isOutofInteractionBox(oldHand)) {
                    countOutofBox++;
                }
                
//                if (isPalmToUp(oldHand)) {
//                    countPalmUp++;
//                }
                
                count++;
            }
        }
        LostData result = new LostData(minMinDistance, maxMinDistance, sumMinDistance, count); 
        printLostData(result);
        System.out.println("##Out >> " + countOutofBox);
        System.out.println();
        return result;
    }
    
    public LostData analyzeByExperiment(ExData exData) {
        LostData overall = null;
        
        for (int i =0; i < 9; i++) {
            System.out.println();
            System.out.println("TASK " + (i+1));
            
            LostData ldL = analyzeByTask(exData.getDataByTaskID(i), 0);
            LostData ldR = analyzeByTask(exData.getDataByTaskID(i), 1);
            
            
            if (overall == null) {
                if (ldL.getMinMinDistance() > 0) {
                    overall = ldL;
                    updateOverall(overall, ldR);
                } else if (ldR.getMinMinDistance() > 0) {
                    overall = ldR;
                }
            } else {
                updateOverall(overall, ldL);
                updateOverall(overall, ldR);
            }
            System.out.println("Time Spent " + exData.getTimeOf(i));
        }
        
        System.out.println();
        System.out.println("OVERALL");
        printLostData(overall);
        return overall;
    }
    
    private void updateOverall(LostData overall, LostData input) {
        if (input.getMinMinDistance() > 0) {
            overall.setMinMinDistance(Math.min(overall.getMinMinDistance(), input.getMinMinDistance()));
            overall.setMaxMinDistance(Math.max(overall.getMaxMinDistance(), input.getMaxMinDistance()));
            overall.setSumMinDistance(overall.getSumMinDistance() + input.getSumMinDistance());
            overall.setCount(overall.getCount() + input.getCount());
        }
    }
    
    private void printLostData(LostData data) {
        if (data.getCount() == 0) {
            System.out.println("Min " + 0 + ", Max " + 0 + ", Avg " + 0 + ", Count " + 0);
        } else {
            System.out.println("Min " + data.getMinMinDistance() + ", Max " + data.getMaxMinDistance() + ", Avg " + data.getSumMinDistance()/data.getCount() + ", Count " + data.getCount());
        }
    }
    
    private boolean isOutofInteractionBox(ExHand hand) {
        Point3D center = Mathematics.CalculateCenterPoint(hand.getThumb(), hand.getIndex(), hand.getMiddle());
        if (center.getX() > 117.5 || center.getX() < -117.5 || center.getY() > 317.5 || center.getY() < 82.5 || center.getZ() > 73.5 || center.getZ() < -73.5) {
            System.out.println("Out of the box!!!");
            return true;
        } else {
            return false;
        }
        
    }
    
    private boolean isPalmToUp(ExHand hand) {
        Point3D normal = Mathematics.CalculateNormalVector(hand.getThumb(), hand.getIndex(), hand.getMiddle()).normalize();
        if (normal.getY() > 0) {
            System.out.println("Palm UP!!!");
            return true;
        } else {
            return false;
        }
        
    }
    
}
