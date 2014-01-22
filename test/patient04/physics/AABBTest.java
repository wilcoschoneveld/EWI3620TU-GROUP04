/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.physics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import patient04.math.Vector;

/**
 *
 * @author Bart
 */
public class AABBTest {
    AABB instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
    
    public AABBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of constructor2, of class AABB
     */
    @Test
    public void testConstructor2() {
        System.out.println("constructor2");
        AABB result = new AABB(new Vector(-1, -1, -1), new Vector(1, 1, 1));
        AABB expResult = new AABB(new Vector(), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }

    /**
     * Test of copy method, of class AABB.
     */
    @Test
    public void testCopy_0args() {
        System.out.println("copy");
        AABB expResult = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        AABB result = instance.copy();
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }

    /**
     * Test of copy method, of class AABB.
     */
    @Test
    public void testCopy_Vector() {
        System.out.println("copy");
        Vector pos = new Vector(2, 2, 2);
        AABB expResult = new AABB(new Vector(2, 2, 2), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        AABB result = instance.copy(pos);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }
    
    /**
     * Test of copy method, of class AABB.
     */
    @Test
    public void testCopy_Vector2() {
        System.out.println("copy");
        Vector pos = new Vector();
        AABB expResult = new AABB(new Vector(), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        AABB result = instance.copy(pos);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }

    /**
     * Test of translate method, of class AABB.
     */
    @Test
    public void testTranslate() {
        System.out.println("translate");
        Vector delta = new Vector(2, 2, 2);
        AABB expResult = new AABB(new Vector(3, 3, 3), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        AABB result = instance.translate(delta);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }
    
    /**
     * Test of translate method, of class AABB.
     */
    @Test
    public void testTranslate2() {
        System.out.println("translate2");
        Vector delta = new Vector(-2, -2, -2);
        AABB expResult = new AABB(new Vector(-1, -1, -1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        AABB result = instance.translate(delta);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }

    /**
     * Test of rotate method, of class AABB.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
        float angle = 25F;
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;
        AABB expResult = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));;
        AABB result = instance.rotate(angle, x, y, z);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }
    
    /**
     * Test of expand method, of class AABB.
     */
    @Test
    public void testExpand() {
        System.out.println("expand");
        Vector delta = new Vector(2, 2, 2);
        AABB expResult = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(3, 3, 3));
        AABB result = instance.expand(delta);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }
    
     /**
     * Test of expand method, of class AABB.
     */
    @Test
    public void testExpand2() {
        System.out.println("expand2");
        Vector delta = new Vector(-2, -2, -2);
        AABB expResult = new AABB(new Vector(1, 1, 1), new Vector(-3, -3, -3), new Vector(1, 1, 1));
        AABB result = instance.expand(delta);
        assertEquals(expResult.pos, result.pos);
        assertEquals(expResult.min, result.min);
        assertEquals(expResult.max, result.max);
    }
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects() {
        System.out.println("intersects");
        AABB o = null;
        instance = new AABB(new Vector(4, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }    

    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects2() {
        System.out.println("intersects2");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(4, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects3() {
        System.out.println("intersects3");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(-2, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }    
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects4() {
        System.out.println("intersects4");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(1, 0, 4), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }  
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects5() {
        System.out.println("intersects5");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(1, 0, -2), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }  
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects6() {
        System.out.println("intersects6");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(1, 3, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }  
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects7() {
        System.out.println("intersects7");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(1, -2, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = false;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    }  
    
    /**
     * Test of intersects method, of class AABB.
     */
    @Test
    public void testIntersects8() {
        System.out.println("intersects8");
        AABB o = new AABB(new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        instance = new AABB(new Vector(2, 0, 2), new Vector(-1, 0, -1), new Vector(1, 1, 1));
        boolean expResult = true;
        boolean result = instance.intersects(o);
        assertEquals(expResult, result);
    } 
   
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisx() {
        AABB o = new AABB(new Vector(5, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(2, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisx2() {
        AABB o = new AABB(new Vector(-3, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(-10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(-2, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisx3() {
        AABB o = new AABB(new Vector(5, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(2, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(2, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisx4() {
        AABB o = new AABB(new Vector(-3, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(-2, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(-2, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisx5() {
        AABB o = new AABB(new Vector(-3, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisx6() {
        AABB o = new AABB(new Vector(5, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(-10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(-10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
      
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisy() {
        AABB o = new AABB(new Vector(1, 5, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 10, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 2, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisy2() {
        AABB o = new AABB(new Vector(1, -3, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, -10, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, -2, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisy3() {
        AABB o = new AABB(new Vector(1, 5, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 2, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 2, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisy4() {
        AABB o = new AABB(new Vector(1, -3, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, -2, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, -2, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisy5() {
        AABB o = new AABB(new Vector(1, -3, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 10, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 10, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisy6() {
        AABB o = new AABB(new Vector(1, 5, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, -10, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, -10, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisz() {
        AABB o = new AABB(new Vector(1, 1, 5), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 0, 10);
        int axis = 2;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 0, 2);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisz2() {
        AABB o = new AABB(new Vector(1, 1, -3), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 0, -10);
        int axis = 2;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 0, -2);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisz3() {
        AABB o = new AABB(new Vector(1, 1, 5), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 0, 2);
        int axis = 2;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 0, 2);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisz4() {
        AABB o = new AABB(new Vector(1, 1, -3), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 0, -2);
        int axis = 2;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 0, -2);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisz5() {
        AABB o = new AABB(new Vector(1, 1, -3), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 0, 10);
        int axis = 2;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 0, 10);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxisz6() {
        AABB o = new AABB(new Vector(1, 1, 5), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(0, 0, -10);
        int axis = 2;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(0, 0, -10);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxis1() {
        AABB o = new AABB(new Vector(1, 5, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxis2() {
        AABB o = new AABB(new Vector(1, -3, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxis3() {
        AABB o = new AABB(new Vector(1, 1, 5), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxis4() {
        AABB o = new AABB(new Vector(1, 1, -3), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 0;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxis5() {
        AABB o = new AABB(new Vector(5, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
    
    /**
     * Test of sweepAlongAxis method, of class AABB.
     */
    @Test
    public void testSweepAlongAxis6() {
        AABB o = new AABB(new Vector(-3, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        Vector delta = new Vector(10, 0, 0);
        int axis = 1;
        instance = new AABB(new Vector(1, 1, 1), new Vector(-1, -1, -1), new Vector(1, 1, 1));
        instance.sweepAlongAxis(o, delta, axis);
        Vector expResult = new Vector(10, 0, 0);
        float error = expResult.min(delta).length();
        assertTrue(error < 0.001);
    }
}
