package dom.abm;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optional;

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
	public void setIdHuesped(int idHuesped) {
		this.idHuesped = idHuesped;
	}
	//}}
	
	//{{Nombre
	private String nombre;
	
	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    //}}
	
	//{{Apellido
	private String apellido;
	
	@MemberOrder(sequence = "2")
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	//}}
	
	
	//{{Edad
	private int edad;
	@MemberOrder(sequence = "3")
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	//}}
	
	//{{dni
		private String dni;
		@MemberOrder(sequence = "4")
		public String getDni() {
			return dni;
		}
		public void setDni(String dni) {
			this.dni = dni;
		}
		//}}
	
	//{{Estado
		private boolean estado;
		@MemberOrder(sequence = "5")
		public boolean getEstado() {
			return estado;
		}
		public void setEstado(boolean estado) {
			this.estado = estado;
		}
		//}}
	
	//{{Dirección
	private String direccion;
	@MemberOrder(sequence = "6")
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	//}}
	
	//{{Empresa
		private Empresa empresa;
		
		@Optional
		@MemberOrder(sequence = "7")		
		public Empresa getEmpresa() {
			return empresa;
		}
		
		public void setEmpresa(Empresa empresa) {
			this.empresa = empresa;
		}
	//}}

}
