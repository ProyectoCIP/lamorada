package dom.tarifa;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;

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
	
	public String iconName() {
		return "tarifa";
	}
	
	public String title() {
		return Integer.toString(getPax());
	}
	
	private int pax;
	
	@Hidden(where=Where.ALL_TABLES)
	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}
	
	public String validatePax(int pax) {
        return (pax>4)? "Cuadruple es lo máximo":null;
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
