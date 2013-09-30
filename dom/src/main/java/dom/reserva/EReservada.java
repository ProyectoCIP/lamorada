package dom.reserva;

import org.apache.isis.applib.annotation.Named;

import dom.empresa.Empresa;
import dom.huesped.Huesped;

public class EReservada implements IEReserva {
	
	private Reserva reserva;

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	@Override
	public Reserva accion(Reserva reserva) {
		// TODO Auto-generated method stub
		reservar();
		
		return reserva;
		
	}
	
	private Reserva reservar(
			
			
			) {
	
	return reserva;
	
	}

}
