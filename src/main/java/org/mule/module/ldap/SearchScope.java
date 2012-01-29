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

import org.mule.module.ldap.ldap.api.LDAPSearchControls;

public enum SearchScope
{
    OBJECT(LDAPSearchControls.OBJECT_SCOPE),
    ONE_LEVEL(LDAPSearchControls.ONELEVEL_SCOPE),
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


