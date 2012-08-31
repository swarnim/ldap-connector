/**
 * Mule LDAP Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;
import org.mule.module.ldap.ldap.api.InvalidAttributeException;
import org.mule.module.ldap.ldap.api.InvalidEntryException;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.NameNotFoundException;

public class LDAPUpdateTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPUpdateTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "update-mule-config.xml";
    }
    
    @Test
    public void testUpdateExistingEntryExistingAttributeValues() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("uid", "user1");
        entryToUpdate.addAttribute("cn", "User One Updated");
        entryToUpdate.addAttribute("sn", "One");
        entryToUpdate.addAttribute("userPassword", "test1234");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateEntryFlow", entryToUpdate);
        
        assertEquals(entryToUpdate.getAttribute("uid").getValue(), result.getAttribute("uid").getValue());
        assertEquals(entryToUpdate.getAttribute("cn").getValue(), result.getAttribute("cn").getValue());
        assertEquals(entryToUpdate.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
        assertNull(result.getAttribute("mail"));
    }

    @Test
    public void testUpdateExistingEntryAddNewAttribute() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("uid", "user1");
        entryToUpdate.addAttribute("cn", "User One");
        entryToUpdate.addAttribute("sn", "One Updated");
        entryToUpdate.addAttribute("mail", new String[] {"user1@mail.com", "user1@mail.org"});
        entryToUpdate.addAttribute("userPassword", "test1234");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateEntryFlow", entryToUpdate);
        
        assertEquals(entryToUpdate.getAttribute("uid").getValue(), result.getAttribute("uid").getValue());
        assertEquals(entryToUpdate.getAttribute("cn").getValue(), result.getAttribute("cn").getValue());
        assertEquals(entryToUpdate.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
        assertEquals(entryToUpdate.getAttribute("mail").getValues().size(), result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().containsAll(entryToUpdate.getAttribute("mail").getValues()));
        assertFalse(result.getAttribute("mail").getValues().contains("userX@mail.com"));
    }
    
    // This test depends on the previous one updating the entry
    @Test
    public void testUpdateExistingEntryNotAllAttributes() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("sn", "One Updated Twice");
        entryToUpdate.addAttribute("userPassword", "test1234");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateEntryFlow", entryToUpdate);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals("User One", result.getAttribute("cn").getValue());
        assertEquals(entryToUpdate.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
        assertEquals(2, result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().contains("user1@mail.com"));
        assertTrue(result.getAttribute("mail").getValues().contains("user1@mail.org"));
        assertFalse(result.getAttribute("mail").getValues().contains("userX@mail.com"));
    }
    
    // This test depends on the previous one updating the entry to add mail attribute
    /**
     * Replacing a two email attribute with one value
     */
    @Test
    public void testUpdateExistingValuesInMultiValueAttribute() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("mail", new String[] {"user1@mail.com"});
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testUpdateEntryFlow", entryToUpdate);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals("User One", result.getAttribute("cn").getValue());
        assertEquals(1, result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().contains("user1@mail.com"));
        assertFalse(result.getAttribute("mail").getValues().contains("user1@mail.org"));
    }
    
    @Test
    public void testUpdateNonExistingDnEntry() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=userX,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("uid", "userX");
        entryToUpdate.addAttribute("cn", "User X");
        entryToUpdate.addAttribute("sn", "X");
        entryToUpdate.addAttribute("userPassword", "userX");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testUpdateEntryFlow", NameNotFoundException.class, entryToUpdate);
    }    
    
    @Test
    @Ignore // This is not working in the embedded Directory Server! No schema validation?
    public void testUpdateMissingRequiredAttributeEntry() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("uid", "user1");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testUpdateEntryFlow", InvalidEntryException.class, entryToUpdate);
    }    

    @Test
    @Ignore // This is not working in the embedded Directory Server! It is just a WARN message that the attribute will be skipped
    public void testUpdateNotSupportedAttributeEntry() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("uid", "user1");
        entryToUpdate.addAttribute("cn", "User One Updated");
        entryToUpdate.addAttribute("sn", "One Updated");
        entryToUpdate.addAttribute("userPassword", "test1234");
        entryToUpdate.addAttribute("notSupportedAttribute", "notSupportedValue");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testCreateEntryFlow", InvalidAttributeException.class, entryToUpdate);
    }    

    @Test
    public void testUpdateInvalidAttributeValueEntry() throws Exception
    {
        LDAPEntry entryToUpdate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToUpdate.addAttribute("uid", "user1");
        entryToUpdate.addAttribute("cn", new LDAPEntry()); // Invalid value!
        entryToUpdate.addAttribute("sn", "One");
        entryToUpdate.addAttribute("userPassword", "test1234");
        entryToUpdate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testUpdateEntryFlow", InvalidAttributeException.class, entryToUpdate);
    }    
    
}


