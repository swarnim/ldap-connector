/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap.api.jndi;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsResponseControl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.module.ldap.api.LDAPEntry;
import org.mule.module.ldap.api.LDAPException;
import org.mule.module.ldap.api.LDAPResultSet;
import org.mule.module.ldap.api.LDAPSearchControls;

public class PagedLDAPResultSet implements LDAPResultSet
{
    protected final Log logger = LogFactory.getLog(getClass());

    private String baseDn;
    private String filter;
    private Object[] filterArgs;
    private LdapContext conn;
    private LDAPSearchControls controls;
    
    private NamingEnumeration<SearchResult> entries = null;
    
    private byte[] cookie = null;
    
    /**
     * 
     */
    public PagedLDAPResultSet(String baseDn, String filter, Object[] filterArgs, LdapContext conn, LDAPSearchControls controls, NamingEnumeration<SearchResult> entries)
    {
        this.baseDn = baseDn;
        this.filter = filter;
        this.filterArgs = filterArgs;
        this.controls = controls;
        this.conn = conn;
        this.entries = entries;
    }
    
    /**
     * @throws LDAPException
     * @see org.mule.module.ldap.api.LDAPResultSet#close()
     */
    @Override
    public void close() throws LDAPException
    {
        try
        {
            closeResultSet();
        }
        finally
        {
            if(this.conn != null)
            {
                try
                {
                    this.conn.close();
                }
                catch(NamingException nex)
                {
                    throw LDAPException.create(nex);
                }
                finally
                {
                    this.conn = null;
                }
            }
        }
    }

    private void closeResultSet() throws LDAPException
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
    
    /**
     * @return
     * @throws LDAPException
     * @see org.mule.module.ldap.api.LDAPResultSet#next()
     */
    @Override
    public LDAPEntry next() throws LDAPException
    {
        if(hasNext()) // Force navigating to next page
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
        }
        throw new NoSuchElementException();
    }

    private void getNextPage() throws LDAPException
    {
        try
        {
            silentCloseEntriesEnumeration();
            
            this.conn.setRequestControls(LDAPJNDIUtils.buildRequestControls(controls, cookie));
            if(filterArgs != null && filterArgs.length > 0)
            {
                this.entries = this.conn.search(baseDn, filter, filterArgs, LDAPJNDIUtils.buildSearchControls(controls));
            }
            else
            {
                this.entries = this.conn.search(baseDn, filter, LDAPJNDIUtils.buildSearchControls(controls));
            }
        }
        catch(NamingException nex)
        {
            throw LDAPException.create(nex);
        }
    }
    
    private void silentCloseEntriesEnumeration()
    {
        if(this.entries != null)
        {
            try
            {
                this.entries.close();
            } 
            catch(NamingException nex)
            {
                // Ignore
            }
            finally
            {
                this.entries = null;
            }
        }
    }
    
    /**
     * @return
     * @throws LDAPException
     * @see org.mule.module.ldap.api.LDAPResultSet#hasNext()
     */
    @Override
    public boolean hasNext() throws LDAPException
    {
        try
        {
            if(this.entries != null)
            {
                if(!this.entries.hasMore())
                {
                    this.cookie = getPagedResultsResponseControlCookie();
                    if(this.cookie != null)
                    {
                        getNextPage();
                        return this.entries != null && this.entries.hasMore();
                    }
                    else
                    {
                        return false;
                    }
                   
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        catch(SizeLimitExceededException slee)
        {
            logger.warn("Size limit exceeded. Max results is: " + this.controls.getMaxResults(), slee);
            return false;
        }
        catch(NamingException nex)
        {
            throw LDAPException.create(nex);
        }
    }

    private byte[] getPagedResultsResponseControlCookie() throws LDAPException
    {
        try
        {
            // Examine the paged results control response
            Control[] responseControls = this.conn.getResponseControls();
            if (responseControls != null)
            {
                for (int i = 0; i < responseControls.length; i++)
                {
                    if (responseControls[i] instanceof PagedResultsResponseControl)
                    {
                        PagedResultsResponseControl prrc = (PagedResultsResponseControl)responseControls[i];
                        return prrc.getCookie();
                    }
                }
            }
            return null;
        }
        catch(NamingException nex)
        {
            throw LDAPException.create(nex);
        }
    }
    
    /**
     * @return
     * @throws LDAPException
     * @see org.mule.module.ldap.api.LDAPResultSet#getAllEntries()
     */
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


