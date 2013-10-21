package dom.disponibilidad;

import java.util.Date;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import dom.reserva.Reserva;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name="habitacion_para_reservar", language="JDOQL",value="SELECT FROM dom.disponibilidad.HabitacionFecha WHERE paraReservar == true"),
	@javax.jdo.annotations.Query(name="habitacion_relleno", language="JDOQL",value="SELECT FROM dom.disponibilidad.HabitacionFecha WHERE paraReservar == false")
})
@ObjectType("HF")
@AutoComplete(repository=HabitacionFechaServicio.class,action="habitacionesReservadas")
@Audited
public class HabitacionFecha {

	public String title() {
		return getNombreHabitacion();
	}
	
	private Date fecha;
	
	public Date getFecha() {
		return fecha;
	}
	
	private boolean paraReservar;

	@Named("Seleccionada")
	public boolean isParaReservar() {
		return paraReservar;
	}

	public void setParaReservar(boolean paraReservar) {
		this.paraReservar = paraReservar;
	}

	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}
	
	@Named("Seleccionar")
	@Bulk
    @MemberOrder(name="paraReservar", sequence = "1")
	@DescribedAs("Para reservar las habitaciones seleccionadas debe hacerlo desde el menu Disponibilidad -> Opcion:Reservar")
	@Hidden(where=Where.OBJECT_FORMS)
	public HabitacionFecha seleccionar() {
		
		if(getReserva() == null) {
			setParaReservar(true);
		}
				
		return this;
	}
	
	  public String disableSeleccionar() {
	        return paraReservar ? "Ya esta seleccionada!" : null;
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
	
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	public void injectHabitacionFechaServicio(final HabitacionFechaServicio servicio) {
	}
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
	}
	//}}
	
}
