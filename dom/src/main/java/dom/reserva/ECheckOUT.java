package dom.reserva;

public class ECheckOUT implements IEReserva {
	
	public String getIcon() {
		return "CheckOut";
	}

	@Override
	public String getNombre() {
		return "CheckOUT";
	}

	@Override
	public Reserva accion(Reserva reserva) {
		
		return reserva;
		
	}

}
