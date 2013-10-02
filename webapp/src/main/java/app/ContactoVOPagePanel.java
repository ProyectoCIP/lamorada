package app;

import org.apache.isis.viewer.wicket.model.models.ValueModel;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.wicket.markup.html.basic.Label;

public class ContactoVOPagePanel extends PanelAbstract<ValueModel> {

	private static final String ID_DIRECCION = "direccion";
	private static final String ID_TELEFONO = "telefono";
	private static final String ID_CORREO = "correo";
	
	private static final long serialVersionUID = 1L;
	
	public ContactoVOPagePanel(String id, ValueModel model) {
		super(id, model);
		buildGui();
	}

	private void buildGui() {
		
		//final ValueModel contactoVOPagePanel = getModel();
		
		final Label direccion = new Label(ID_DIRECCION,"la direccion de la entidad");

		final Label telefono = new Label(ID_TELEFONO,"el telefono de la entidad");

		final Label correo = new Label(ID_CORREO,getModel().getObject().toString());
		
		add(direccion);
		add(telefono);
		add(correo);
		
	}
}
