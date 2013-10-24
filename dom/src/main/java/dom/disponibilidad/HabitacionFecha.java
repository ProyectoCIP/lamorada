package dom.disponibilidad;

import java.awt.Container;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import dom.enumeradores.TipoHabitacion;

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
	
	public void setFecha(Date fecha) {
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
	
	private TipoHabitacion tipoHabitacion;
	
	@Hidden(where=Where.OBJECT_FORMS)
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	
	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	
	private int pax;
	
	@Named("Personas")
	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}
	
	public String validatePax(int pax){
		return mayorPaxPermitido(pax) ? null : "El número de personas es mayor al permitido";
	}
	
	/*
	 * Se guarda la tarifa de la habitación en esa fecha para que no cambie 
	 * si se cambia el costo del pax en general
	 */
	
	private float tarifa;
	
	@Hidden(where=Where.OBJECT_FORMS)
	public float getTarifa() {
		return tarifa;
	}

	public void setTarifa(float tarifa) {
		this.tarifa = tarifa;
	}
	
	private boolean mayorPaxPermitido(int pax) {
		if((getTipoHabitacion() == TipoHabitacion.Doble) && (pax > 2)) {
			return false;
		}
		if((getTipoHabitacion() == TipoHabitacion.Triple) && (pax > 3)) {
			return false;
		}
		if((getTipoHabitacion() == TipoHabitacion.Cuadruple) && (pax > 4)) {
			return false;
		}
		return true;
	}
	
	private Reserva reserva;
	
	@Named("Estado")
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
	}
	//}}
	
}
