/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: AuthenticationException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 23, 2006 (12:58:02 AM) 
 */

package org.mule.module.ldap.ldap.api;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class AuthenticationException extends LDAPException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 6434067405937564392L;

    public AuthenticationException()
    {
        super();
    }

    public AuthenticationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AuthenticationException(String message)
    {
        super(message);
    }

    public AuthenticationException(Throwable cause)
    {
        super(cause);
    }

}
