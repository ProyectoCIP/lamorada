package dom.correo;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.joda.time.LocalDate;

@Named("Consulta")
public class ServicioHuespedEnvia extends AbstractFactoryAndRepository {
	
	public String iconName() {
		return "email";
	}

	@Named("Consultar") 
	public void consultar(
			@Named("Nombre") final String nombre,
			@Named("Apellido") final String apellido,
			@Optional
			@Named("Teléfono") final String telefono,
			@Named("Correo") final String correo,
			@Named("Desde") final LocalDate desde,
			@Optional
			@Named("Hasta") final LocalDate hasta,
			@MaxLength(255)
			@MultiLine(numberOfLines=4)
			@Named("Consulta") final String mensaje) {

		Envio enviar = new Envio();
		enviar.setProperties();
		enviar.enviar(nombre,apellido,telefono,correo,desde,hasta,mensaje,"proyectocipifes@gmail.com");
	
		getContainer().informUser("GRACIAS POR ENVIARNOS SU CONSULTA, LE CONTESTAREMOS A LA BREVEDAD");
		
	}
		
}
