/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.math;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kajdreef
 */
public class VectorTest {
    
    public VectorTest() {
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
     * Test of copy method, of class Vector.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
        Vector instance = new Vector(1,1,1);
        Vector expResult = new Vector(1,1,1);
        Vector result = instance.copy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of set method, of class Vector.
     */
    @Test
    public void testSet_3args() {
        System.out.println("set");
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;
        Vector instance = new Vector();
        Vector expResult = new Vector(0.0f, 0.0f, 0.0f);
        Vector result = instance.set(x, y, z);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of set method, of class Vector.
     */
    @Test
    public void testSet_Vector() {
        System.out.println("set");
        Vector other = new Vector(1,2,3);
        Vector instance = new Vector(1,1,1);
        Vector expResult = new Vector(1,2,3);
        Vector result = instance.set(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class Vector.
     */
    @Test
    public void testAdd_3args() {
        System.out.println("add");
        float dx = 1.0F;
        float dy = 2.0F;
        float dz = 3.0F;
        Vector instance = new Vector(1,1,1);
        Vector expResult = new Vector(2,3,4);
        Vector result = instance.add(dx, dy, dz);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class Vector.
     */
    @Test
    public void testAdd_Vector() {
        System.out.println("add");
        Vector other = new Vector(1,2,3);
        Vector instance = new Vector(1,1,1);
        Vector expResult = new Vector(2,3,4);
        Vector result = instance.add(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of min method, of class Vector.
     */
    @Test
    public void testMin_3args() {
        System.out.println("min");
        float dx = 3.0F;
        float dy = 2.0F;
        float dz = 1.0F;
        Vector instance = new Vector(1,2,3);
        Vector expResult = new Vector(-2,0,2);
        Vector result = instance.min(dx, dy, dz);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of min method, of class Vector.
     */
    @Test
    public void testMin_Vector() {
        System.out.println("min");
        Vector other = new Vector(1,2,3);
        Vector instance = new Vector(1,1,1);
        Vector expResult = new Vector(0,-1,-2);
        Vector result = instance.min(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of scale method, of class Vector.
     */
    @Test
    public void testScale_3args() {
        System.out.println("scale");
        float sx = 1.0F;
        float sy = 3.0F;
        float sz = -1.0F;
        Vector instance = new Vector(1,2,3);
        Vector expResult = new Vector(1,6,-3);
        Vector result = instance.scale(sx, sy, sz);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of scale method, of class Vector.
     */
    @Test
    public void testScale_float() {
        System.out.println("scale");
        float scale = 2.0F;
        Vector instance = new Vector(3,3,3);
        Vector expResult = new Vector(6,6,6);
        Vector result = instance.scale(scale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of scale method, of class Vector.
     */
    @Test
    public void testScale_Vector() {
        System.out.println("scale");
        Vector other = new Vector(1,2,3);
        Vector instance = new Vector(2,2,2);
        Vector expResult = new Vector(2,4,6);
        Vector result = instance.scale(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of length method, of class Vector.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        Vector instance = new Vector(5,0,0);
        float expResult = 5.0F;
        float result = instance.length();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of normalize method, of class Vector.
     */
    @Test
    public void testNormalize() {
        System.out.println("normalize");
        Vector instance = new Vector(0,0,5);
        Vector expResult = new Vector(0,0,1);
        Vector result = instance.normalize();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of dot method, of class Vector.
     */
    @Test
    public void testDot() {
        System.out.println("dot");
        Vector other = new Vector(1,1,-1);
        Vector instance = new Vector(-1,-3,-1);
        float expResult = -3.0F;
        float result = instance.dot(other);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of cross method, of class Vector.
     */
    @Test
    public void testCross() {
        System.out.println("cross");
        Vector other = new Vector(1,2,3);
        Vector instance = new Vector(3,2,1);
        Vector expResult = new Vector(4,-8,4);
        Vector result = instance.cross(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of premultiply method, of class Vector.
     */
    @Test
    public void testPremultiply() {
        System.out.println("premultiply");
        Matrix mat = new Matrix();
        Vector instance = new Vector(3,2,1);
        Vector expResult = new Vector(3,2,1);
        Vector result = instance.premultiply(mat);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of rotate method, of class Vector.
     */
    @Test
    public void testRotate_4args() {
        System.out.println("rotate");
        float angle = 90.0F;
        float x = 0.0F;
        float y = 1.0F;
        float z = 0.0F;
        Vector instance = new Vector(5,2,3);
        Vector expResult = new Vector(3,2,-5);
        Vector result = instance.rotate(angle, x, y, z);
        
        float d = result.min(expResult).length();
        
        assertTrue(d < 0.0001f);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of rotate method, of class Vector.
     */
    @Test
    public void testRotate_float_Vector() {
        System.out.println("rotate");
        float angle = 180F;
        Vector other = new Vector(1, 0, 0);
        
        Vector instance = new Vector(5, 6, 3);
        Vector expResult = new Vector(5, -6, -3);
        
        Vector result = instance.rotate(angle, other);
        
        float d = result.min(expResult).length();
        
        assertTrue(d < 0.0001f);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isNull method, of class Vector.
     */
    @Test
    public void testIsNull() {
        System.out.println("isNull");
        
        Vector instance = new Vector();
        assertTrue(instance.isNull());
        
        instance = new Vector(0,0,1);
        assertFalse(instance.isNull());
        
        instance = new Vector(0,1,0);
        assertFalse(instance.isNull());
        
        instance = new Vector(1,0,0);
        assertFalse(instance.isNull());
    }

    /**
     * Test of isInTriangle method, of class Vector.
     */
    @Test
    public void testIsInTriangle() {
        System.out.println("isInTriangle");
        Vector p1 = new Vector(1,1,0);
        Vector p2 = new Vector(1,0,1);
        Vector p3 = new Vector(0,1,1);
        
        Vector instance = new Vector(0.5f,0.5f, 0 );
        boolean expResult = true;
        boolean result = instance.isInTriangle(p1, p2, p3);
        assertEquals(expResult, result);
        
        instance = new Vector(1.0f, 0.0f, 0.0f);
        expResult = false;
        result = instance.isInTriangle(p1, p2, p3);
        assertEquals(expResult, result);
        
        instance = new Vector(0.0f, 1.0f, 0.0f);
        expResult = false;
        result = instance.isInTriangle(p1, p2, p3);
        assertEquals(expResult, result);
        
        instance = new Vector(0.0f, 0.0f, 1.0f);
        expResult = false;
        result = instance.isInTriangle(p1, p2, p3);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Vector.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        Object obj = new Object();
        Vector instance = new Vector();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        
        expResult = false;
        Vector instance2 = new Vector(1,0,0);
        result = instance.equals(instance2);
        assertEquals(expResult, result);
        
        expResult = false;
         instance2 = new Vector(0,1,0);
        result = instance.equals(instance2);
        assertEquals(expResult, result);
        
        expResult = false;
        instance2 = new Vector(0,0,1);
        result = instance.equals(instance2);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Vector.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Vector instance = new Vector(1, 2, 3);
        Vector instance2 = new Vector(1, 2, 3);
        
        assertEquals(instance.hashCode(), instance2.hashCode());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Vector.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Vector instance = new Vector(1.2345f, 2.3456f, 3.4567f);
        String expResult = "(1,235 2,346 3,457)";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
