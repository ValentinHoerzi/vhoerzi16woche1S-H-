/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.htlgrieskirchen.pos3.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import net.htlgrieskirchen.pos3.hierarchy.fx.Main;
import net.htlgrieskirchen.pos3.hierarchy.fx.Controller;
import static org.junit.Assert.fail;

/**
 *
 * @author Torsten Welsch
 */
public class JavaFXUtil {
    
    private static final int WAIT_TIME = 100;
    private static Controller controller;

    public static <T> List<T> toList(ObservableList<T> observableList) {
        return observableList.stream().collect(Collectors.toList());
    }
    
    public static <T> T getField(String fieldName, Class<T> fieldType) {
        T result = null;
        
        try {
            result = (T) accessField(fieldName).get(getController());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            fail("unbale to get field " + fieldName);
        }
        
        return result;
    }
    
    public static <T> void setField(String fieldName, T fieldValue) {
        try {
            accessField(fieldName).set(getController(), fieldValue);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            fail("unbale to set field " + fieldName + " to " + fieldValue.toString());
        }
    }
    
    public static Field accessField(String fieldName) {
        Field result = null;
        
        try {
            result = getController().getClass().getDeclaredField(fieldName);
            result.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            fail("unbale to get access to field " + fieldName);
        }
        
        return result;
    }
    
    public static void processEvent(String methodName) {
        FutureTask callEnventHandlerMethod = new FutureTask(() -> {
            Method method = getActionEventHandlerMethod(methodName);
            method.invoke(getController(), (Object) null);
            
            return Boolean.TRUE;
        });
        Platform.runLater(callEnventHandlerMethod);
        try {
            System.out.println("> event handler method called: " + callEnventHandlerMethod.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail("unbale to call event handler method " + methodName);
        }
    }
    
    public static Method getActionEventHandlerMethod(String methodName) {
        return getMethod(methodName, ActionEvent.class);
    }
    
    public static Method getMethod(String methodName, Class<?>... parameter ) {
        Method result = null;
        
        try {
            result = getController().getClass().getDeclaredMethod(methodName, parameter);
            result.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            fail("unbale to get access to method " + methodName);
        }
        
        return result;
    }
    
    private static Controller getController() {
        return controller != null ? controller : tryToGetController();
    } 
    
    public static Controller tryToGetController() {
        Controller result = null;
        
        int time = 0;
        new Thread(() -> Main.main(null)).start();
        while (result == null) {
            
            try {
                Thread.sleep(WAIT_TIME);
                time += WAIT_TIME;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            result = Main.getController();
            
            if (time > 100 * WAIT_TIME) {
                throw new RuntimeException("unable to get controller");
            }
        }
        
        controller = result;
        return result;
    }
    
}
