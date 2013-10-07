package dom.reserva;

import org.apache.isis.applib.AbstractFactoryAndRepository;

public class ECheckIN extends AbstractFactoryAndRepository implements IEReserva {
	
	public String getIcon() {
		return "CheckIN";
	}
	
	@Override
	public String getNombre() {
		return "CheckIN";
	}

	@Override
	public Reserva accion(Reserva reserva) {
		
		return reserva;
		
	}

}
