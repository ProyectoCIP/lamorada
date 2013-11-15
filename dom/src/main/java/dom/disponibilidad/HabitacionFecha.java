package dom.disponibilidad;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.reserva.Reserva;
import dom.tarifa.TarifaServicio;
import dom.asterisk.Asterisk;
import dom.enumeradores.EstadoHabitacion;
import dom.enumeradores.TipoHabitacion;

/**
 * 
 * Es el objeto que relaciona la Habitación con la fecha en la que se reserva/bloquea
 * 
 * @author ProyectoCIP
 *
 */
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
	
	/**
	 * 
	 * El ícono cambia dependiendo de si está Disponible, Bloqueada o Reservada
	 * 
	 * @return Retorna el nombre del ícono que va a ser usado en el viewer
	 */
	public String iconName() {
		if (getEstado() == EstadoHabitacion.BLOQUEADA) { 
			return "bloqueada";
		}
		else {
			return (getReserva() == null) ? "disponibilidad" : "candado";
		}
	}

	private EstadoHabitacion estadoHabitacion;
		
	/**
	 * 
	 * @return Retorna el estado de la habitación que puede ser Disponible o Bloqueada
	 */
	@Hidden(where=Where.ALL_TABLES)
	public EstadoHabitacion getEstado() {
		return estadoHabitacion;
	}
	public void setEstado(final EstadoHabitacion estadoHabitacion) {
		this.estadoHabitacion = estadoHabitacion;
	}
		

	/**
	 * 
	 * @return Es el título que toma el objeto en el viewer
	 */
	public String title() {
		return getNombreHabitacion();
	}
	
	/**
	 * 
	 * @return Retorna la fecha formateada que se muestra en el viewer de la forma "dd/MM/yyyy"
	 */
	@Named("Fecha")
	public String getFechaString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(getFecha());
	}
	
	private Date fecha;
	
	/**
	 * 
	 * @return Retorna la fecha de consulta por el usuario
	 */
	@Hidden
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}
	  
	private String nombreHabitacion;
	
	/**
	 * 
	 * @return Retorna el nombre de la habitación
	 */
	@Hidden
	public String getNombreHabitacion() {
		return nombreHabitacion;
	}

	public void setNombreHabitacion(final String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}
	
	//{{ Interno de la habitación para llamadas con la central asterisk
	private int interno;
	
	/**
	 * 
	 * @return Retorna el número Interno de la habitación que se registro en la central telefónica
	 */
	public int getInterno(){
		return interno;
	}
		
	public void setInterno(final int interno) {
		this.interno = interno;
	}
	
	/**
	 * 
	 * @return Retorna la habitación para volver al formulario objeto que dibuja el viewer
	 * @throws Exception
	 */
	@MemberOrder(name="interno",sequence="1")
	public HabitacionFecha llamar() throws Exception {
		Asterisk pbx = new Asterisk();
        pbx.call(Integer.toString(getInterno()));
        return this;
	}
	
	
	private TipoHabitacion tipoHabitacion;
	
	/**
	 * 
	 * @return Retorna el tipo de habitación (Doble, Triple, Cuadruple)
	 */
	@Hidden(where=Where.OBJECT_FORMS)
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	
	public void setTipoHabitacion(final TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	
	private int pax;
	
	/**
	 * 
	 * @return Retorna la cantidad de personas que ocupan la habitación
	 */
	@Named("Personas")
	public int getPax() {
		return pax;
	}

	public void setPax(final int pax) {
		this.pax = pax;
	}
	
	/**
	 * 
	 * Se ingresa el número de personas, busca en el repositorio
	 * la tarifa correspondiente a ese número y lo edita.
	 * 
	 * @param personas La nueva cantidad de personas que van a ocupar la habitación
	 * @return Retorna la habitación modificada con el nuevo número de pax
	 */
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
	
	/**
	 * 
	 * @param personas La nueva cantidad de personas que van a ocupar la habitación
	 * @return Si se cumple la condición retorna la cadena que se muestra en el viewer
	 */
	public String validatePersonas(final int personas){
		return mayorPaxPermitido(personas) ? null : "El número de personas es mayor al permitido";
	}
	
	/**
	 * 
	 * @param personas La nueva cantidad de personas que van a ocupar la habitación
	 * @return Si el número de personas excede el máximo permitido en esa habitación retorna false
	 */
	private boolean mayorPaxPermitido(final int personas) {
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
	 * si se cambia el costo del pax en un futuro
	 */
	
	private BigDecimal tarifa;
	
	/**
	 * 
	 * @return La tarifa que se cobra por reservar esta habitación
	 */
	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(final BigDecimal tarifa) {
		this.tarifa = tarifa;
	}
	
	private Reserva reserva;
	
	/**
	 * 
	 * @return Retorna el objeto reserva en el que esta habitación esta registrada
	 */
	@Named("Estado")
	@Disabled
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(final Reserva reserva) {
		this.reserva = reserva;
	}

	@SuppressWarnings("unused")
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
	}

	private TarifaServicio tFS;
	
	public void injectTarifaServicio(final TarifaServicio tFS) {
		this.tFS = tFS;
	}
	
	//{{Usuario actual
	private String usuario;

	@Hidden
	public String getUsuario() {
	    return usuario;
	}

	public void setUsuario(final String usuario) {
	    this.usuario = usuario;
	}//}}
		
	public static Filter<HabitacionFecha> creadoPor(final String usuarioActual) {
	    return new Filter<HabitacionFecha>() {
	        @Override
	        public boolean accept(final HabitacionFecha habitacionFecha) {
	            return Objects.equal(habitacionFecha.getUsuario(), usuarioActual);
	        }
	    };
	}	
}
