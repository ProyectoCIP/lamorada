package app;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.filter.Filters;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociationFilters;
import org.apache.isis.core.metamodel.spec.feature.OneToOneAssociation;
import org.apache.isis.viewer.wicket.model.mementos.PropertyMemento;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.model.util.ObjectAssociations;
import org.apache.isis.viewer.wicket.model.util.ObjectSpecifications;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.isis.viewer.wicket.ui.util.EvenOrOddCssClassAppenderFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

public class ContactoVOPagePanel extends PanelAbstract<ScalarModel> {

	private static final String ID_MEMBER_GROUP = "memberGroup";
	
	 private static final String ID_MEMBER_GROUP_NAME = "memberGroupName";
	 private static final String ID_PROPERTIES = "properties";
	 private static final String ID_PROPERTY = "property";
	
	         
	private static final long serialVersionUID = 1L;
	
	public ContactoVOPagePanel(String id, ScalarModel scalarModel) {
		super(id, scalarModel);
		buildGui();
	}

	private void buildGui() {
		
		addPropertiesAndOrCollections();
		
		//final ValueModel contactoVOPagePanel = getModel();
		
		/*final Label direccion = new Label(ID_DIRECCION,"la direccion de la entidad");

		final Label telefono = new Label(ID_TELEFONO,"el telefono de la entidad");

		final Label correo = new Label(ID_CORREO,getModel().getObject().toString());
		
		add(direccion);
		add(telefono);
		add(correo);*/
		
	}

	private void addPropertiesAndOrCollections() {
		
		final ScalarModel scalarModel = (ScalarModel) getModel();
		final ObjectAdapter adapter = scalarModel.getObject();
		
		final ObjectSpecification objSpec = adapter.getSpecification();
		
		final List<ObjectAssociation> associations = visibleAssociations(adapter, objSpec, Where.OBJECT_FORMS);
		
		final RepeatingView memberGroupRv = new RepeatingView(ID_MEMBER_GROUP);
			
		add(memberGroupRv);
		
		Map<String, List<ObjectAssociation>> associationsByGroup = ObjectAssociations.groupByMemberOrderName(associations);
		final List<String> groupNames = ObjectSpecifications.orderByMemberGroups(objSpec, associationsByGroup.keySet());
		        
		for(String groupName: groupNames) {
			
			final List<ObjectAssociation> associationsInGroup = associationsByGroup.get(groupName);
		
		    final WebMarkupContainer memberGroupRvContainer = new WebMarkupContainer(memberGroupRv.newChildId());
		    memberGroupRv.add(memberGroupRvContainer);
		    memberGroupRvContainer.add(new Label(ID_MEMBER_GROUP_NAME, groupName));
		
		
		    final RepeatingView propertyRv = new RepeatingView(ID_PROPERTIES);
		    final EvenOrOddCssClassAppenderFactory eo = new EvenOrOddCssClassAppenderFactory();
		    memberGroupRvContainer.add(propertyRv);
		
		    @SuppressWarnings("unused")
		    Component component;
		    for (final ObjectAssociation association : associationsInGroup) {
		    	final WebMarkupContainer propertyRvContainer = new WebMarkupContainer(propertyRv.newChildId());
		        	propertyRv.add(propertyRvContainer);
		            propertyRvContainer.add(eo.nextClass());
		            addPropertyToForm(scalarModel, association, propertyRvContainer);
		        }
		    }
		}
	
	private void addPropertyToForm(final ScalarModel scalarModel,
			final ObjectAssociation association,
			final WebMarkupContainer container) {
			final OneToOneAssociation otoa = (OneToOneAssociation) association;
			final PropertyMemento pm = new PropertyMemento(otoa);
			
			final ScalarModel sM = scalarModel.getPropertyModel(pm);
			getComponentFactoryRegistry().addOrReplaceComponent(container, ID_PROPERTY, ComponentType.SCALAR_NAME_AND_VALUE, sM);
	}
	
	 private List<ObjectAssociation> visibleAssociations(final ObjectAdapter adapter, final ObjectSpecification objSpec, Where where) {
		 return objSpec.getAssociations(visibleAssociationFilter(adapter, where));
	 }
	
	 @SuppressWarnings("unchecked")
	 private Filter<ObjectAssociation> visibleAssociationFilter(final ObjectAdapter adapter, Where where) {
		 return Filters.and(ObjectAssociationFilters.PROPERTIES, ObjectAssociationFilters.dynamicallyVisible(getAuthenticationSession(), adapter, where));
	 }
	
    
    
    
    
    
}
