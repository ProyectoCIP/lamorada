package dom.disponibilidad;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.annotation.Named;

import dom.reserva.HabitacionFecha;

public class HabitacionesSeleccionadas {
	
	//{
	private List<HabitacionFecha> listaHF = new ArrayList<HabitacionFecha>();
	
	public List<HabitacionFecha> getListaHF() {
		return listaHF;
	}
	
	public void setListaHF(final List<HabitacionFecha> listaHF) {
		this.listaHF = listaHF;
	}
	//}}
	
	@Named("Reservar")
	public void reservar() {
		
	}
	
	public String disableReservar() {
		return "Imposible reservar en este momento";
	}

}
