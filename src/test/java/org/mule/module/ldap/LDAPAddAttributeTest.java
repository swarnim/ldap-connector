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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mule.module.ldap.api.InvalidAttributeException;
import org.mule.module.ldap.api.LDAPEntry;
import org.mule.module.ldap.api.NameNotFoundException;

public class LDAPAddAttributeTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPAddAttributeTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "add-attribute-mule-config.xml";
    }
    
    // Single
    @Test
    public void testAddNewSingleAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValue", "Description for user1");
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddSingleAttributeFlow", params);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals(params.get("attributeValue"), result.getAttribute("description").getValue());
    }

    @Test
    public void testAddDuplicateSingleAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValue", "Description for user1");
        
        
        runFlowWithPayloadAndExpectException("testAddSingleAttributeFlow", InvalidAttributeException.class, params);
    }

    
    @Test
    public void testAddEmptySingleAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "telephoneNumber");
        params.put("attributeValue", "");
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddSingleAttributeFlow", params);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals(params.get("attributeValue"), result.getAttribute("telephoneNumber").getValue());
    }
    
    @Test
    public void testAddSingleAttributeValueToExistingMultiValueAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user3,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValue", "user3@new.mail.com");
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddSingleAttributeFlow", params);
        
        assertEquals("user3", result.getAttribute("uid").getValue());
        assertEquals(2, result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().contains("user3@mulesoft.org"));
        assertTrue(result.getAttribute("mail").getValues().contains(params.get("attributeValue")));
    }

    @Test
    public void testAddNewSingleAttributeToNonExistingDnEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=userX,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValue", "Description for non existant userX");
        
        runFlowWithPayloadAndExpectException("testAddSingleAttributeFlow", NameNotFoundException.class, params);
    }
    
    // Multi
    @Test
    public void testAddNewMultiAttributeToExistingEntry() throws Exception
    {
        List<String> mails = new ArrayList<String>();
        mails.add("user1@mail.com");
        mails.add("user1@mail.org");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValues", mails);
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddMultiAttributeFlow", params);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals(mails.size(), result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().containsAll(mails));
        assertFalse(result.getAttribute("mail").getValues().contains("userX@mail.org"));
    }    
    
    @Test
    public void testAddNewSingleAttributeUsingMultiValueOperationToExistingEntry() throws Exception
    {
        List<String> description = new ArrayList<String>();
        description.add("Single value description");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user4,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValues", description);
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddMultiAttributeFlow", params);
        
        assertEquals("user4", result.getAttribute("uid").getValue());
        assertEquals(description.get(0), result.getAttribute("description").getValue());
        assertEquals(description.size(), result.getAttribute("description").getValues().size());
    }  

    @Test
    public void testAddEmptyMultiAttributeValuesToExistingMultiValueAttributeToExistingEntry() throws Exception
    {
        List<String> description = new ArrayList<String>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user4,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValues", description);

        runFlowWithPayloadAndExpectException("testAddMultiAttributeFlow", InvalidAttributeException.class, params);
    }
    
    @Test
    public void testAddMultiAttributeValuesToExistingMultiValueAttributeToExistingEntry() throws Exception
    {
        List<String> mails = new ArrayList<String>();
        mails.add("user4@mail.com");
        mails.add("user4@mail.org");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user4,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValues", mails);
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddMultiAttributeFlow", params);
        
        assertEquals("user4", result.getAttribute("uid").getValue());
        assertEquals(mails.size() + 2, result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().contains("user4@mulesoft.org"));
        assertTrue(result.getAttribute("mail").getValues().contains("user4@mulesoft.com"));
        assertTrue(result.getAttribute("mail").getValues().containsAll(mails));
    }    
    
    @Test
    public void testAddNewMultiAttributeToNonExistingDnEntry() throws Exception
    {
        List<String> mails = new ArrayList<String>();
        mails.add("userX@mail.com");
        mails.add("userX@mail.org");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=userX,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValues", mails);
        
        runFlowWithPayloadAndExpectException("testAddMultiAttributeFlow", NameNotFoundException.class, params);
    }    
}


