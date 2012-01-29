/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: LDAPEntry.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 22, 2006 (1:01:40 AM) 
 */

package org.mule.module.ldap.ldap.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class LDAPEntry implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 194192089581212405L;

    private String dn = null;
    private LDAPEntryAttributes attributes = new LDAPEntryAttributes();

    /**
	 * 
	 */
    public LDAPEntry()
    {
        this(null);
    }

    /**
     * @param dn
     */
    public LDAPEntry(String dn)
    {
        super();
        setDn(dn);
    }

    /**
     * @return Returns the dn.
     */
    public String getDn()
    {
        return dn;
    }

    /**
     * @param dn The dn to set.
     */
    public void setDn(String dn)
    {
        this.dn = dn;
    }

    /**
     * @param attribute
     */
    public void addAttribute(LDAPEntryAttribute attribute)
    {
        this.attributes.addAttribute(attribute);
    }

    /**
     * @param name
     * @return
     */
    public LDAPEntryAttribute getAttribute(String name)
    {
        if (name != null)
        {
            return getAttributes().getAttribute(name);
        }
        else
        {
            return null;
        }
    }

    /**
	 * 
	 *
	 */
    public void resetAttributes()
    {
        this.attributes.resetAttributes();
    }

    /**
     * @return
     */
    public int getAttributeCount()
    {
        return this.attributes.getCount();
    }

    /**
     * @return
     */
    public Iterator<LDAPEntryAttribute> attributes()
    {
        return this.attributes.attributes();
    }

    /**
     * @return
     */
    public String toLDIFString()
    {

        StringBuilder entryLdif = new StringBuilder();

        entryLdif.append("dn: " + getDn() + LDAPUtils.NEW_LINE);

        LDAPEntryAttribute anAttr;
        for (Iterator<LDAPEntryAttribute> it = attributes(); it.hasNext();)
        {
            anAttr = it.next();
            entryLdif.append(anAttr.toLDIFString());
        }
        entryLdif.append(LDAPUtils.NEW_LINE);

        return entryLdif.toString();
    }

    public Map<String, Object> toMap()
    {
        Map<String, Object> entry = new HashMap<String, Object>();
        
        entry.put("dn", getDn());

        LDAPEntryAttribute anAttr;
        for (Iterator<LDAPEntryAttribute> it = attributes(); it.hasNext();)
        {
            anAttr = it.next();
            if(!anAttr.isMultiValued())
            {
                anAttr.getValue();
            }
            else
            {
                entry.put(anAttr.getName(), anAttr.getValues());
            }
        }        
        
        return entry;
    }
    
    public String toString()
    {
        return toLDIFString();
    }
    
    /**
     * @return Returns the attributes.
     */
    public LDAPEntryAttributes getAttributes()
    {
        return attributes;
    }

    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes(LDAPEntryAttributes attributes)
    {
        this.attributes = attributes;
    }
}
