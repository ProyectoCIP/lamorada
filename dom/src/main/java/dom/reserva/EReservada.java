package dom.reserva;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.joda.time.LocalDate;

import dom.enumeradores.FormaPago;
import dom.huesped.Huesped;

public class EReservada extends AbstractFactoryAndRepository implements IEReserva {

	public String getIcon() {
		return "Reservada";
	}
	
	@Override
	public String getNombre() {
		return "Reservada";
	}
	
	@Hidden
	@Override
	public Reserva accion(Reserva reserva) {
		return reservar(reserva,null,null,0,null);
	}
	
	private Reserva reservar(
			Reserva reserva,
			@Named("Huesped") Huesped huesped,
			@Optional
			@Named("Tipo Seña") FormaPago tipoSena,
			@Optional
			@Named("Seña") float sena,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario
			) {
		
			reserva.setHuesped(huesped);
			reserva.setTipoSena(tipoSena);
			reserva.setMontoSena(sena);
			reserva.setFecha(LocalDate.now());
			reserva.setComentario(comentario);
			reserva.setUsuario(getContainer().getUser().getName());
			
			persistIfNotAlready(reserva);
			getContainer().informUser("Reserva realizada con éxito!");
			
			return reserva;
	}

}
