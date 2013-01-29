/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap.api.jndi;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestLDAPJNDIUtils
{
    
    /**
     * 
     */
    public TestLDAPJNDIUtils()
    {
    }

    @Test
    public void testContainsDnValue()
    {
        final String dn1 = "uid=user1, ou=people, dc=mulesoft, dc=org";
        final String dn2 = "uid=user2, ou=people, dc=mulesoft, dc=org";
        final String dn3 = "uid=user3, ou=people, dc=mulesoft, dc=org"; 
        
        final List<Object> dns = new ArrayList<Object>();
        dns.add(dn1);
        dns.add(dn2);
        
        assertTrue(LDAPJNDIUtils.containsDnValue(dn1, dns));
        assertTrue(LDAPJNDIUtils.containsDnValue(dn2, dns));
        assertFalse(LDAPJNDIUtils.containsDnValue(dn3, dns));
        
        assertTrue(LDAPJNDIUtils.containsDnValue("uid=user1,ou=people,dc=mulesoft,dc=org", dns));
        assertTrue(LDAPJNDIUtils.containsDnValue("uid=user2,ou=People, dc=mulesoft, dc=org", dns));

        assertFalse(LDAPJNDIUtils.containsDnValue("not-a-dn", dns));
        
    }
}


