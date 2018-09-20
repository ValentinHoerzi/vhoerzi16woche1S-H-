/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos2.util;

import java.io.File;

/**
 *
 * @author Torsten Welsch
 */
public class GradingUtil {
    
    private static int maxPoints;
    private static int achievedPoints;
    
    public static void increaseMaxPoints() {
        maxPoints++;
    }
    
    public static void increaseMaxPoints(int points) {
        maxPoints += points;
    }
    
    public static void increaseAchievedPoints() {
        achievedPoints++;
    }
    
    public static void increaseAchievedPoints(int points) {
        achievedPoints += points;
    }
    
    public static void computeGrading() {
        File workingDirectory = new File(System.getProperty("user.dir"));
        String[] pathParts = workingDirectory.toString().split("\\\\");
        String studentName = pathParts[pathParts.length - 1];
        System.err.print(studentName + " grading: " + achievedPoints + "|" + maxPoints + " = ");

        double ratio = (double) achievedPoints / maxPoints;
        if (ratio < 0.33333) {
            System.err.println("-");
        } else if (ratio < 0.66666) {
            System.err.println("o");
        } else {
            System.err.println("+");
        }
        
        System.err.println();
    }
}
