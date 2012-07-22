/*
 * Project: Leonards Common Libraries
 * This class is member of leonards.common.ldap
 * File: LDAPMultiValueEntryAttribute.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 24, 2006 (11:58:07 PM) 
 */

package org.mule.module.ldap.ldap.api;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class LDAPMultiValueEntryAttribute extends LDAPEntryAttribute
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 2022378719197583814L;

    private List<Object> values = null;

    /**
	 * 
	 */
    public LDAPMultiValueEntryAttribute()
    {
        super();
    }

    /**
     * @param name
     * @param values
     */
    public LDAPMultiValueEntryAttribute(String name, Object values[])
    {
        this(name);
        addValues(values);
    }

    /**
     * @param name
     * @param values
     */
    public LDAPMultiValueEntryAttribute(String name, List<Object> values)
    {
        this(name);
        addValues(values != null ? values.toArray() : null);
    }

    /**
     * @param name
     */
    public LDAPMultiValueEntryAttribute(String name)
    {
        super(name);
    }

    /**
     * @return
     * @see leonards.common.ldap.LDAPEntryAttribute#getValue()
     */
    public Object getValue()
    {
        return !values.isEmpty() ? values.get(0) : null;
    }

    /**
     * @return
     * @see leonards.common.ldap.LDAPEntryAttribute#getValues()
     */
    public List<Object> getValues()
    {
        if (values == null)
        {
            values = new ArrayList<Object>();
        }
        return values;
    }

    /**
     * @return
     * @see leonards.common.ldap.LDAPEntryAttribute#isMultiValued()
     */
    public boolean isMultiValued()
    {
        return true;
    }

    /**
     * @param value
     */
    public void addValue(Object value)
    {
        getValues().add(value);
    }

    /**
     * @param values
     */
    public void addValues(Object values[])
    {
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                getValues().add(values[i]);
            }
        }

    }
}
