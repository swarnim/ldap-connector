/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap.ldap.api.jndi;

import java.io.IOException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.Control;
import javax.naming.ldap.PagedResultsControl;

import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.LDAPEntryAttribute;
import org.mule.module.ldap.ldap.api.LDAPException;
import org.mule.module.ldap.ldap.api.LDAPMultiValueEntryAttribute;
import org.mule.module.ldap.ldap.api.LDAPSearchControls;
import org.mule.module.ldap.ldap.api.LDAPSingleValueEntryAttribute;

public class LDAPJNDIUtils
{

    /**
     * 
     */
    public LDAPJNDIUtils()
    {
    }

    /**
     * @param entryDN
     * @param attributes
     * @return
     * @throws LDAPException
     */
    public static LDAPEntry buildEntry(String entryDN, Attributes attributes) throws LDAPException
    {
        LDAPEntry anEntry = new LDAPEntry(entryDN);
        if (attributes != null)
        {
            try
            {
                for (NamingEnumeration<?> attrs = attributes.getAll(); attrs.hasMore();)
                {
                    anEntry.addAttribute(buildAttribute((Attribute) attrs.nextElement()));
                }
            }
            catch (NamingException nex)
            {
                throw LDAPException.create(nex);
            }
        }
        return anEntry;
    }    
    
    /**
     * @param attribute
     * @return
     * @throws LDAPException
     */
    protected static LDAPEntryAttribute buildAttribute(Attribute attribute) throws LDAPException
    {
        if (attribute != null)
        {
            try
            {
                if (attribute.size() > 1)
                {
                    LDAPMultiValueEntryAttribute newAttribute = new LDAPMultiValueEntryAttribute();
                    newAttribute.setName(attribute.getID());
                    NamingEnumeration<?> values = attribute.getAll();
                    while (values.hasMore())
                    {
                        newAttribute.addValue(values.next());
                    }
                    return newAttribute;
                }
                else
                {
                    LDAPSingleValueEntryAttribute newAttribute = new LDAPSingleValueEntryAttribute();
                    newAttribute.setName(attribute.getID());
                    newAttribute.setValue(attribute.get());
                    return newAttribute;
                }
            }
            catch (NamingException nex)
            {
                throw LDAPException.create(nex);
            }
        }
        else
        {
            return null;
        }
    }   
    
    /**
     * 
     * @param controls
     * @param cookie
     * @return
     * @throws LDAPException
     */
    public static Control[] buildRequestControls(LDAPSearchControls controls, byte[] cookie) throws LDAPException
    {
        try
        {
            if(controls.isPagingEnabled())
            {
                if(cookie != null)
                {
                    return new Control[] {new PagedResultsControl(controls.getPageSize(), cookie, Control.CRITICAL)};
                }
                else
                {
                    return new Control[] {new PagedResultsControl(controls.getPageSize(), Control.CRITICAL)};
                }
            }
            else
            {
                return new Control[0];
            }
        }
        catch(IOException ex)
        {
            throw new LDAPException("Could not create request paging controls", ex);
        }
    }    
    
    /**
     * @param controls
     * @return
     */
    public static SearchControls buildSearchControls(LDAPSearchControls controls)
    {
        SearchControls ctrls = new SearchControls();
        ctrls.setCountLimit(controls.getMaxResults());
        ctrls.setReturningAttributes(controls.getAttributesToReturn());
        ctrls.setReturningObjFlag(controls.isReturnObject());
        ctrls.setSearchScope(transformScope(controls.getScope()));
        ctrls.setTimeLimit(controls.getTimeout());
        return ctrls;
    }    
    
    /**
     * @param scope
     * @return
     */
    private static int transformScope(int scope)
    {
        switch (scope)
        {
            case LDAPSearchControls.OBJECT_SCOPE :
                return SearchControls.OBJECT_SCOPE;
            case LDAPSearchControls.ONELEVEL_SCOPE :
                return SearchControls.ONELEVEL_SCOPE;
            case LDAPSearchControls.SUBTREE_SCOPE :
                return SearchControls.SUBTREE_SCOPE;
            default :
                return SearchControls.ONELEVEL_SCOPE;
        }
    }      
}


