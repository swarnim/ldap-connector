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

import org.mule.module.ldap.ldap.api.LDAPSearchControls;

public enum SearchScope
{
    /**
     * Object
     */
    OBJECT(LDAPSearchControls.OBJECT_SCOPE),
    
    /**
     * One Level
     */
    ONE_LEVEL(LDAPSearchControls.ONELEVEL_SCOPE),
    
    /**
     * Sub Tree
     */
    SUB_TREE(LDAPSearchControls.SUBTREE_SCOPE);
    
    private int value;
    
    private SearchScope(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return this.value;
    }
}


