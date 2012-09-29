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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;
import org.mule.module.ldap.ldap.api.InvalidAttributeException;
import org.mule.module.ldap.ldap.api.InvalidEntryException;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.NameNotFoundException;

public class LDAPModifyTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPModifyTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "modify-mule-config.xml";
    }
    
    @Test
    public void testModifyExistingEntryExistingAttributeValues() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("uid", "user1");
        entryToModify.addAttribute("cn", "User One Updated");
        entryToModify.addAttribute("sn", "One");
        entryToModify.addAttribute("userPassword", "test1234");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testModifyEntryFlow", entryToModify);
        
        assertEquals(entryToModify.getAttribute("uid").getValue(), result.getAttribute("uid").getValue());
        assertEquals(entryToModify.getAttribute("cn").getValue(), result.getAttribute("cn").getValue());
        assertEquals(entryToModify.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
        assertNull(result.getAttribute("mail"));
    }

    @Test
    public void testModifyExistingEntryAddNewAttribute() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("uid", "user1");
        entryToModify.addAttribute("cn", "User One");
        entryToModify.addAttribute("sn", "One Updated");
        entryToModify.addAttribute("mail", new String[] {"user1@mail.com", "user1@mail.org"});
        entryToModify.addAttribute("userPassword", "test1234");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testModifyEntryFlow", entryToModify);
        
        assertEquals(entryToModify.getAttribute("uid").getValue(), result.getAttribute("uid").getValue());
        assertEquals(entryToModify.getAttribute("cn").getValue(), result.getAttribute("cn").getValue());
        assertEquals(entryToModify.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
        assertEquals(entryToModify.getAttribute("mail").getValues().size(), result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().containsAll(entryToModify.getAttribute("mail").getValues()));
        assertFalse(result.getAttribute("mail").getValues().contains("userX@mail.com"));
    }
    
    // This test depends on the previous one updating the entry
    @Test
    public void testModifyExistingEntryNotAllAttributes() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("sn", "One Updated Twice");
        entryToModify.addAttribute("userPassword", "test1234");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testModifyEntryFlow", entryToModify);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals("User One", result.getAttribute("cn").getValue());
        assertEquals(entryToModify.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
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
    public void testModifyExistingValuesInMultiValueAttribute() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("mail", new String[] {"user1@mail.com"});
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testModifyEntryFlow", entryToModify);
        
        assertEquals("user1", result.getAttribute("uid").getValue());
        assertEquals("User One", result.getAttribute("cn").getValue());
        assertEquals(1, result.getAttribute("mail").getValues().size());
        assertTrue(result.getAttribute("mail").getValues().contains("user1@mail.com"));
        assertFalse(result.getAttribute("mail").getValues().contains("user1@mail.org"));
    }
    
    @Test
    public void testModifyNonExistingDnEntry() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=userX,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("uid", "userX");
        entryToModify.addAttribute("cn", "User X");
        entryToModify.addAttribute("sn", "X");
        entryToModify.addAttribute("userPassword", "userX");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testModifyEntryFlow", NameNotFoundException.class, entryToModify);
    }    
    
    @Test
    @Ignore // This is not working in the embedded Directory Server! No schema validation?
    public void testModifyMissingRequiredAttributeEntry() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("uid", "user1");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testModifyEntryFlow", InvalidEntryException.class, entryToModify);
    }    

    @Test
    @Ignore // This is not working in the embedded Directory Server! It is just a WARN message that the attribute will be skipped
    public void testModifyNotSupportedAttributeEntry() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("uid", "user1");
        entryToModify.addAttribute("cn", "User One Updated");
        entryToModify.addAttribute("sn", "One Updated");
        entryToModify.addAttribute("userPassword", "test1234");
        entryToModify.addAttribute("notSupportedAttribute", "notSupportedValue");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testCreateEntryFlow", InvalidAttributeException.class, entryToModify);
    }    

    @Test
    public void testModifyInvalidAttributeValueEntry() throws Exception
    {
        LDAPEntry entryToModify = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToModify.addAttribute("uid", "user1");
        entryToModify.addAttribute("cn", new LDAPEntry()); // Invalid value!
        entryToModify.addAttribute("sn", "One");
        entryToModify.addAttribute("userPassword", "test1234");
        entryToModify.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testModifyEntryFlow", InvalidAttributeException.class, entryToModify);
    }    
    
}


