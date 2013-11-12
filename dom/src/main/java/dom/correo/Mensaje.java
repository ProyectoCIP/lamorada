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
import org.joda.time.LocalDate;

import dom.disponibilidad.Disponibilidad;
import dom.disponibilidad.HabitacionFechaServicio;

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

	@Title(sequence="1.0")
	@Optional
	@MemberOrder(sequence="1")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	@Optional
	@MemberOrder(sequence="2")
	public String getApellido() {
		return apellido;
	}
	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}
	
	@MemberOrder(sequence="4")
	@Optional
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(final String telefono) {
		this.telefono = telefono;
	}

	@Title(sequence="1.2")
	@Optional
	@MemberOrder(sequence="3")
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(final String correo) {
		this.correo = correo;
	}

	
	@MemberOrder(sequence="6")
	public String getDesde() {
		return desde;
	}

	public void setDesde(String partes) {
		this.desde = partes;
	}

	@MemberOrder(sequence="7")
	public String getHasta() {
		return hasta;
	}

	public void setHasta(final String hasta) {
		this.hasta = hasta;
	}

	@MemberOrder(sequence="8")
	@Hidden(where=Where.ALL_TABLES)
	@MultiLine(numberOfLines=3)
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(final String mensaje) {
		this.mensaje = mensaje;
	}
	
	@Named("Consultar")
	public List<Disponibilidad> disponibilidad() {
		String hasta = getHasta().equals("") ? null : getHasta();
		return servicio.porFechas(new LocalDate(getDesde()), new LocalDate(hasta));
	}
	
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
	
	@Named("Borrar")
	@Bulk
	public List<Mensaje> borrar() {
		//Borramos el/los objeto/s seleccionado/s
		container.removeIfNotAlready(this);

		//Vuelvo a la bandeja de entrada
		return bde.listaMensajesPersistidos();
	}

	@Named("Fecha del Mensaje")
	@MemberOrder(sequence="5")
	public LocalDate getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(final LocalDate fechaActual) {
		this.fechaActual = fechaActual;
	}
    /*
     * Usuario actual logeado
     */
    private String usuario;

    @Hidden
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(final String usuario) {
        this.usuario = usuario;
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

	@Override
	public int compareTo(Mensaje mensaje) {
		// TODO Auto-generated method stub
		return this.fechaActual.compareTo(mensaje.getFechaActual());
	}
    
}
