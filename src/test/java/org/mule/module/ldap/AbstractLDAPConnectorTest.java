/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mule.module.ldap;

import java.io.File;

import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.tck.FunctionalTestCase;
import org.mule.util.FileUtils;
import org.springframework.security.ldap.server.ApacheDSContainer;

public abstract class AbstractLDAPConnectorTest extends FunctionalTestCase
{
    private ApacheDSContainer ldapServer;
    public static int LDAP_PORT = 10389;
    public static File WORKING_DIRECTORY = new File(System.getProperty("java.io.tmpdir") + File.separator + "ldap-connector-junit-server");
    
    @Override
    protected void doSetUp() throws Exception {
        try
        {
            FileUtils.deleteDirectory(WORKING_DIRECTORY);
            
            ldapServer = new ApacheDSContainer("dc=mulesoft,dc=org", "classpath:test-server.ldif");
            ldapServer.setWorkingDirectory(WORKING_DIRECTORY);
            ldapServer.setPort(LDAP_PORT);
            ldapServer.afterPropertiesSet();
            ldapServer.start();
        }
        finally
        {
            super.doSetUp();
        }
        
    }

    @Override
    protected void doTearDown() throws Exception {
        try
        {
            if (ldapServer != null) {
                ldapServer.stop();
            }
        }
        finally
        {
            super.doTearDown();
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
         MuleEvent event = AbstractMuleTestCase.getTestEvent(payload);
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
      protected <T, U> void runFlowWithPayloadAndExpectException(String flowName, Class<T> expect, U payload) throws Exception
      {
          Flow flow = lookupFlowConstruct(flowName);
          MuleEvent event = AbstractMuleTestCase.getTestEvent(payload);
          MuleEvent responseEvent = flow.process(event);

          assertNotNull(responseEvent.getMessage().getExceptionPayload());
          // responseEvent.getMessage().getExceptionPayload().getException() holds the messaging exception
          assertEquals(expect, responseEvent.getMessage().getExceptionPayload().getException().getCause().getClass());
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
    protected Flow lookupFlowConstruct(String name)
    {
        return (Flow) AbstractMuleTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
