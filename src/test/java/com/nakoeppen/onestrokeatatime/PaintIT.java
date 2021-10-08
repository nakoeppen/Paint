//Nicholas Koeppen
//Used for Paint Class Unit Testing in One Stroke at a Time
package com.nakoeppen.onestrokeatatime;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author nicho
 */
public class PaintIT {
    
    public PaintIT() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getImage method, of class Paint.
     */
    @Test
    public void testGetImage() {
        System.out.println("getImage");
        Paint instance = new Paint();
        Image expResult = null;
        Image result = instance.getImage();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLineType method, of class Paint.
     */
    @Test
    public void testGetLineType() {
        System.out.println("getLineType");
        Paint instance = new Paint();
        int expResult = 0;
        int result = instance.getLineType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getColor method, of class Paint.
     */
    @Test
    public void testGetColor_0args() {
        System.out.println("getColor");
        Paint instance = new Paint();
        Color expResult = null;
        Color result = instance.getColor();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLineWidth method, of class Paint.
     */
    @Test
    public void testGetLineWidth() {
        System.out.println("getLineWidth");
        Paint instance = new Paint();
        int expResult = 0;
        int result = instance.getLineWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSaveFile method, of class Paint.
     */
    @Test
    public void testGetSaveFile() {
        System.out.println("getSaveFile");
        Paint instance = new Paint();
        File expResult = null;
        File result = instance.getSaveFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUnsavedChanges method, of class Paint.
     */
    @Test
    public void testGetUnsavedChanges() {
        System.out.println("getUnsavedChanges");
        Paint instance = new Paint();
        boolean expResult = false;
        boolean result = instance.getUnsavedChanges();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfPolygonSides method, of class Paint.
     */
    @Test
    public void testGetNumberOfPolygonSides() {
        System.out.println("getNumberOfPolygonSides");
        Paint instance = new Paint();
        int expResult = 0;
        int result = instance.getNumberOfPolygonSides();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFill method, of class Paint.
     */
    @Test
    public void testGetFill() {
        System.out.println("getFill");
        Paint instance = new Paint();
        boolean expResult = false;
        boolean result = instance.getFill();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }    
}
