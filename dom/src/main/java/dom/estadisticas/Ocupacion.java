package dom.estadisticas;

import org.apache.isis.applib.annotation.Hidden;

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
	
	private int plazas;	

	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}
	
	private String porcentaje;
	
	public String getPorcentaje() {
		return Float.toString((getPlazas()*getPax()/100))+"%";
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

}

