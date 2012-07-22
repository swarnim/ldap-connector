/**
 * Mule LDAP Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: LDAPResultSet.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 22, 2006 (1:00:57 AM) 
 */

package org.mule.module.ldap.ldap.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class LDAPResultSet implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 8725620491032853115L;

    private List<LDAPEntry> entries = null;

    /**
	 * 
	 */
    public LDAPResultSet()
    {
        super();
    }

    /**
     * @return Returns the entries.
     */
    public List<LDAPEntry> getEntries()
    {
        if (entries == null)
        {
            setEntries(new ArrayList<LDAPEntry>());
        }
        return entries;
    }

    /**
     * @param entries The entries to set.
     */
    protected void setEntries(List<LDAPEntry> entries)
    {
        this.entries = entries;
    }

    /**
     * @return
     */
    public Iterator<LDAPEntry> entries()
    {
        return getEntries().iterator();
    }

    /**
     * @param entry
     */
    public void addEntry(LDAPEntry entry)
    {
        getEntries().add(entry);
    }

    /**
     * @param idx
     * @return
     */
    public LDAPEntry getEntry(int idx)
    {
        return (LDAPEntry) getEntries().get(idx);
    }

    /**
	 * 
	 */
    public void close()
    {
        if (this.entries != null)
        {
            this.entries.clear();
            this.entries = null;
        }
    }
}
