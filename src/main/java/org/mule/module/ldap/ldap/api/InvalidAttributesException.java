/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: InvalidAttributesException.java
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
public class InvalidAttributesException extends LDAPException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1608293876428310202L;

    public InvalidAttributesException()
    {
        super();
    }

    public InvalidAttributesException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidAttributesException(String message)
    {
        super(message);
    }

    public InvalidAttributesException(Throwable cause)
    {
        super(cause);
    }
}
