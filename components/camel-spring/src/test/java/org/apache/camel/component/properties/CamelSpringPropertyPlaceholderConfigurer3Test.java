/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.properties;

import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringTestSupport;
import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class CamelSpringPropertyPlaceholderConfigurer3Test extends SpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("org/apache/camel/component/properties/CamelSpringPropertyPlaceholderConfigurer3Test.xml");
    }

    public void testCamelSpringPropertyPlaceholderConfigurerTest() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Guten Tag Camel");

        template.sendBody("direct:bar", "Camel");

        assertMockEndpointsSatisfied();
    }

    @SuppressWarnings("unused")
    private static class MyBridgePropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {

        @Override
        public Properties resolveProperties(CamelContext context, boolean ignoreMissingLocation, String... uri) throws Exception {
            Properties answer = super.resolveProperties(context, ignoreMissingLocation, uri);

            // define the additional properties we need to provide so that the uri "direct:{{foo}}" by the "from" clause
            // as well as "{{scheme}}{{separator}}{{context-path}}" by the "to" clause can be properly resolved. please
            // note that in this simple test we just add these properties hard-coded below but of course the mechanism to
            // retrieve these extra properties can be anything else, e.g. through the entries inside a database table etc.
            answer.put("foo", "bar");

            answer.put("scheme", "mock");
            answer.put("separator", ":");
            answer.put("context-path", "result");
                
            return answer;
        }

    }

}
