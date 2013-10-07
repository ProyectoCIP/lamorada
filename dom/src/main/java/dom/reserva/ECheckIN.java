package dom.reserva;

public class ECheckIN  implements IEReserva {
	
	public String getNombre() {
		return "CheckIN";
	}

	@Override
	public Reserva accion(Reserva reserva) {
		
		return reserva;
		
	}

}
