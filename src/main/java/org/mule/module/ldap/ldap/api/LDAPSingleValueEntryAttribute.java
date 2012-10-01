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
 * File: LDAPSingleValueEntryAttribute.java
 *
 * Property of Leonards / Mindpool
 * Created on Jun 24, 2006 (10:59:34 PM) 
 */

package org.mule.module.ldap.ldap.api;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the abstraction
 * 
 * @author mariano
 */
public class LDAPSingleValueEntryAttribute extends LDAPEntryAttribute
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 2501023016379810331L;

    private Object value = null;

    /**
	 * 
	 */
    public LDAPSingleValueEntryAttribute()
    {
        super();
    }

    /**
     * @param name
     */
    public LDAPSingleValueEntryAttribute(String name)
    {
        super(name);
    }

    /**
     * @param name
     * @param value
     */
    public LDAPSingleValueEntryAttribute(String name, Object value)
    {
        this(name);
        setValue(value);
    }

    /**
     * @return
     * @see leonards.common.ldap.LDAPEntryAttribute#getValue()
     */
    public Object getValue()
    {
        return this.value;
    }

    /**
     * @return
     * @see leonards.common.ldap.LDAPEntryAttribute#getValues()
     */
    public List<Object> getValues()
    {
        List<Object> values = new ArrayList<Object>();
        values.add(this.value);
        return values;
    }

    /**
     * @return
     * @see leonards.common.ldap.LDAPEntryAttribute#isMultiValued()
     */
    public boolean isMultiValued()
    {
        return false;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(Object value)
    {
        this.value = value;
    }

}
