package dom.disponibilidad;

import java.text.SimpleDateFormat;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.value.Date;
import org.joda.time.LocalDate;

import dom.reserva.HabitacionFecha;
import dom.reserva.Reserva;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("DISPONIBILIDAD")
@Audited
@javax.jdo.annotations.Query(name="traerLosQueSeReservan", language="JDOQL",value="SELECT FROM dom.disponibilidad.Disponibilidad WHERE paraReservar == true")
public class Disponibilidad {
	
	public String title() {
		return getHabitacionFecha().getNombreHabitacion();
	}	
	
    private HabitacionFecha habitacionFecha;
    
    @Hidden
	public HabitacionFecha getHabitacionFecha() {
		return habitacionFecha;
	}

	public void setHabitacionFecha(final HabitacionFecha hf) {
		this.habitacionFecha = hf;
	}
	
	private Reserva reserva;
	
	public Reserva getReserva() {
		return reserva;
	}
	
	public void setReserva(final Reserva reserva) {
		this.reserva = reserva;
	}
	
	private boolean paraReservar;
	
	@Hidden
	public boolean isParaReservar() {
		return paraReservar;
	}

	public void setParaReservar(boolean paraReservar) {
		this.paraReservar = paraReservar;
	}
	
	private SimpleDateFormat sF = new SimpleDateFormat("dd/MM/yyyy");
	
	public String getFecha() {
		return sF.format(getFechaReal());
	}
	
	private java.util.Date fechaReal;

	@Hidden
	public java.util.Date getFechaReal() {
		return fechaReal;
	}

	public void setFechaReal(final java.util.Date date) {
		this.fechaReal = date;
	}
	
	@Bulk
	@Named("Reservar")
	public void seleccionar() {
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
