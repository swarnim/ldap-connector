/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap.api;

public class ContextNotEmptyException extends LDAPException
{

    private static final long serialVersionUID = 1223165927506950877L;

    /**
     * 
     */
    public ContextNotEmptyException()
    {
    }

    /**
     * @param message
     * @param cause
     */
    public ContextNotEmptyException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ContextNotEmptyException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public ContextNotEmptyException(Throwable cause)
    {
        super(cause);
    }

}


