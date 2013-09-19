package dom.huesped;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.empresa.Empresa;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class Huesped {
	
	//{{idHuesped
	private int idHuesped;
				
	@Hidden
	public int getIdHuesped() {
		return idHuesped;
	}
	public void setIdHuesped(final int idHuesped) {
		this.idHuesped = idHuesped;
	}
	//}}
	
	//{{Nombre
	private String nombre;
	
	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
    //}}
	
	//{{Apellido
	private String apellido;
	
	@MemberOrder(sequence = "2")
	public String getApellido() {
		return apellido;
	}
	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}
	//}}
	
	
	//{{Edad
	private int edad;
	@MemberOrder(sequence = "3")
	public int getEdad() {
		return edad;
	}
	public void setEdad(final int edad) {
		this.edad = edad;
	}
	//}}
	
	//{{dni
	private String dni;
	
	@MemberOrder(sequence = "4")
	public String getDni() {
		return dni;
	}
	public void setDni(final String dni) {
		this.dni = dni;
	}
	//}}
	
	//{{Estado
	private boolean estado;

	@Hidden
	public boolean getEstado() {
		return estado;
	}
	public void setEstado(final boolean estado) {
		this.estado = estado;
	}
	//}}
	
	//{{Dirección
	private String direccion;
	
	@MemberOrder(sequence = "5")
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(final String direccion) {
		this.direccion = direccion;
	}
	//}}
	
	//{{Empresa
	private Empresa empresa;
		
	@Optional
	@MemberOrder(sequence = "6")		
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