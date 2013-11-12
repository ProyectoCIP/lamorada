package dom.contacto;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.RegEx;

import dom.empresa.Empresa;
import dom.huesped.Huesped;
import dom.huesped.HuespedServicio;

/**
 * 
 * Son los datos que nos permiten tener las diferentes formas de contacto con el huesped y las empresas
 * 
 * 
 * @author ProyectoCIP
 * @see dom.huesped.Huesped
 * @see dom.huesped.HuespedServicio
 * @see dom.empresa.Empresa
 * @see dom.empresa.EmpresaServicio
 * 
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("CONTACTO")
@Audited
@Immutable
public class Contacto {
	
	/**
	 * 
	 * @return Retorno el nombre del ícono que va a ser usado en el viewer
	 */
	public String iconName() {
		return "direccion";
	}
	
	/**
	 * 
	 * @return Es el título que toma el objeto en el viewer
	 */
	public String title() {
		return getDomicilio();
	}
	
	/**
	 * 
	 * @return Si esta relacionado con un Huesped retorna al mismo de lo contrario a la Empresa
	 */
	public Object volver() {
		return (getHuesped()==null) ? getEmpresa() : getHuesped();
	}
	//}}
	
	//{{Dirección
	private String domicilio;
	
	/**
	 * 
	 * @return Retorna el domicilio
	 */
	
	@RegEx(validation="[\\w\\s]+")
	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(final String domicilio) {
		this.domicilio = domicilio;
	}
	//}}
	
	//{{Teléfono
	private String telefono;
	
	
	/**
	 * 
	 * @return Retorna el telefono
	 */
	
	@Optional	
	@RegEx(validation="\\d{7,10}")
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(final String telefono) {
		this.telefono = telefono;
	}
	//}}
	
	//{{Celular
	private String celular;

	/**
	 * 
	 * @return Retorna el celular 
	 */

	@Optional
	@RegEx(validation="\\d{3,7}(-)?\\d{6}")
	public String getCelular() {
		return celular;
	}

	public void setCelular(final String celular) {
		this.celular = celular;
	}	
	//}}
	
	//{{Correo
	private String email;
	
	/**
	 * 
	 * @return Retorna el email 
	 */

	@Optional
	@RegEx(validation="(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
	public String getEmail() {
		return email;
	}
		
	public void setEmail(final String email) {
		this.email = email;
	}
	//}}	
	
	private Huesped huesped;
	
	/**
	 * 
	 * @return El huesped al cual le pertenecen dichos datos
	 */
	
	@Hidden
	public Huesped getHuesped() {
		return huesped;
	}
	
	public void setHuesped(final Huesped huesped) {
		this.huesped = huesped;
	}
	
	/**
	 * 
	 * @param huesped Agrega el objeto contacto al huesped 
	 */
	@Hidden
	public void addToContacto(Huesped huesped) {
    	huesped.setContacto(this);
    	setHuesped(huesped);
	}
		
	private Empresa empresa;
	
	
	/**
	 * 
	 * @return La empresa a la cual le pertenecen dichos datos
	 */
	@Hidden
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(final Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * 
	 * @param empresa Agrega el objeto contacto a la empresa 
	 */
	@Hidden
	public void addToEmpresa(Empresa empresa) {
    	empresa.setContacto(this);
    	setEmpresa(empresa);
	}
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}
	
}