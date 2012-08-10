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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.NameNotFoundException;

public class LDAPUpdateAttributeTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPUpdateAttributeTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "update-attribute-mule-config.xml";
    }
    
    // Single
    @Test
    public void testUpdateExistingSingleAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "cn");
        params.put("attributeValue", "User One Updated");
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateSingleAttributeFlow", params);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals(params.get("attributeValue"), result.getAttribute("cn").getValue());
        assertEquals(1, result.getAttribute("cn").getValues().size());
    }

    @Test
    public void testUpdateNonExistingSingleAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "telephoneNumber");
        params.put("attributeValue", "777888999100");
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateSingleAttributeFlow", params);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals(params.get("attributeValue"), result.getAttribute("telephoneNumber").getValue());
    }
    
    @Test
    public void testUpdateSingleAttributeValueToExistingMultiValueAttributeToExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user3,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValue", "user3@new.mail.com");
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateSingleAttributeFlow", params);
        
        assertEquals("user3", result.getAttribute("uid").getValue());
        assertEquals(1, result.getAttribute("mail").getValues().size());
        assertFalse(result.getAttribute("mail").getValues().contains("user3@mulesoft.org"));
        assertTrue(result.getAttribute("mail").getValues().contains(params.get("attributeValue")));
    }

    @Test
    public void testUpdateSingleAttributeToNonExistingDnEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=userX,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "description");
        params.put("attributeValue", "Description for non existant userX");
        
        runFlowWithPayloadAndExpectException("testUpdateSingleAttributeFlow", NameNotFoundException.class, params);
    }
    

    // Multi
    @Test
    public void testUpdateNewMultiAttributeToExistingEntry() throws Exception
    {
        List<String> mails = new ArrayList<String>();
        mails.add("user3@mail.com");
        mails.add("user3@mail.org");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dn", "uid=user3,ou=people,dc=mulesoft,dc=org");
        params.put("attributeName", "mail");
        params.put("attributeValues", mails);
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateMultiAttributeFlow", params);
        
        assertEquals("user3", result.getAttribute("uid").getValue());
        assertEquals(mails.size(), result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().containsAll(mails));
        assertFalse(result.getAttribute("mail").getValues().contains("user3@new.mail.com"));
    }    
}


