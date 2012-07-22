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

public enum Referral
{
    IGNORE("ignore"), THROW("throw"), FOLLOW("follow"); 
    
    private String referral;
    
    private Referral(String referral)
    {
        this.referral = referral;
    }
    
    public String toString()
    {
        return this.referral;
    }
}


