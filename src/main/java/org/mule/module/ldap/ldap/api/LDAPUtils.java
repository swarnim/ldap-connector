/**
 * Mule LDAP Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.ldap.ldap.api;

import java.io.IOException;

import org.mule.util.Base64;

public class LDAPUtils
{
    public static final String NEW_LINE = System.getProperty("line.separator") != null ? System.getProperty("line.separator") : "\n";

    /**
     * 
     */
    public LDAPUtils()
    {
    }

    public static String encodeBase64(byte value[])
    {
        try
        {
            return Base64.encodeBytes(value, Base64.DONT_BREAK_LINES);
        }
        catch (IOException ex)
        {
            return null;
        }
            
    }    
}


