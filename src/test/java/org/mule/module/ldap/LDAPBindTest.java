/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.transport.NullPayload;

public class LDAPBindTest extends AbstractLDAPConnectorTest
{

    /**
     * 
     */
    public LDAPBindTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "bind-mule-config.xml";
    }
    
    @Test
    public void testAnonymousBind() throws Exception
    {
        NullPayload result = (NullPayload) runFlow("testAnonymousBind", null);
        
        assertEquals(NullPayload.class, result.getClass());
    }
    
    @Test
    public void testConfigBind() throws Exception
    {
        LDAPEntry result = (LDAPEntry) runFlow("testConfigBind", null);
        
        assertEquals("admin", result.getAttribute("uid").getValue());
        assertEquals("Administrator", result.getAttribute("cn").getValue());
        assertEquals("Administrator", result.getAttribute("sn").getValue());
    }
    
    @Test
    public void testValidBind() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("authDn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("authPassword", "user1");
        
        LDAPEntry result = (LDAPEntry) runFlow("testAuthenticationBind", params);
        
        assertNotNull(result);        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals("User One", result.getAttribute("cn").getValue());
    }    
    
    @Test
    public void testInvalidUserBind() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("authDn", "uid=userX,ou=people,dc=mulesoft,dc=org");
        params.put("authPassword", "passwordX");
        
        Throwable ex = runFlowWithPayloadAndReturnException("testAuthenticationBind", params);
        
        // Need to do this because of issue http://www.mulesoft.org/jira/browse/DEVKIT-177
        // Once fixed the following two lines can replace all the code after this comment
        // Throwable ex = runFlowWithPayloadAndExpectException("testAuthenticationLogin", ConnectionException.class, params);
        // assertEquals(ConnectionExceptionCode.INCORRECT_CREDENTIALS,((ConnectionException) ex).getCode());

        if(ex instanceof NoSuchElementException)
        {
            assertTrue(ex.getMessage().contains("49"));
        }
        else
        {
            assertEquals(ConnectionException.class, ex.getClass());
            assertEquals(ConnectionExceptionCode.INCORRECT_CREDENTIALS,((ConnectionException) ex).getCode());
        }
        
        
    }    
    
    @Test
    public void testInvalidPasswordBind() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("authDn", "uid=user2,ou=people,dc=mulesoft,dc=org");
        params.put("authPassword", "invalidPassword");
        
        Throwable ex = runFlowWithPayloadAndReturnException("testAuthenticationBind", params);
        
        // Need to do this because of issue http://www.mulesoft.org/jira/browse/DEVKIT-177
        // Once fixed the following two lines can replace all the code after this comment
        // Throwable ex = runFlowWithPayloadAndExpectException("testAuthenticationLogin", ConnectionException.class, params);
        // assertEquals(ConnectionExceptionCode.INCORRECT_CREDENTIALS,((ConnectionException) ex).getCode());

        if(ex instanceof NoSuchElementException)
        {
            assertTrue(ex.getMessage().contains("49"));
        }
        else
        {
            assertEquals(ConnectionException.class, ex.getClass());
            assertEquals(ConnectionExceptionCode.INCORRECT_CREDENTIALS,((ConnectionException) ex).getCode());
        }
    }      
}


