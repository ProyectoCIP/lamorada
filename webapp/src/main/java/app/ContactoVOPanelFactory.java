package app;

import org.apache.isis.viewer.wicket.model.models.AboutModel;
import org.apache.isis.viewer.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public class ContactoVOPanelFactory extends ComponentFactoryAbstract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -795959377032401308L;

	public ContactoVOPanelFactory() {
        super(ComponentType.ENTITY_LINK);
    }

    @Override
    public ApplicationAdvice appliesTo(final IModel<?> model) {
        return appliesIf(model instanceof AboutModel);
    }

    @Override
    public Component createComponent(final String id, final IModel<?> model) {
        final AboutModel aboutModel = (AboutModel) model;
        return new ContactoVOPanel(id, aboutModel);
    }

}
