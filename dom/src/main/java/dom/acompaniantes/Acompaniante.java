package dom.acompaniantes;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;

import dom.reserva.Reserva;
import dom.reserva.ReservaServicio;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="acompaniantes", language="JDOQL",value="SELECT FROM dom.acompaniante.Acompaniante")
@AutoComplete(repository=ReservaServicio.class,action="completaAcompaniantes")
@ObjectType("ACOMPANIANTE")
@Audited
public class Acompaniante {
	
	public String iconName() {
		return "acompaniantes";
	}	

	private String nombre;
	
	@Title(sequence="1.0")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
	
	private String apellido;

	@Title(sequence="1.1")
	public String getApellido() {
		return apellido;
	}

	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}	
	
	private int edad;
	
	public int getEdad() {
		return edad;
	}

	public void setEdad(final int edad) {
		this.edad = edad;
	}
	
	private String relacion;
	
	public String getRelacion() {
		return relacion;
	}

	public void setRelacion(final String relacion) {
		this.relacion = relacion;
	}
	
	private Reserva reserva;
	
	@Hidden(where=Where.ALL_TABLES)
	public Reserva getReserva() {
		return reserva;
	}
	public void setReserva(final Reserva reserva) {
		this.reserva = reserva;
	}
	
	// {{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
	
}
