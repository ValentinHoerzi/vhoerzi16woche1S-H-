/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.fail;

/**
 *
 * @author Torsten Welsch
 */
public class FileUtil {
    
    private static final Map<String, String> GERMAN_CHAR_MAPPING = new HashMap<>();
    static {
        GERMAN_CHAR_MAPPING.put("\\u00e4", "ä");
        GERMAN_CHAR_MAPPING.put("\\u00fc", "ü");
        GERMAN_CHAR_MAPPING.put("\\u00f6", "ö");
        GERMAN_CHAR_MAPPING.put("\\u00c4", "Ä");
        GERMAN_CHAR_MAPPING.put("\\u00dc", "Ü");
        GERMAN_CHAR_MAPPING.put("\\u00d6", "Ö");
        GERMAN_CHAR_MAPPING.put("\\u00df", "ß");
    }
    
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
    
    public static File getFile(String fileName) {
        File file = new File(fileName);
        return file.exists() ? file : null;
    }
    
    public static boolean containsCode(File file, String code) {
        boolean result = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String found;
            while ((found = br.readLine()) != null) {
                found = normalize(found);
                String expected = normalize(code);
                
                if (!expected.contains("final")) {
                    found = found.replace("final", "");
                }
                
                if (found.contains(expected)) {
                    result = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("could not read file " + file.getPath());
        }

        return result;
    }
    
    private static String normalize(String s) {
        String result = s;
        
        for (Map.Entry<String, String> entry : GERMAN_CHAR_MAPPING.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        result = result.trim().replace(" ", "");
        
        return result;
    }
}
