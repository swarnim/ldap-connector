/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.mule.module.ldap.ldap.api.InvalidAttributeException;
import org.mule.module.ldap.ldap.api.InvalidEntryException;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.NameAlreadyBoundException;

public class LDAPAddTest extends AbstractLDAPConnectorTest
{

    /**
     * 
     */
    public LDAPAddTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "add-mule-config.xml";
    }
    
    @Test
    public void testAddNewValidEntry() throws Exception
    {
        LDAPEntry entryToAdd = new LDAPEntry("uid=testuser,ou=people,dc=mulesoft,dc=org");
        entryToAdd.addAttribute("uid", "testuser");
        entryToAdd.addAttribute("cn", "Test User");
        entryToAdd.addAttribute("sn", "User");
        entryToAdd.addAttribute("userPassword", "test1234");
        entryToAdd.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testAddEntryFlow", entryToAdd);
        
        assertEquals(entryToAdd.getAttribute("uid").getValue(), result.getAttribute("uid").getValue());
        assertEquals(entryToAdd.getAttribute("cn").getValue(), result.getAttribute("cn").getValue());
        assertEquals(entryToAdd.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
    }
    
    @Test
    public void testAddExistingDnEntry() throws Exception
    {
        LDAPEntry entryToAdd = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToAdd.addAttribute("uid", "user1");
        entryToAdd.addAttribute("cn", "User One");
        entryToAdd.addAttribute("sn", "One");
        entryToAdd.addAttribute("userPassword", "user1");
        entryToAdd.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testAddEntryFlow", NameAlreadyBoundException.class, entryToAdd);
    }    
    
    @Test
    @Ignore // This is not working in the embedded Directory Server! No schema validation?
    public void testAddMissingRequiredAttributeEntry() throws Exception
    {
        LDAPEntry entryToAdd = new LDAPEntry("uid=invalidtestuser1,ou=people,dc=mulesoft,dc=org");
        entryToAdd.addAttribute("uid", "invalidtestuser1");
        entryToAdd.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testAddEntryFlow", InvalidEntryException.class, entryToAdd);
    }    

    @Test
    @Ignore // This is not working in the embedded Directory Server! It is just a WARN message that the attribute will be skipped
    public void testAddNotSupportedAttributeEntry() throws Exception
    {
        LDAPEntry entryToAdd = new LDAPEntry("uid=invalidtestuser2,ou=people,dc=mulesoft,dc=org");
        entryToAdd.addAttribute("uid", "invalidtestuser2");
        entryToAdd.addAttribute("cn", "Invalid Test User 2");
        entryToAdd.addAttribute("sn", "User");
        entryToAdd.addAttribute("userPassword", "test1234");
        entryToAdd.addAttribute("notSupportedAttribute", "notSupportedValue");
        entryToAdd.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testAddEntryFlow", InvalidAttributeException.class, entryToAdd);
    }    

    @Test
    public void testAddInvalidAttributeValueEntry() throws Exception
    {
        LDAPEntry entryToAdd = new LDAPEntry("uid=invalidtestuser3,ou=people,dc=mulesoft,dc=org");
        entryToAdd.addAttribute("uid", "invalidtestuser3");
        entryToAdd.addAttribute("cn", new LDAPEntry()); // Invalid value!
        entryToAdd.addAttribute("sn", "User");
        entryToAdd.addAttribute("userPassword", "test1234");
        entryToAdd.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testAddEntryFlow", InvalidAttributeException.class, entryToAdd);
    }    
    
}


