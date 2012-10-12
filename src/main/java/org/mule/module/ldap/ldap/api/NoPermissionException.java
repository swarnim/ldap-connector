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
 * File: NoPermissionException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 25, 2006 (9:12:54 AM) 
 */

package org.mule.module.ldap.ldap.api;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class NoPermissionException extends LDAPException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -5581541484202999596L;

    public NoPermissionException()
    {
        super();
    }

    public NoPermissionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NoPermissionException(String message)
    {
        super(message);
    }

    public NoPermissionException(Throwable cause)
    {
        super(cause);
    }

}
