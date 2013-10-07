package dom.reserva;

public class ECerrada implements IEReserva {

	@Override
	public String getNombre() {
		return "Cerrada";
	}

	@Override
	public Reserva accion(Reserva reserva) {
		
		return reserva;
		
	}

}
