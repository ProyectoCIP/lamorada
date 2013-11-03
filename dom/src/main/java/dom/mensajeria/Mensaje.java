package dom.mensajeria;

public class Mensaje {
	private String mail;
	private String pass;
	private String celular;
	private String remitente;
	private String prefijoPais;
	private String texto;
	private String url;
	
	
	
	public void setUrl(String url) {
		this.url = "http://www.afilnet.com/http/sms/?" + getMail() + getPass() + getCelular() + getRemitente() + getPrefijoPais() + getTexto() ;
	}	
	
	public String getPass() {
		return pass;
	}
	
	public String getMail() {
		return mail;
	}
	
	public String getCelular() {
		return celular;
	}
	
	public String getRemitente() {
		return remitente;
	}
	
	public String getPrefijoPais() {
		return prefijoPais;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setMail(String mail) {
		this.mail = "email=luisparadawagner@gmail.com" + mail;
	}	
	
	public void setPass(String pass) {
		this.pass = "&clave=Cami&juli76" + pass;
	}
	
	public void setCelular(String celular) {
		this.celular = "&movil=2995741694";
	}	
	
	public void setRemitente(String remitente) {
		this.remitente = "&remite=La%20Morada";
	}	
	
	public void setPrefijoPais(String prefijoPais) {
		this.prefijoPais = "&pais=0054";
	}	
	
	public void setTexto(String texto) {
		this.texto = "&sms=Su Reserva fue hecha con éxito";
	}	
	
}
