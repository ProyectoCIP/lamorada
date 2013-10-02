/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.apache.isis.viewer.wicket.ui.pages.*;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.isis.viewer.wicket.ui.app.registry.ComponentFactoryRegistrar;
import org.apache.isis.viewer.wicket.viewer.IsisWicketApplication;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.AbstractScheduledService.Scheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;

import dom.correo.ICorreo;
import dom.correo.Recepcion;


/**
 * As specified in <tt>web.xml</tt>.
 * 
 * <p>
 * See:
 * <pre>
 * &lt;filter>
 *   &lt;filter-name>wicket&lt;/filter-name>
 *    &lt;filter-class>org.apache.wicket.protocol.http.WicketFilter&lt;/filter-class>
 *    &lt;init-param>
 *      &lt;param-name>applicationClassName&lt;/param-name>
 *      &lt;param-value>app.LaMoradaStartApplication&lt;/param-value>
 *    &lt;/init-param>
 * &lt;/filter>
 * </pre>
 * 
 */
public class LaMoradaStartApplication extends IsisWicketApplication {
	
	private static final long serialVersionUID = 1L;

    @Override
    protected Module newIsisWicketModule() {
    
    	try {
    		
    	    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    		org.quartz.Scheduler scheduler = schedulerFactory.getScheduler();
    	
    		schedulerFactory = new StdSchedulerFactory();
            String cron = "0 0/1 * * * ?";
            scheduler.start();
       
            @SuppressWarnings("deprecation")
			JobDetail checkearCorreos = new JobDetailImpl("CHECKEO_PROGRAMADO", org.quartz.Scheduler.DEFAULT_GROUP, dom.correo.Recepcion.class);
            @SuppressWarnings("deprecation")
			CronTrigger crTrigger = new CronTriggerImpl("crTriggerSaludo",org.quartz.Scheduler.DEFAULT_GROUP,cron);
            scheduler.scheduleJob(checkearCorreos, crTrigger);
        	
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}

    	
    	final Module isisDefaults = super.newIsisWicketModule();
        
        final Module LaMoradaStartOverrides = new AbstractModule() {
            @Override
            protected void configure() {
            	
            	//bind(ComponentFactoryRegistrar.class).to(ComponentFactoryRegistrarForLaMoradaStart.class);
                bind(ComponentFactoryRegistrar.class).to(ContactoVOPagePanelFactoryRegistrar.class);
                bind(String.class).annotatedWith(Names.named("applicationName")).toInstance("La Morada");
                bind(String.class).annotatedWith(Names.named("applicationFooter")).toInstance("Capo");
                bind(String.class).annotatedWith(Names.named("applicationCss")).toInstance("css/application.css");
                bind(String.class).annotatedWith(Names.named("applicationJs")).toInstance("scripts/application.js");
                bind(String.class).annotatedWith(Names.named("welcomeMessage")).toInstance(readLines("welcome.html"));                
                bind(String.class).annotatedWith(Names.named("aboutMessage")).toInstance("La Morada");
                bind(InputStream.class).annotatedWith(Names.named("metaInfManifest")).toProvider(Providers.of(getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF")));
            }
        };

        return Modules.override(isisDefaults).with(LaMoradaStartOverrides);
    }

    private static String readLines(final String resourceName) {
        try {
            List<String> readLines = Resources.readLines(Resources.getResource(LaMoradaStartApplication.class, resourceName), Charset.defaultCharset());
            final String aboutText = Joiner.on("\n").join(readLines);
            return aboutText;
        } catch (IOException e) {
            return "La Morada Petit Hotel";
        }
    }
    
    private void addUserName() {
    	
    }

}
