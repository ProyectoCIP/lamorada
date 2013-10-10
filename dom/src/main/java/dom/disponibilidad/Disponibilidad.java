package dom.disponibilidad;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberGroups;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.PublishedObject;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import dom.habitacion.Habitacion;
import dom.reserva.Reserva;
import dom.reserva.ReservaServicio;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
//@javax.jdo.annotations.Unique(name="ToDoItem_description_must_be_unique", members={"ownedBy","description"})
@ObjectType("DISPONIBILIDAD")
@Audited
@javax.jdo.annotations.Query(name="traerLosQueSeReservan", language="JDOQL",value="SELECT FROM dom.disponibilidad.Disponibilidad")
//@PublishedObject(ToDoItemChangedPayloadFactory.class)
//@AutoComplete(repository=.class, action="autoComplete")
//@Bookmarkable
public class Disponibilidad {

    private LocalDate fechaDesde;

    @Hidden(where=Where.ANYWHERE)
    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(final LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

/*    private LocalDate fechaHasta;

    @Hidden(where=Where.ANYWHERE)
    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(final LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
  */
    
    private Habitacion habitacion;
    
    @Hidden
	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}
	
	private boolean paraReservar;
	
	public boolean isParaReservar() {
		return paraReservar;
	}

	public void setParaReservar(boolean paraReservar) {
		this.paraReservar = paraReservar;
	}
	
	private LocalDate fecha;

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	@Bulk
	@Named("Reservar")
	public Disponibilidad reservar() {
		
		setParaReservar(true);
		container.persist(this);
		
		disponibleServicio.traerReserva();
		
		return this;
	}
    
	/*
    private Reserva reserva;    
    
    public Reserva getReserva() { return reserva; }    

    public void setReservas(Reserva reserva) {
    	this.reserva = reserva;
    }*/
	
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
