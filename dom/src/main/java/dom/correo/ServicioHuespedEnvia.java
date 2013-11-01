package dom.correo;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.joda.time.LocalDate;

@Named("Consulta")
public class ServicioHuespedEnvia extends AbstractFactoryAndRepository {

	@Named("Consultar") 
	public void consultar(
			@Named("Nombre") String nombre,
			@Named("Apellido") String apellido,
			@Optional
			@Named("Teléfono") String telefono,
			@Named("Correo") String correo,
			@Optional
			@Named("Desde") LocalDate desde,
			@Optional
			@Named("Hasta") LocalDate hasta,
			@MaxLength(255)
			@MultiLine(numberOfLines=4)
			@Named("Consulta") String mensaje) {

		Envio enviar = new Envio();
		enviar.setProperties();
		enviar.enviar(nombre,apellido,telefono,correo,desde,hasta,mensaje);
	
		
		
	}
		
}
