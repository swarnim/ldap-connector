/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.ldap.ldap.api.jndi;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.mule.module.ldap.ldap.api.LDAPEntry;
import org.mule.module.ldap.ldap.api.LDAPException;
import org.mule.module.ldap.ldap.api.LDAPResultSet;
import org.mule.module.ldap.ldap.api.LDAPSearchControls;

public class SimpleLDAPResultSet implements LDAPResultSet
{
    private NamingEnumeration<SearchResult> entries = null;
    private String baseDn = null;
    
    /**
     * 
     */
    public SimpleLDAPResultSet(String baseDn, LdapContext conn, LDAPSearchControls controls, NamingEnumeration<SearchResult> entries)
    {
        this.entries = entries;
        this.baseDn = baseDn;
    }

    /**
     * 
     * @return
     * @see org.mule.module.ldap.ldap.api.LDAPResultSet#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return this.entries != null ? this.entries.hasMoreElements() : false;
    }

    /**
     * 
     * @return
     * @throws LDAPException
     * @see org.mule.module.ldap.ldap.api.LDAPResultSet#next()
     */
    @Override
    public LDAPEntry next() throws LDAPException
    {
        SearchResult searchResult = (SearchResult) this.entries.nextElement();
        String entryDn;
        if (searchResult != null)
        {
            entryDn = searchResult.getName();
            if (searchResult.isRelative())
            {
                entryDn += "," + baseDn;
            }
            return LDAPJNDIUtils.buildEntry(entryDn, searchResult.getAttributes());
        }
        else
        {
            throw new NoSuchElementException();
        }
    }


    /**
     * @throws LDAPException
     * @see org.mule.module.ldap.ldap.api.LDAPResultSet#close()
     */
    @Override
    public void close() throws LDAPException
    {
        try
        {
            if(this.entries != null)
            {
                this.entries.close();
            }
        }
        catch(NamingException nex)
        {
            throw LDAPException.create(nex);
        }
        finally
        {
            this.entries = null;
        }
    }

    @Override
    public List<LDAPEntry> getAllEntries() throws LDAPException
    {
        List<LDAPEntry> allEntries = new ArrayList<LDAPEntry>();
        
        while(hasNext())
        {
            LDAPEntry entry = next();
            if(entry != null)
            {
                allEntries.add(entry);
            }
        }
        
        return allEntries;
    }

}

