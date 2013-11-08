package dom.huesped;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
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
	
	public String iconName() {
		return "huesped";
	}
	
	//{{Nombre
	private String nombre;
	
	@Title(sequence="1.0")
	@MemberOrder(sequence = "1")
	@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
    //}}
	
	//{{Apellido
	private String apellido;
	
	@Title(sequence="1.1")
	@MemberOrder(sequence = "2")
	@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}
	//}}
	
	
	//{{Edad
	private String edad;
	
	@MemberOrder(sequence = "3")
	@RegEx(validation="\\d{1,2}")
	public String getEdad() {
		return edad;
	}
	public void setEdad(final String edad) {
		this.edad = edad;
	}
	//}}
	
	//{{dni
	private String dni;
	
	@MemberOrder(sequence = "4")
	@RegEx(validation="\\d{6,8}")
	public String getDni() {
		return dni;
	}
	public void setDni(final String dni) {
		this.dni = dni;
	}
	//}}
	
	
	//{{
	@Persistent(mappedBy="huesped")
	private Contacto contacto;
	
	public Contacto getContacto() {
		return contacto;
	}
	
	public void setContacto(final Contacto contacto) {
		this.contacto = contacto;
	}
	
	
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
		
		container.persistIfNotAlready(contacto);
		
		setContacto(contacto);
		
		return this;
	}
	//}}
	
	//{{Estado
	private boolean estado;

	@Hidden
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(final boolean estado) {
		this.estado = estado;
	}
	//}}	
	
	//{{Empresa
	private Empresa empresa;
		
	@Optional
	@MemberOrder(sequence = "8")		
	public Empresa getEmpresa() {
		return empresa;
	}
		
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
