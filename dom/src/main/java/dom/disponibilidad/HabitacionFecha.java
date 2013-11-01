package dom.disponibilidad;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.query.QueryDefault;

import dom.reserva.Reserva;
import dom.tarifa.Tarifa;
import dom.tarifa.TarifaServicio;
import dom.enumeradores.TipoHabitacion;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name="habitacion_para_reservar", language="JDOQL",value="SELECT FROM dom.disponibilidad.HabitacionFecha WHERE paraReservar == true"),
	@javax.jdo.annotations.Query(name="habitacion_relleno", language="JDOQL",value="SELECT FROM dom.disponibilidad.HabitacionFecha WHERE paraReservar == false"),
	@javax.jdo.annotations.Query(name="traerOcupacion", language="JDOQL",value="SELECT FROM dom.disponibilidad.HabitacionFecha WHERE fecha >= :inicio && fecha <= :fin ORDER BY fecha ASC")
})
@ObjectType("HF")
@AutoComplete(repository=HabitacionFechaServicio.class,action="habitacionesReservadas")
@Audited
public class HabitacionFecha {
	
	public String iconName() {
		return "habitacion";
	}

	public String title() {
		return getNombreHabitacion();
	}
	
	@Named("Fecha")
	public String getFechaString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(getFecha());
	}
	
	private Date fecha;
	
	@Hidden
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
	
	//{{ Interno de la habitación para llamadas con la central asterisk
	private int interno;
	
	public int getInterno(){
		return interno;
	}
		
	public void setInterno(int interno) {
		this.interno = interno;
	}
	//}}
	
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
	
	@Named("Editar")
	@MemberOrder(name="pax",sequence="1")
	public HabitacionFecha personas(@Named("Cantidad de Personas") int personas) {
		setPax(personas);
		/*
		 * Actualizo la tarifa con respecto a las nuevas personas ingresadas
		 */
		setTarifa(tFS.tarifa(personas).getPrecio());
		return this;
	}
	
	public String validatePersonas(int personas){
		return mayorPaxPermitido(personas) ? null : "El número de personas es mayor al permitido";
	}
	
	private boolean mayorPaxPermitido(int personas) {
		if((getTipoHabitacion() == TipoHabitacion.Doble) && (personas > 2)) { return false;
		}
		if((getTipoHabitacion() == TipoHabitacion.Triple) && (personas > 3)) { return false;
		}
		if((getTipoHabitacion() == TipoHabitacion.Cuadruple) && (personas > 4)) { return false;
		}
		return true;
	}
	/*
	 * Se guarda la tarifa de la habitación en esa fecha para que no cambie 
	 * si se cambia el costo del pax en general
	 */
	
	private float tarifa;
	
	public float getTarifa() {
		return tarifa;
	}

	public void setTarifa(float tarifa) {
		this.tarifa = tarifa;
	}
	
	private Reserva reserva;
	
	@Named("Estado")
	@Disabled
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

	private TarifaServicio tFS;
	
	public void injectTarifaServicio(TarifaServicio tFS) {
		this.tFS = tFS;
	}
	
}
