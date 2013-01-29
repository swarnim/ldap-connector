/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class BypassTrustManager implements X509TrustManager {

	  public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException
	  {
	    // do nothing
	  }
	  
	  public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException
	  {
	    // do nothing
	  }
	  
	  public X509Certificate[] getAcceptedIssuers()
	  {
	    return new java.security.cert.X509Certificate[0];
	  }
}
