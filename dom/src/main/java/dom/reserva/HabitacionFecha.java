package dom.reserva;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.LocalDate;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("HF")
@AutoComplete(repository=ReservaServicio.class,action="habitacionesReservadas")
@Audited
public class HabitacionFecha {

	public String title() {
		return getNombreHabitacion();
	}
	
	private LocalDate fecha;
	
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(final LocalDate fecha) {
		this.fecha = fecha;
	}
	
	private String nombreHabitacion;
	
	@Hidden
	public String getNombreHabitacion() {
		return nombreHabitacion;
	}

	public void setNombreHabitacion(String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}
	
	private Reserva reserva;
	
	@Hidden(where=Where.ALL_TABLES)
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}
	 //}}
	
}
