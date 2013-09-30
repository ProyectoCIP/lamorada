package app;

import org.apache.isis.viewer.wicket.viewer.registries.components.ComponentFactoryRegistrarDefault;

import com.google.inject.Singleton;

@Singleton
public class ComponentFactoryRegistrarForContactoVO extends ComponentFactoryRegistrarDefault {

	@Override
    public void addComponentFactories(ComponentFactoryList componentFactories) {
        super.addComponentFactories(componentFactories);
        // currently no replacements
    }
	
}
