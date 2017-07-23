/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicos;

import entidades.BLeitor;
import entidades.Users;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author Migueljr
 */
public class AutenticacaoImplIT extends TestCase {
    
    public AutenticacaoImplIT(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test of getUserCredential method, of class AutenticacaoImpl.
     */
   

    /**
     * Test of login method, of class AutenticacaoImpl.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        String nm = "mjunior";
        String pd = "mjunior";
        AutenticacaoImpl instance = new AutenticacaoImpl();
        int expResult = 0;
        int result = instance.login(nm, pd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserCredential method, of class AutenticacaoImpl.
     */
    public void testGetUserCredential() {
        System.out.println("getUserCredential");
        AutenticacaoImpl instance = new AutenticacaoImpl();
        UserCredential expResult = null;
        UserCredential result = instance.getUserCredential();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of logout method, of class AutenticacaoImpl.
     */
    public void testLogout() {
        System.out.println("logout");
        AutenticacaoImpl instance = new AutenticacaoImpl();
        instance.logout();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pegarLeitor method, of class AutenticacaoImpl.
     */
    public void testPegarLeitor() {
        System.out.println("pegarLeitor");
        Users user = null;
        AutenticacaoImpl instance = new AutenticacaoImpl();
        BLeitor expResult = null;
        BLeitor result = instance.pegarLeitor(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

  