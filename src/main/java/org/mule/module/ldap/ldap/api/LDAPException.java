/**
 * Mule LDAP Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: LDAPException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 22, 2006 (12:59:56 AM) 
 */

package org.mule.module.ldap.ldap.api;

import javax.naming.NamingException;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class LDAPException extends Exception
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1220777755286608188L;

    public LDAPException()
    {
        super();
    }

    public LDAPException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public LDAPException(String message)
    {
        super(message);
    }

    public LDAPException(Throwable cause)
    {
        super(cause);
    }

    public String getCode()
    {
        if(getCause() instanceof NamingException)
        {
            NamingException nex = (NamingException) getCause();
            return nex.getRootCause() != null ? nex.getRootCause().getMessage() : nex.getExplanation();
        }
        else
        {
            return null;
        }
    }
}
