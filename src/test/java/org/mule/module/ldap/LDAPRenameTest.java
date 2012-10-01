/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.NameNotFoundException;

public class LDAPRenameTest extends AbstractLDAPConnectorTest
{
    /**
     * 
     */
    public LDAPRenameTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "rename-mule-config.xml";
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteExistingEntry() throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oldDn", "uid=user1,ou=people,dc=mulesoft,dc=org");
        params.put("newDn", "uid=userRename,ou=people,dc=mulesoft,dc=org");
        
        Map<String, Object> result = (Map<String, Object>) runFlow("testRenameEntryFlow", params);
        assertEquals(params, result);
        
        runFlowWithPayloadAndExpectException("testLookupEntryFlow", NameNotFoundException.class, params.get("oldDn"));
        
        LDAPEntry renamedEntry = (LDAPEntry) runFlow("testLookupEntryFlow", params.get("newDn"));
        
        assertEquals("userRename", renamedEntry.getAttribute("uid").getValue());
        assertEquals("User One", renamedEntry.getAttribute("cn").getValue());
      
    }
}


