package dom.contacto;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;

import dom.empresa.Empresa;
import dom.huesped.Huesped;
import dom.huesped.HuespedServicio;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("CONTACTO")
@Audited
@Immutable
public class Contacto {
	
	public String iconName() {
		return "direccion";
	}
	
	public String title() {
		return getDomicilio();
	}
	
	//{{ Si esta relacionado con un Huesped retorna al mismo de lo contrario a la Empresa
	public Object volver() {
		return (getHuesped()==null) ? getEmpresa() : getHuesped();
	}
	//}}
	
	//{{Dirección
	private String domicilio;
	
	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	//}}
	
	//{{Teléfono
	private String telefono;
	
	@Optional	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	//}}
	
	//{{Celular
	private String celular;

	@Optional
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}	
	//}}
	
	//{{Correo
	private String email;

	@Optional
	public String getEmail() {
		return email;
	}
		
	public void setEmail(String email) {
		this.email = email;
	}
	//}}	
	
	private Huesped huesped;
	
	@Hidden
	public Huesped getHuesped() {
		return huesped;
	}
	
	public void setHuesped(Huesped huesped) {
		this.huesped = huesped;
	}

	@Hidden
	public void addToContacto(Huesped huesped) {
    	huesped.setContacto(this);
    	setHuesped(huesped);
	}
		
	private Empresa empresa;
	
	@Hidden
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Hidden
	public void addToEmpresa(Empresa empresa) {
    	empresa.setContacto(this);
    	setEmpresa(empresa);
	}
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(DomainObjectContainer container) {
		this.container = container;
	}
	
}