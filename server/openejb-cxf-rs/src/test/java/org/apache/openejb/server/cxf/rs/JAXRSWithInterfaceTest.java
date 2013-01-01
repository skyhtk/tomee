/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.apache.openejb.server.cxf.rs;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.junit.Classes;
import org.apache.openejb.junit.EnableServices;
import org.apache.openejb.junit.Module;
import org.apache.openejb.server.cxf.rs.beans.MyExpertRestClass;
import org.apache.openejb.server.cxf.rs.beans.MyFirstRestClass;
import org.apache.openejb.server.cxf.rs.beans.RestWithInjections;
import org.apache.openejb.server.cxf.rs.beans.SimpleEJB;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@EnableServices("jax-rs")
@RunWith(ApplicationComposer.class)
public class JAXRSWithInterfaceTest {
    public static final String BASE_URL = "http://localhost:4204/foo/";

    @Module
    @Classes({ RestWithInjections.class, SimpleEJB.class, MyExpertRestClass.class, MyFirstRestClass.class })
    public WebApp war() {
        return new WebApp()
                .contextRoot("foo")
                .addServlet("REST Application", Application.class.getName())
                .addInitParam("REST Application", "javax.ws.rs.Application", InterfaceApp.class.getName());
    }

    @Test
    public void itf() {
        assertEquals("itf", WebClient.create(BASE_URL).path("itf").get(String.class));
    }

    public static class InterfaceApp extends Application {
        private final Set<Class<?>> classes = new HashSet<Class<?>>();

        public InterfaceApp() {
            classes.add(Impl.class);
        }

        @Override
        public Set<Class<?>> getClasses() {
            return classes;
        }
    }

    public static interface Itf {
        @Path("itf")
        @GET
        String itf();
    }

    public static class Impl implements Itf {
        public String itf() {
            return "itf";
        }
    }
}
