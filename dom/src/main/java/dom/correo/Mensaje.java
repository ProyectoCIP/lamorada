package dom.correo;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

import dom.disponibilidad.Disponibilidad;
import dom.disponibilidad.HabitacionFechaServicio;

/**
 * 
 * Es el objeto que se muestra en el viewer con toda la información del correo electrónico
 * 
 * @see dom.disponibilidad.Disponibilidad
 * @see dom.disponibilidad.HabitacionFechaServicio
 * 
 * @author ProyectoCIP
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("MENSAJE")
@Audited
@Immutable
public class Mensaje implements Comparable<Mensaje> {
	
	private String nombre;
	private String apellido;
	private String telefono;
	private String correo;
	private LocalDate fechaActual;
	private String desde;
	private String hasta;
	private String mensaje;
	
	public String iconName() {
		return "email";
	}

	/**
	 * 
	 * Es la primer parte del título que toma el objeto en el viewer
	 * 
	 * @return El nombre del remitente
	 */
	@Title(sequence="1.0")
	@Optional
	@MemberOrder(sequence="1")
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Setea el nombre del remitente
	 * @param nombre 
	 */
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return  Retorna el apellido del remitente
	 */
	@Optional
	@MemberOrder(sequence="2")
	public String getApellido() {
		return apellido;
	}
	
	/**
	 * Setea el apellido del remitente
	 * @param apellido 
	 */
	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}
	
	/**
	 * 
	 * @return Retorna el télefono del remitente
	 */
	@MemberOrder(sequence="4")
	@Optional
	public String getTelefono() {
		return telefono;
	}
	
	/**
	 * Setea el télefono del remitente
	 * @param telefono
	 */
	public void setTelefono(final String telefono) {
		this.telefono = telefono;
	}

	/**
	 * 
	 * Es la segunda parte del título que toma el objeto en el viewer
	 * 
	 * @return El nombre del remitente
	 */	
	@Title(sequence="1.2")
	@Optional
	@MemberOrder(sequence="3")
	public String getCorreo() {
		return correo;
	}
	
	/**
	 * Setea el correo 
	 * @param correo
	 */
	public void setCorreo(final String correo) {
		this.correo = correo;
	}

	/**
	 * 
	 * @return Retorna la primer fecha consultada por el remitente
	 */
	@MemberOrder(sequence="6")
	public String getDesde() {
		return desde;
	}

	/**
	 * Setea la primer fecha consultada por el remitente
	 * @param partes
	 */
	public void setDesde(String partes) {
		this.desde = partes;
	}

	/**
	 * 
	 * @return Retorna la segunda fecha consultada por el remitente
	 */
	@Optional
	@MemberOrder(sequence="7")
	public String getHasta() {
		return hasta;
	}

	/**
	 * Setea la primer fecha consultada por el remitente
	 * @param hasta 
	 */
	public void setHasta(final String hasta) {
		this.hasta = hasta;
	}

	/**
	 * 
	 * @return Retorna el mensaje/consulta que hizo el remitente
	 */
	@MemberOrder(sequence="8")
	@Hidden(where=Where.ALL_TABLES)
	@MultiLine(numberOfLines=3)
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Setea el mensaje/consulta que hizo el remitente
	 * @param mensaje
	 */
	public void setMensaje(final String mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * 
	 * @return Retorna el mapa de disponibilidad correspondiente a la fecha que hizo el usuario
	 */
	@Named("Consultar")
	public List<Disponibilidad> disponibilidad() {
		String hasta = getHasta().equals("") ? null : getHasta();
		return servicio.porFechas(new LocalDate(getDesde()), new LocalDate(hasta));
	}
	
	/**
	 * La posibilidad de responder a la consulta/correo directamente desde el mismo
	 * @param mensaje El mensaje que escribe el recepcionista al usuario
	 * @return Retorna el mensaje original del remitente
	 */
	public Mensaje Responder(
			@MultiLine(numberOfLines=6)
			@Named("Mensaje") String mensaje) {

		Envio correo = new Envio();
		correo.setProperties();
		
		correo.enviar("La Morada Petit Hotel","","","",null,null,mensaje,getCorreo());
		
		container.informUser("Mensaje enviado!");
		return this;
	}
	
	private HabitacionFechaServicio servicio;
	
	public void injectServicio(HabitacionFechaServicio servicio) {
		this.servicio = servicio;
	}
	
	/**
	 * Permite borrar la consulta/correo
	 * @return Retorna la lista de mensajes/bandeja de entrada
	 */
	@Named("Borrar")
	@Bulk
	public List<Mensaje> borrar() {
		//Borramos el/los objeto/s seleccionado/s
		container.removeIfNotAlready(this);

		//Vuelvo a la bandeja de entrada
		return bde.listaMensajesPersistidos();
	}

	/**
	 * 
	 * @return Retorna la fecha del día en que llegó la consulta/correo
	 */
	@Named("Fecha del Mensaje")
	@MemberOrder(sequence="5")
	public LocalDate getFechaActual() {
		return fechaActual;
	}

	/**
	 * Setea la fecha del día en que llegó la consulta/correo
	 * @param fechaActual
	 */
	public void setFechaActual(final LocalDate fechaActual) {
		this.fechaActual = fechaActual;
	}
    
    // {{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
    //}}
    
    private ServicioBandejaDeEntrada bde;

    public void injectServicioBandejaDeEntrada(final ServicioBandejaDeEntrada bde) {
        this.bde = bde;
    }

    /**
     * Ordena los emails por fecha de ingreso
     */
	@Override
	public int compareTo(Mensaje mensaje) {
		// TODO Auto-generated method stub
		return this.fechaActual.compareTo(mensaje.getFechaActual());
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
			
	public static Filter<Mensaje> creadoPor(final String usuarioActual) {
	    return new Filter<Mensaje>() {
	        @Override
	        public boolean accept(final Mensaje mensaje) {
	            return Objects.equal(mensaje.getUsuario(), usuarioActual);
	        }
	    };
	}	
}
