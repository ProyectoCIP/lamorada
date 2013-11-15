package dom.huesped;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.consumo.Consumo;
import dom.contacto.Contacto;
import dom.empresa.Empresa;
import dom.habitacion.Habitacion;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="traerPax", language="JDOQL",value="SELECT FROM dom.tarifa.Tarifa WHERE contacto_id == :pax")
@ObjectType("HUESPED")
@AutoComplete(repository=HuespedServicio.class, action="completaHuesped")
@Audited
public class Huesped {
	
	/**
	 * Muestra el ícono del hu&eacute;sped
	 * @return el nombre del ícon que se va a usar en el viewer.
	 */
	public String iconName() {
		return "huesped";
	}
	
	//{{Nombre
	private String nombre;
	
	/**
	 * Retorna el nombre del huesped creado.
	 * @return el nombre del huesped.
	 */
	@Title(sequence="1.0")
	@MemberOrder(sequence = "1")
	@RegEx(validation="[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Setea el nombre del huesped
	 * @param nombre
	 */
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
    //}}
	
	//{{Apellido
	private String apellido;
	
	/**
	 * Retorna el apellido del huesped.
	 * @return el apellido del huesped
	 */
	@Title(sequence="1.1")
	@MemberOrder(sequence = "2")
	@RegEx(validation="[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
	public String getApellido() {
		return apellido;
	}
	
	/**
	 * Setea el apellido del huesped.
	 * @param apellido
	 */
	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}
	//}}
	
	
	//{{Edad
	private String edad;
	
	/**
	 * Retorna la edad del huesped.
	 * @return la edad del huesped
	 */
	@MemberOrder(sequence = "3")
	@RegEx(validation="\\d{1,2}")
	public String getEdad() {
		return edad;
	}
	
	/**
	 * Setea la edad del huesped.
	 * @param edad
	 */
	public void setEdad(final String edad) {
		this.edad = edad;
	}
	//}}
	
	//{{dni
	private String dni;
	
	/**
	 * Retorna el n&uacute;mero de DNI del huesped.
	 * @return el DNI del huesped
	 */
	@MemberOrder(sequence = "4")
	@RegEx(validation="\\d{6,8}")
	public String getDni() {
		return dni;
	}
	
	/**
	 * Setea el DNI del huesped.
	 * @param dni
	 */
	public void setDni(final String dni) {
		this.dni = dni;
	}
	//}}
	
	
	//{{
	@Persistent(mappedBy="huesped")
	private Contacto contacto;
	
	/**
	 * Retorna los datos de contacto del huesped, como la direcci&oacute;n, mail, tel&eacute;fono y celular.
	 * @return
	 */
	public Contacto getContacto() {
		return contacto;
	}
	
	/**
	 * M&eacute;todo que setea los datos de contacto del huesped, como la direcci&oacute;n, mail, tel&eacute;fono y celular.
	 * @param contacto
	 */
	public void setContacto(final Contacto contacto) {
		this.contacto = contacto;
	}
	
	/**
	 * Metodo que crea los datos de contacto del huesped. Recibe como par&aacute;metros la direcci&oacute;n, el mail, tel&eacute;fono y celular.
	 * @param direccion
	 * @param telefono
	 * @param celular
	 * @param email
	 * @return
	 */
	@Named("Nuevo")
	@MemberOrder(name="contacto",sequence="1")
	public Huesped crearContacto(
			@RegEx(validation="[\\w\\s]+")
			@Named("Dirección") String direccion,
			@Optional
			@RegEx(validation="\\d{7,10}")
			@Named("Télefono") String telefono,
			@Optional
			@RegEx(validation="\\d{3,7}(-)?\\d{6}")
			@Named("Celular") String celular,
			@Optional
			@RegEx(validation="(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
			@Named("E-mail") String email) {
		
		Contacto contacto = container.newTransientInstance(Contacto.class);
		
		contacto.setDomicilio(direccion);
		contacto.setCelular(celular);
		contacto.setTelefono(telefono);
		contacto.setEmail(email);
		contacto.setUsuario(container.getUser().getName());
		
		container.persistIfNotAlready(contacto);
		
		setContacto(contacto);
		
		return this;
	}
	//}}
	
	//{{Estado
	private boolean estado;

	/**
	 * Retorna el estado en el sistema del huesped.
	 * @return el estado del huesped
	 */
	@Hidden
	public boolean isEstado() {
		return estado;
	}
	
	/**
	 * Setea el estado del huesped en el sistema.
	 * @param estado
	 */
	public void setEstado(final boolean estado) {
		this.estado = estado;
	}
	//}}
	
	/**
	 * Muestra todos los hu&eacute;spedes creados en el sistema.
	 * @return los hu&eacute;spedes
	 */
	@Named("Borrar")
	@Bulk
	public List<Huesped> borrar() {		
		container.removeIfNotAlready(this);
		return huespedServicio.listaHuespedes();
	}
	
	
	//{{Empresa
	private Empresa empresa;
		
	/**
	 * Retorna la empresa a la cual pertenece el huesped. Si el huesped no pertenece a ninguna empresa no muestra nada.
	 * @return
	 */
	@Optional
	@MemberOrder(sequence = "8")		
	public Empresa getEmpresa() {
		return empresa;
	}
		
	/**
	 * Setea la empresa a la cual pertenece el huesped en caso de ser necesario.
	 * @param empresa
	 */
	public void setEmpresa(final Empresa empresa) {
		this.empresa = empresa;
	}
	//}}
		
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
	    this.setContainer(container);
	}

	public DomainObjectContainer getContainer() {
		return container;
	}
	public void setContainer(DomainObjectContainer container) {
		this.container = container;
	}   
	
	private HuespedServicio huespedServicio;
	
	public void injectHuespedServicio(HuespedServicio huespedServicio) {
		this.huespedServicio = huespedServicio;
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
	
	public static Filter<Huesped> creadoPor(final String usuarioActual) {
        return new Filter<Huesped>() {
            @Override
            public boolean accept(final Huesped huesped) {
                return Objects.equal(huesped.getUsuario(), usuarioActual);
            }
        };
    }

}
