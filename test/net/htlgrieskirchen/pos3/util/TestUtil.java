/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos2.util;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Torsten Welsch
 */
public class TestUtil {

    // #######################
    // # interesting methods #
    // #######################

    public static <T> void assertContentContains(String message, List<T> expected, List<T> result) {
        for (T e : expected) {
            boolean found = false;
            for (T r : result) {
                if (Objects.equals(e,r)) {
                    found = true;
                    break;
                }
                else if (Objects.equals(e.toString(), r.toString())) {
                    System.err.println("WARNING: equals uses toString()");
                    found = true;
                    break;
                }
            }
            assertTrue(message, found);
        }
    }

    public static <T> void assertContentEquals(String message, List<T> expected, List<T> result) {
        assertEquals("list size", expected.size(), result.size());
        assertContentContains(message, expected, result);
    }

    public static <T> void assertOrderEquals(String message, List<T> expected, List<T> result) {
        assertEquals("list size", expected.size(), result.size());

        for (int i = 0; i < expected.size(); ++i) {
            if (!Objects.equals(expected.get(i), result.get(i))) {
                System.err.println("WARNING: equals uses toString()");
                assertEquals(message, expected.get(i).toString(), result.get(i).toString());
                
            }
        }
    }

    private static List<String> readFile(File file) {
        List<String> result = new LinkedList<>();
        
        if (file.exists()) {
            try (Scanner s = new Scanner(file)) {
                while (s.hasNextLine()) {
                    result.add(s.nextLine().trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail("could not read file " + file.getPath());
            }
        } else {
            fail("could not find file " + file.getPath());
        }  
        
        return result;
    }

    // ####################
    // # derivate methods #
    // ####################
    
    public static <T> void assertContentContains(String message, T[] expected, List<T> result) {
        assertContentContains(message, Arrays.asList(expected), result);
    }

    public static <T> void assertContentContains(String message, List<T> expected, T[] result) {
        assertContentContains(message, expected, Arrays.asList(result));
    }

    public static <T> void assertContentContains(String message, T[] expected, T[] result) {
        assertContentContains(message, Arrays.asList(expected), Arrays.asList(result));
    }
    
    public static <T> void assertContentEquals(String message, T[] expected, List<T> result) {
        assertContentEquals(message, Arrays.asList(expected), result);
    }

    public static <T> void assertContentEquals(String message, List<T> expected, T[] result) {
        assertContentEquals(message, expected, Arrays.asList(result));
    }

    public static <T> void assertContentEquals(String message, T[] expected, T[] result) {

        assertContentEquals(message, Arrays.asList(expected), Arrays.asList(result));
    }
    
    public static <T> void assertOrderEquals(String message, T[] expected, List<T> result) {
        assertOrderEquals(message, Arrays.asList(expected), result);
    }

    public static <T> void assertOrderEquals(String message, List<T> expected, T[] result) {
        assertOrderEquals(message, expected, Arrays.asList(result));
    }

    public static <T> void assertOrderEquals(String message, T[] expected, T[] result) {
        assertOrderEquals(message, Arrays.asList(expected), Arrays.asList(result));
    }
    
    public static void assertOrderEquals(String[] expected, File result) {
        assertOrderEquals("not all test lines in file", expected, readFile(result));
    }
    
    public static void assertContentContains(String[] expected, File result) {
        assertContentContains("not all test lines in file", expected, readFile(result));
    }
    
    public static void assertContentEquals(String[] expected, File result) {
        assertContentEquals("not all test lines in file", expected, readFile(result));
    }
}
