package dom.reserva;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.annotation.Optional;
import org.joda.time.LocalDate;

import dom.consumo.Consumo;
import dom.huesped.Huesped;

public class ReservaServicio extends AbstractFactoryAndRepository {
	
	public void reserva(
			@Named("Huesped") Huesped huesped,
			@Optional
			@Named("Comentario") String comentario
			
			) {
		reservar(huesped,comentario);
	}
	
	private Reserva reservar(
			final Huesped huesped,
			final String comentario) {
			final Reserva reserva = newTransientInstance(Reserva.class);
			reserva.setHuesped(huesped);
			reserva.setFecha(LocalDate.now());
			reserva.setComentario(comentario);
			reserva.setUsuario(usuarioActual());
			
			persistIfNotAlready(reserva);
			getContainer().informUser("Reserva realizada con éxito!");
			
			return reserva;
	}
	
	@NotInServiceMenu
	public Consumo agregarConsumo(
			String descripcion,
			int cantidad,
			float precio
    		) {
		
		Consumo consumo = newTransientInstance(Consumo.class);
		consumo.setDescripcion(descripcion);
		consumo.setCantidad(cantidad);
		consumo.setPrecio(precio);
		persistIfNotAlready(consumo);
		return consumo;
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	

}
