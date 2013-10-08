package dom.tarifa;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.ObjectType;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name="traerPax", language="JDOQL",value="SELECT FROM dom.tarifa.Tarifa WHERE pax == :pax"),
	@javax.jdo.annotations.Query(name="traerTodosPax", language="JDOQL",value="SELECT FROM dom.tarifa.Tarifa")
})
@ObjectType("TARIFA")
@Audited
public class Tarifa {
	
	private int pax;

	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}

	private float precio;

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	private DomainObjectContainer container;
	
	public void setContainer(final DomainObjectContainer container) {
		this.container = container;
	}   
	
	//{{inyeccion TarifaServicio
	private TarifaServicio tarifaServicio;
	
	public void injectReservaServicio(final TarifaServicio tarifaServicio) {
		this.tarifaServicio = tarifaServicio;
	}
	//}}
	
}
