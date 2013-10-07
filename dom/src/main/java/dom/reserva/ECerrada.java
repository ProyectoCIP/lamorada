package dom.reserva;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.joda.time.LocalDate;


public class ECerrada extends AbstractFactoryAndRepository implements IEReserva {
	
	public String getIcon() {
		return "Cerrada";
	}

	@Override
	public String getNombre() {
		return "Cerrada";
	}

	@Override
	public Reserva accion(Reserva reserva) {
		
		return cerrar(reserva,"nulo",null);
		
	}
	
	private Reserva cerrar(
			Reserva reserva,
			@Optional
			@Named("Número de Factura") String numeroFactura,
			@Optional
			@Named("Fecha de Factura") LocalDate fechaFactura
			) {

			if(!numeroFactura.equals("nulo"))
				reserva.setNumeroFactura(numeroFactura);
			
			if(fechaFactura != null)
				reserva.setFechaFactura(fechaFactura);
			
			getContainer().informUser("Cierre realizado con éxito!");
			
			return reserva;
	}

}
