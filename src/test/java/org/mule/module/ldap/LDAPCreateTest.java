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

import org.junit.Ignore;
import org.junit.Test;
import org.mule.module.ldap.ldap.api.InvalidAttributeException;
import org.mule.module.ldap.ldap.api.InvalidEntryException;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.NameAlreadyBoundException;

public class LDAPCreateTest extends AbstractLDAPConnectorTest
{

    /**
     * 
     */
    public LDAPCreateTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "create-mule-config.xml";
    }
    
    @Test
    public void testCreateNewValidEntry() throws Exception
    {
        LDAPEntry entryToCreate = new LDAPEntry("uid=testuser,ou=people,dc=mulesoft,dc=org");
        entryToCreate.addAttribute("uid", "testuser");
        entryToCreate.addAttribute("cn", "Test User");
        entryToCreate.addAttribute("sn", "User");
        entryToCreate.addAttribute("userPassword", "test1234");
        entryToCreate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        LDAPEntry result = (LDAPEntry) runFlow("testCreateEntryFlow", entryToCreate);
        
        assertEquals(entryToCreate.getAttribute("uid").getValue(), result.getAttribute("uid").getValue());
        assertEquals(entryToCreate.getAttribute("cn").getValue(), result.getAttribute("cn").getValue());
        assertEquals(entryToCreate.getAttribute("sn").getValue(), result.getAttribute("sn").getValue());
    }
    
    @Test
    public void testCreateExistingDnEntry() throws Exception
    {
        LDAPEntry entryToCreate = new LDAPEntry("uid=user1,ou=people,dc=mulesoft,dc=org");
        entryToCreate.addAttribute("uid", "user1");
        entryToCreate.addAttribute("cn", "User One");
        entryToCreate.addAttribute("sn", "One");
        entryToCreate.addAttribute("userPassword", "user1");
        entryToCreate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testCreateEntryFlow", NameAlreadyBoundException.class, entryToCreate);
    }    
    
    @Test
    @Ignore // This is not working in the embedded Directory Server! No schema validation?
    public void testCreateMissingRequiredAttributeEntry() throws Exception
    {
        LDAPEntry entryToCreate = new LDAPEntry("uid=invalidtestuser1,ou=people,dc=mulesoft,dc=org");
        entryToCreate.addAttribute("uid", "invalidtestuser1");
        entryToCreate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testCreateEntryFlow", InvalidEntryException.class, entryToCreate);
    }    

    @Test
    @Ignore // This is not working in the embedded Directory Server! It is just a WARN message that the attribute will be skipped
    public void testCreateNotSupportedAttributeEntry() throws Exception
    {
        LDAPEntry entryToCreate = new LDAPEntry("uid=invalidtestuser2,ou=people,dc=mulesoft,dc=org");
        entryToCreate.addAttribute("uid", "invalidtestuser2");
        entryToCreate.addAttribute("cn", "Invalid Test User 2");
        entryToCreate.addAttribute("sn", "User");
        entryToCreate.addAttribute("userPassword", "test1234");
        entryToCreate.addAttribute("notSupportedAttribute", "notSupportedValue");
        entryToCreate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testCreateEntryFlow", InvalidAttributeException.class, entryToCreate);
    }    

    @Test
    public void testCreateInvalidAttributeValueEntry() throws Exception
    {
        LDAPEntry entryToCreate = new LDAPEntry("uid=invalidtestuser3,ou=people,dc=mulesoft,dc=org");
        entryToCreate.addAttribute("uid", "invalidtestuser3");
        entryToCreate.addAttribute("cn", new LDAPEntry()); // Invalid value!
        entryToCreate.addAttribute("sn", "User");
        entryToCreate.addAttribute("userPassword", "test1234");
        entryToCreate.addAttribute("objectclass", new String[] {"top", "person", "organizationalPerson", "inetOrgPerson"});
        
        runFlowWithPayloadAndExpectException("testCreateEntryFlow", InvalidAttributeException.class, entryToCreate);
    }    
    
}


