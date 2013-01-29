/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.module.ldap.api.NameNotFoundException;

public class LDAPExistsTest extends AbstractLDAPConnectorTest
{

    /**
     * 
     */
    public LDAPExistsTest()
    {
    }

    @Override
    protected String getConfigResources()
    {
        return "exists-mule-config.xml";
    }
    
    @Test
    public void testExists() throws Exception
    {
        String dn = "uid=user1,ou=people,dc=mulesoft,dc=org";
        Boolean result = (Boolean) runFlow("testExistsFlow", dn);
        
        assertTrue(result);
    }

    @Test
    public void testNotExists() throws Exception
    {
        String dn = "uid=userXXX,ou=people,dc=mulesoft,dc=org";
        Boolean result = (Boolean) runFlow("testExistsFlow", dn);
        
        assertFalse(result);
    }
}


