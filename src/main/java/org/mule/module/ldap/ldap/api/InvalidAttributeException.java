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
 * File: InvalidAttributeException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 25, 2006 (9:30:24 AM) 
 */

package org.mule.module.ldap.ldap.api;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class InvalidAttributeException extends LDAPException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1608293876428310202L;

    public InvalidAttributeException()
    {
        super();
    }

    public InvalidAttributeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidAttributeException(String message)
    {
        super(message);
    }

    public InvalidAttributeException(Throwable cause)
    {
        super(cause);
    }
}
