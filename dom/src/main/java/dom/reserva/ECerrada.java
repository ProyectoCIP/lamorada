package dom.reserva;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.joda.time.LocalDate;

import dom.enumeradores.FormaPago;


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
		
		return cerrar(reserva,FormaPago.Efectivo,0,"nulo",null);
	
	}
	
	
	private Reserva cerrar(
			Reserva reserva,
			@Named("Forma de Pago") FormaPago fP,
			@Optional
			@Named("Descuento") float descuento,
			@Optional
			@Named("Número de Factura") String numeroFactura,
			@Optional
			@Named("Fecha de Factura") LocalDate fechaFactura
			) {

			reserva.setNumeroFactura(numeroFactura);
			reserva.setFechaFactura(fechaFactura.toDate());
			reserva.setDescuento(descuento);
			reserva.setFormaDeCierre(fP);
			
			getContainer().informUser("Cierre realizado con éxito!");
			
			return reserva;
	}

}
