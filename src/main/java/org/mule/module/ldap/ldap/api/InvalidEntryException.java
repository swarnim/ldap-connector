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

public class InvalidEntryException extends LDAPException
{

    private static final long serialVersionUID = 8360765951775030530L;

    /**
     * 
     */
    public InvalidEntryException()
    {
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidEntryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message
     */
    public InvalidEntryException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidEntryException(Throwable cause)
    {
        super(cause);
    }

}


