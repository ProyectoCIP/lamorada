package dom.contacto;

import java.io.Serializable;

import org.apache.isis.applib.annotation.Value;

@Value(semanticsProviderName = "ContactValueSemanticsProvider")
public final class ContactoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 981967545177159614L;

	public ContactoVO(String direccion, String telefono, String correo) {
		this.domicilio = direccion;
		this.email = correo;
		this.telefono = telefono;
	}
	
	private String domicilio;


	public String getDomicilio() {
		return domicilio;
	}

	/*
	 * public void setDomicilio(String domicilio) {
	 *
		this.domicilio = domicilio;
	}
	*/	

	private String telefono;
	
	public String getTelefono() {
		return telefono;
	}
	
	/*
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}*/
	
	private String email;

	public String getEmail() {
		return email;
	}

	public String toString() {
		return getDomicilio()+"-"+getEmail()+"-"+getTelefono();
	}
	
	/*
	public void setEmail(String email) {
		this.email = email;
	}*/
	
}