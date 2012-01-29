/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.ldap.ldap.api;

public class CommunicationException extends LDAPException
{

    private static final long serialVersionUID = 7403927535538538812L;

    /**
     * 
     */
    public CommunicationException()
    {
    }

    /**
     * @param message
     * @param cause
     */
    public CommunicationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message
     */
    public CommunicationException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public CommunicationException(Throwable cause)
    {
        super(cause);
    }
}


