/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.junit.Test;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.LDAPException;

public class LDAPDeleteAttributeTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPDeleteAttributeTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "delete-attribute-mule-config.xml";
    }
    
    // Single
    @Test
    public void testDeleteExistingSingleAttributeFromExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user3,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValue", "user3@mulesoft.org");
        
        LDAPEntry result = (LDAPEntry) runFlow("testDeleteSingleAttributeFlow", params);
        
        assertEquals("user3", result.getAttribute("uid").getValue());
        assertNull(result.getAttribute("mail"));
    }

    @Test
    public void testDeleteNonExistingSingleAttributeValueFromExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user4,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValue", "nonexisting@mail.com");
        
        LDAPEntry result = (LDAPEntry) runFlow("testDeleteSingleAttributeFlow", params);
        
        assertEquals("user4", result.getAttribute("uid").getValue());
        assertEquals(2, result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().contains("user4@mulesoft.org"));
        assertTrue(result.getAttribute("mail").getValues().contains("user4@mulesoft.com"));
    }
    
    @Test
    public void testDeleteNonExistingSingleAttributeFromExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user3,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValue", "Doesn't matter");
        
        Throwable ex = runFlowWithPayloadAndExpectException("testDeleteSingleAttributeFlow", LDAPException.class, params);
        
        assertEquals(ex.getCause().getClass(), NamingException.class);
    }

    @Test
    public void testDeleteExistingSingleAttributeWithoutProvidingValueFromExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user5,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValue", null);
        
        LDAPEntry result = (LDAPEntry) runFlow("testDeleteSingleAttributeWithoutValueFlow", params);
        
        assertEquals("user5", result.getAttribute("uid").getValue());
        assertNull(result.getAttribute("description"));        
    }
    
    // Multi
    @Test
    public void testDeleteExistingMultiAttributeFromExistingEntry() throws Exception
    {
        List<String> mails = new ArrayList<String>();
        mails.add("user5@mail.com");
        mails.add("user5@company.com");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user5,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValues", mails);
        
        LDAPEntry result = (LDAPEntry) runFlow("testDeleteMultiAttributeFlow", params);
        
        assertEquals("user5", result.getAttribute("uid").getValue());
        assertEquals(3, result.getAttribute("mail").getValues().size());
        assertFalse(result.getAttribute("mail").getValues().contains("user5@mail.com"));
        assertFalse(result.getAttribute("mail").getValues().contains("user5@company.com"));
        assertTrue(result.getAttribute("mail").getValues().contains("user5@othercompany.com"));
        assertTrue(result.getAttribute("mail").getValues().contains("user5@mulesoft.com"));
        assertTrue(result.getAttribute("mail").getValues().contains("user5@mulesoft.org"));
    }
    
    @Test
    public void testDeleteNonExistingMultiAttributeFromExistingEntry() throws Exception
    {
        List<String> tels = new ArrayList<String>();
        tels.add("44445555");
        tels.add("33338888");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user5,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "telephoneNumber");
        params.put("attributeValues", tels);
        
        Throwable ex = runFlowWithPayloadAndExpectException("testDeleteMultiAttributeFlow", LDAPException.class, params);
        
        assertEquals(ex.getCause().getClass(), NamingException.class);
    }    

    @Test
    public void testDeleteOneExistingOneNotMultiAttributeValuesFromExistingEntry() throws Exception
    {
        List<String> mails = new ArrayList<String>();
        mails.add("userX@mail.com");
        mails.add("user5@othercompany.com");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user5,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValues", mails);
        
        LDAPEntry result = (LDAPEntry) runFlow("testDeleteMultiAttributeFlow", params);
        
        assertEquals("user5", result.getAttribute("uid").getValue());
        assertEquals(2, result.getAttribute("mail").getValues().size());
        assertFalse(result.getAttribute("mail").getValues().contains("user5@othercompany.com"));
        assertTrue(result.getAttribute("mail").getValues().contains("user5@mulesoft.com"));
        assertTrue(result.getAttribute("mail").getValues().contains("user5@mulesoft.org"));
    }    
    
    @Test
    public void testDeleteExistingMultiAttributeWithoutProvidingValueFromExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user5,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValue", null);
        
        LDAPEntry result = (LDAPEntry) runFlow("testDeleteMultiAttributeWithoutValuesFlow", params);
        
        assertEquals("user5", result.getAttribute("uid").getValue());
        assertNull(result.getAttribute("mail"));        
    }    
}


