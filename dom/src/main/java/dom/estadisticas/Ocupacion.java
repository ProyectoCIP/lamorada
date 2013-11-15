package dom.estadisticas;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.query.QueryDefault;

import dom.enumeradores.TipoHabitacion;
import dom.habitacion.Habitacion;

/**
 * 
 * La f&oacute;rmula para calcular el porcentaje de ocupaci&oacute;n en los hoteles está determinada
 * por una regla de tres simples: 
 * 
 * [(La cantidad de personas que estan persistidas*100)/(La cantidad de dias del mes*La cantidad de personas que ocupan el hotel por dia)]
 * 
 * Los objetos ocupaci&oacute;n muestran el porcentaje de ocupaci&oacute;n de los doce meses
 * en un año que selecciona el usuario.
 * 
 * @see dom.enumeradores.TipoHabitacion
 * @see dom.habitacion.Habitacion
 * @author ProyectoCIP
 *
 */
public class Ocupacion {
	
	/**
	 * 
	 * @return Retorna el nombre del ícono que va a ser usado en el viewer
	 */
	public String iconName() {
		return "estadisticas";
	}
	
	private String año;

	/**
	 * 
	 * @return Retorna el a&ntilde;o del que se quiere saber los porcentajes de ocupaci&oacute;n
	 */
	@Hidden
	public String getAño() {
		return año;
	}

	/**
	 * Setea el año del que se quiere saber los porcentajes de ocupaci&oacute;n
	 * @param año 
	 */
	public void setAño(final String año) {
		this.año = año;
	}

	private String mes;
	
	/**
	 * 
	 * @return Es el título que toma el objeto en el viewer
	 */
	@Title
	@Hidden
	public String getMes() {
		return mes;
	}

	/**
	 * Setea el mes
	 * @param mes
	 */
	public void setMes(final String mes) {
		this.mes = mes;
	}
	
	private int pax;
	
	/**
	 * 
	 * @return Retorna la cantidad total de personas que ocuparon el hotel en ese mes
	 */
	public int getPax() {
		return pax;
	}

	/**
	 * Setea la cantidad total de personas que ocuparon el hotel en ese mes
	 * @param pax
	 */
	public void setPax(final int pax) {
		this.pax = pax;
	}
	
	@SuppressWarnings("unused")
	private String plazasTotales;
	
	/**
	 * Se ve en el viewer sobre la columna "Plazas"
	 * @return Retorna la cantidad personas que pueden ocupar el hotel dependiendo de la cantidad de personas que se pueden alojar como máximo en cada habitaci&oacute;n
	 */
	@Named("Plazas")
	public String getPlazasTotales() {
		return Integer.toString(getPlazas()*plazasPorPax());
	}
	
	/**
	 * Setea la cantidad total de personas que pueden ocupar el hotel dependiendo de la cantidad de personas que se pueden alojar como máximo en cada habitaci&oacute;n
	 * @param plazasTotales 
	 */
	public void setPlazasTotales(final String plazasTotales) {
		this.plazasTotales = plazasTotales;
	}
	
	private int plazas;	

	/**
	 * 
	 * @return Retorna la cantidad de dias que tiene el mes
	 */
	@Hidden
	public int getPlazas() {
		return plazas;
	}

	/**
	 * Setea la cantidad de días que tiene el mes
	 * @param plazas
	 */
	public void setPlazas(final int plazas) {
		this.plazas = plazas;
	}
	
	@SuppressWarnings("unused")
	private String porcentaje;
	
	/**
	 * 
	 * @return Retorna el porcentaje de ocupaci&oacute;n que tiene este mes
	 */
	public String getPorcentaje() {
		
		return Float.toString(((getPax()*100))/(getPlazas()*plazasPorPax()))+"%";
	}

	/**
	 * Setea el porcentaje de ocupaci&oacute;n que tiene este mes
	 * @param porcentaje
	 */
	public void setPorcentaje(final String porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	/**
	 * 
	 * @return Retorna el total de personas que pueden ocupar el hotel en un día
	 */
	private int plazasPorPax() {
		
		int total = 0;
		
		List<Habitacion> lista = container.allMatches(QueryDefault.create(Habitacion.class,"traerHabitaciones"));
		
		for(Habitacion h : lista) {
			total += paxPlazas(h.getTipoHabitacion());
		}
		
		return total;
	
	}
	
	/**
	 * 
	 * @param tipo El tipo de habitaci&oacute;n (Doble, Triple, Cuadruple)
	 * @return Retorna el n&uacute;mero de personas dependiendo del tipo
	 */
	private int paxPlazas(final TipoHabitacion tipo) {
		
		switch(tipo) {
			case Doble : return 2;
			case Triple : return 3;
			case Cuadruple : return 4;
		}

		return 0;
	}
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}

}

