/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: NameNotFoundException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 23, 2006 (12:56:19 AM) 
 */

package org.mule.module.ldap.ldap.api;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class NameNotFoundException extends LDAPException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -4610170776459043832L;

    public NameNotFoundException()
    {
        super();
    }

    public NameNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NameNotFoundException(String message)
    {
        super(message);
    }

    public NameNotFoundException(Throwable cause)
    {
        super(cause);
    }

}
