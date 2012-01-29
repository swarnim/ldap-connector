/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: NameAlreadyBoundException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 25, 2006 (9:30:46 AM) 
 */

package org.mule.module.ldap.ldap.api;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class NameAlreadyBoundException extends LDAPException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 3869273274579892302L;

    public NameAlreadyBoundException()
    {
        super();
    }

    public NameAlreadyBoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NameAlreadyBoundException(String message)
    {
        super(message);
    }

    public NameAlreadyBoundException(Throwable cause)
    {
        super(cause);
    }
}
