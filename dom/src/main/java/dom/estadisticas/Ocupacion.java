package dom.estadisticas;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.query.QueryDefault;

import dom.enumeradores.TipoHabitacion;
import dom.habitacion.Habitacion;

public class Ocupacion {
	
	private String año;

	@Hidden
	public String getAño() {
		return año;
	}

	public void setAño(String año) {
		this.año = año;
	}

	private String mes;
	
	@Title
	@Hidden
	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}
	
	private int pax;
	
	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}
	
	private String plazasTotales;
	
	@Named("Plazas")
	public String getPlazasTotales() {
		return Integer.toString(getPlazas()*plazasPorPax());
	}
	
	public void setPlazasTotales(String plazasTotales) {
		this.plazasTotales = plazasTotales;
	}
	
	private int plazas;	

	@Hidden
	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}
	
	private String porcentaje;
	
	public String getPorcentaje() {
		
		return Float.toString(((getPax()*100))/(getPlazas()*plazasPorPax()))+"%";
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	private int plazasPorPax() {
		
		int total = 0;
		
		List<Habitacion> lista = container.allMatches(QueryDefault.create(Habitacion.class,"traerHabitaciones"));
		
		for(Habitacion h : lista) {
			total += paxPlazas(h.getTipoHabitacion());
		}
		
		return total;
	
	}
	
	private int paxPlazas(TipoHabitacion tipo) {
		
		switch(tipo) {
			case Doble : return 2;
			case Triple : return 3;
			case Cuadruple : return 4;
		}

		return 0;
	}
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(DomainObjectContainer container) {
		this.container = container;
	}

}

