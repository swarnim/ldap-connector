/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.directory.server.core.schema.SchemaInterceptor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mule.api.MessagingException;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.FileUtils;
import org.springframework.security.ldap.server.ApacheDSContainer;

public abstract class AbstractLDAPConnectorTest extends FunctionalTestCase
{
    private static ApacheDSContainer ldapServer;
    public static int LDAP_PORT = 10389;
    public static File WORKING_DIRECTORY = new File(System.getProperty("java.io.tmpdir") + File.separator + "ldap-connector-junit-server");
    
    @BeforeClass
    public static void startLdapServer() throws Exception {
        FileUtils.deleteDirectory(WORKING_DIRECTORY);
        
        ldapServer = new ApacheDSContainer("dc=mulesoft,dc=org", "classpath:test-server.ldif");
        ldapServer.setWorkingDirectory(WORKING_DIRECTORY);
        ldapServer.setPort(LDAP_PORT);
        ldapServer.getService().setAllowAnonymousAccess(true);
        ldapServer.getService().setAccessControlEnabled(true);
        ldapServer.getService().setShutdownHookEnabled(true);
        
        ldapServer.getService().getInterceptors().add(new SchemaInterceptor());
        ldapServer.afterPropertiesSet(); // This method calls start
        ldapServer.getService().getSchemaService().getRegistries();
    }

    @AfterClass
    public static void stopLdapServer() throws Exception {
        if (ldapServer != null) {
            ldapServer.stop();
        }
    }    
    
    /**
     * Run the flow specified by name using the specified payload and assert
     * equality on the expected output
     *
     * @param flowName The name of the flow to run
     * @param payload The payload of the input event
     */
     protected <U> Object runFlow(String flowName, U payload) throws Exception
     {
         Flow flow = lookupFlowConstruct(flowName);
         MuleEvent event = getTestEvent(payload);
         MuleEvent responseEvent = flow.process(event);
         
         return responseEvent.getMessage().getPayload();
     }
     
     /**
      * Run the flow specified by name using the specified payload and assert
      * equality on the expected exception
      *
      * @param flowName The name of the flow to run
      * @param expect The expected exception
      * @param payload The payload of the input event
      */
      protected <T, U> Throwable runFlowWithPayloadAndExpectException(String flowName, Class<T> expect, U payload) throws Exception
      {
          try
          {
              Flow flow = lookupFlowConstruct(flowName);
              MuleEvent event = getTestEvent(payload);
              MuleEvent responseEvent = flow.process(event);

              // Support for mule 3.2.x and previous
              assertNotNull(responseEvent.getMessage().getExceptionPayload());
              assertEquals(expect, responseEvent.getMessage().getExceptionPayload().getException().getCause().getClass());
              
              return responseEvent.getMessage().getExceptionPayload().getException().getCause();
          }
          catch(MessagingException ex)
          {
              // Support for mule 3.3.x
              assertEquals(expect, ex.getCause().getClass());
              return ex.getCause();
          }
      }
      
      /**
       * Run the flow specified by name using the specified payload, expect an exception
       * and return that exception
       *
       * @param flowName The name of the flow to run
       * @param payload The payload of the input event
       * @return The exception
       */
       protected <U> Throwable runFlowWithPayloadAndReturnException(String flowName, U payload) throws Exception
       {
           try
           {
               Flow flow = lookupFlowConstruct(flowName);
               MuleEvent event = getTestEvent(payload);
               MuleEvent responseEvent = flow.process(event);

               // Support for mule 3.2.x and previous
               assertNotNull(responseEvent.getMessage().getExceptionPayload());
               
               return responseEvent.getMessage().getExceptionPayload().getException().getCause();
           }
           catch(MessagingException ex)
           {
               // Support for mule 3.3.x
               return ex.getCause();
           }
       }
       
    /**
    * Run the flow specified by name and assert equality on the expected output
    *
    * @param flowName The name of the flow to run
    * @param expect The expected output
    */
    protected <T> void runFlowAndExpect(String flowName, T expect) throws Exception
    {
        assertEquals(expect, runFlow(flowName, null));
    }

    /**
    * Run the flow specified by name using the specified payload and assert
    * equality on the expected output
    *
    * @param flowName The name of the flow to run
    * @param expect The expected output
    * @param payload The payload of the input event
    */
    protected <T, U> void runFlowWithPayloadAndExpect(String flowName, T expect, U payload) throws Exception
    {
        assertEquals(expect, runFlow(flowName, payload));
    }

    /**
     * Retrieve a flow by name from the registry
     *
     * @param name Name of the flow to retrieve
     */
    protected Flow lookupFlowConstruct(String name) throws Exception
    {
        Flow flow = (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
        if(flow == null)
        {
            throw new Exception("Flow " + name + " is not present in configuration " + getConfigResources());
        }        
        return flow;
    }
}
