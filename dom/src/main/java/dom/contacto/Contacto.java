package dom.contacto;

public class Contacto {
	
	public Contacto(String domicilio, String telefono, String email) {
		
		this.domicilio = domicilio;
		this.telefono = telefono;
		this.email = email;
		
	}

	private String domicilio;

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}	

	private String telefono;
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
