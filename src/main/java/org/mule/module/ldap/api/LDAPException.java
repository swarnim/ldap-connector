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
 * File: LDAPException.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 22, 2006 (12:59:56 AM) 
 */

package org.mule.module.ldap.api;

import java.util.HashMap;
import java.util.Map;

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
    private static Map<Class<? extends NamingException>, Class<? extends LDAPException>> EX_MAPPINGS = new HashMap<Class<? extends NamingException>, Class<? extends LDAPException>>();

    static
    {
        EX_MAPPINGS.put(javax.naming.AuthenticationException.class, AuthenticationException.class);
        EX_MAPPINGS.put(javax.naming.NameNotFoundException.class, NameNotFoundException.class);
        EX_MAPPINGS.put(javax.naming.CommunicationException.class, CommunicationException.class);
        EX_MAPPINGS.put(javax.naming.NoPermissionException.class, NoPermissionException.class);
        EX_MAPPINGS.put(javax.naming.InvalidNameException.class, InvalidAttributeException.class);
        EX_MAPPINGS.put(javax.naming.directory.InvalidAttributeValueException.class, InvalidAttributeException.class);
        EX_MAPPINGS.put(javax.naming.directory.SchemaViolationException.class, InvalidEntryException.class);
        EX_MAPPINGS.put(javax.naming.directory.InvalidAttributesException.class, InvalidEntryException.class);
        EX_MAPPINGS.put(javax.naming.NameAlreadyBoundException.class, NameAlreadyBoundException.class);
        EX_MAPPINGS.put(javax.naming.directory.InvalidAttributeIdentifierException.class, InvalidAttributeException.class);
        EX_MAPPINGS.put(javax.naming.directory.AttributeInUseException.class, InvalidAttributeException.class);
        EX_MAPPINGS.put(javax.naming.ContextNotEmptyException.class, ContextNotEmptyException.class);
        
        
    }
    
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

    /**
     * 
     * @param nex
     * @return
     */
    public static LDAPException create(NamingException nex)
    {
        Class<? extends LDAPException> exClass = EX_MAPPINGS.get(nex.getClass());
        try
        {
            return (LDAPException) exClass.getDeclaredConstructor(String.class, Throwable.class).newInstance(nex.getMessage(), nex);
        }
        catch(Throwable ex)
        {
            // Null pointer, reflection exceptions, etc.
            return new LDAPException(nex.getMessage(), nex);
        }
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
