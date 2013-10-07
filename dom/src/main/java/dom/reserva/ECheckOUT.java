package dom.reserva;

public class ECheckOUT implements IEReserva {

	@Override
	public String getNombre() {
		return "CheckOUT";
	}

	@Override
	public Reserva accion(Reserva reserva) {
		
		return reserva;
		
	}

}
