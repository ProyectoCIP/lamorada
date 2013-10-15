package dom.disponibilidad;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.joda.time.LocalDate;

import dom.reserva.HabitacionFecha;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("DISPONIBILIDAD")
@Audited
@javax.jdo.annotations.Query(name="traerLosQueSeReservan", language="JDOQL",value="SELECT FROM dom.disponibilidad.Disponibilidad WHERE paraReservar == true")
public class Disponibilidad {
	
	public String title() {
		return getHabitacion().getNombreHabitacion();
	}

    private HabitacionFecha habitacion;
    
    @Hidden
	public HabitacionFecha getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(HabitacionFecha hf) {
		this.habitacion = hf;
	}
	
	private boolean paraReservar;
	
	@Hidden
	public boolean isParaReservar() {
		return paraReservar;
	}

	public void setParaReservar(boolean paraReservar) {
		this.paraReservar = paraReservar;
	}
	
	private LocalDate fecha;

	public LocalDate getFecha() {
		return getHabitacion().getFecha();
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	@Bulk
	@Named("Reservar")
	public void reservar() {
		setParaReservar(true);
		container.persistIfNotAlready(this);
		disponibleServicio.listaHabitacionesReservas();
	}
    	
	//{{inyeccion DisponibilidadServicio
	private DisponibilidadServicio disponibleServicio;
	
	public void injectReservaServicio(final DisponibilidadServicio disponibleServicio) {
		this.disponibleServicio = disponibleServicio;
	}
	//}}
    
	// {{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
}
