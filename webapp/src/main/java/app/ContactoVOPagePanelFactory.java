package app;

import org.apache.isis.viewer.wicket.model.models.ValueModel;
import org.apache.isis.viewer.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public class ContactoVOPagePanelFactory extends ComponentFactoryAbstract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContactoVOPagePanelFactory() {
        super(ComponentType.VALUE);
    }

    @Override
    public ApplicationAdvice appliesTo(final IModel<?> model) {
        return appliesIf(model instanceof ValueModel);
    }

    @Override
    public Component createComponent(final String id, final IModel<?> model) {
        final ValueModel valueModel = (ValueModel) model;
        return new ContactoVOPagePanel(id, valueModel);
    }

}
