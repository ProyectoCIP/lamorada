package dom.disponibilidad;

import java.util.Date;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.LocalDate;

import dom.habitacion.Habitacion;

public class Disponibilidad {

	private HabitacionFecha habitacion;
	
	@Hidden
	public HabitacionFecha getHabitacion() {
		return habitacion;
	}
	
	public void setHabitacion(HabitacionFecha habitacion) {
		this.habitacion = habitacion;
	}
	
	private boolean paraReservar;
	
	public boolean isParaReservar() {
		return paraReservar;
	}
	
	public void setParaReservar(boolean paraReservar) {
		this.paraReservar = paraReservar;
	}
	
	private Date fecha;
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date date) {
		this.fecha = date;
	}
	
	@Named("Seleccionar")
	@Bulk
	@MemberOrder(name="paraReservar",sequence="1")
	public Disponibilidad reservar() {
		
		setParaReservar(true);
		return this;
	}
	
	// {{ injected: DomainObjectContainer
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
	    this.container = container;
	}
}
