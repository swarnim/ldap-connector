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

import org.junit.Test;
import org.mule.module.ldap.ldap.api.ContextNotEmptyException;
import org.mule.module.ldap.ldap.api.NameNotFoundException;

public class LDAPDeleteTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPDeleteTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "delete-mule-config.xml";
    }
    
    @Test
    public void testDeleteExistingEntry() throws Exception
    {
        final String dnToDelete = "uid=user1,ou=people,dc=mulesoft,dc=org";
        String dn = (String) runFlow("testDeleteEntryFlow", dnToDelete);
        assertEquals(dnToDelete, dn);
        
        runFlowWithPayloadAndExpectException("testLookupEntryFlow", NameNotFoundException.class, dnToDelete);
    }

    @Test
    public void testDeleteNonExistingDnEntry() throws Exception
    {
        runFlow("testDeleteEntryFlow", "uid=inexistantUserToDelete,ou=people,dc=mulesoft,dc=org");
    }
    
    @Test
    public void testDeleteNonExistingRelativeDnEntry() throws Exception
    {
        runFlowWithPayloadAndExpectException("testDeleteEntryFlow", NameNotFoundException.class, "uid=inexistantUserToDelete,ou=NonInexistantOU,dc=mulesoft,dc=org");
    }
    
    @Test
    public void testDeleteEntryWithChildren() throws Exception
    {
        runFlowWithPayloadAndExpectException("testDeleteEntryFlow", ContextNotEmptyException.class, "ou=people,dc=mulesoft,dc=org");
    }

}


