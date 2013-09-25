package app;

import org.apache.isis.viewer.wicket.viewer.registries.components.ComponentFactoryRegistrarDefault;

import com.google.inject.Singleton;


@Singleton
public class ContactoVOFactoryRegistrar extends ComponentFactoryRegistrarDefault {
	    @Override
	    public void addComponentFactories(ComponentFactoryList componentFactories) {
	        componentFactories.add(new ContactoVOPanelFactory());
	        super.addComponentFactories(componentFactories);
	    }
	}