/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import org.junit.Test;

public class LDAPConfigTest extends AbstractLDAPConnectorTest
{

    /**
     * 
     */
    public LDAPConfigTest()
    {
    }

    /**
     * @return
     * @see org.mule.tck.junit4.FunctionalTestCase#getConfigResources()
     */
    @Override
    protected String getConfigResources()
    {
        return "config-mule-config.xml";
    }

    @Test
    public void testEmbeddedExtendedAttributes()
    {
        Object o = muleContext.getRegistry().get("embeddedExtendedConfig");
        
        System.out.println(o);
    }
}


