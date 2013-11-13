package dom.mensajeria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SMS {
	private String mail;
	private String pass;
	private String celular;
	private String remitente;
	private String prefijoPais;
	private String texto;
	private String url;	
	
	/**
	 * Retorna la URL completa por donde se envía el SMS.
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Setea la URL con los datos necesarios para poder enviar el SMS
	 * @param url
	 */	 
	public void setUrl(final String url) {
		this.url = url;
	}
	
	/**
	 * Retorna el email de la cuenta en el proveedor del servicio SMS.
	 * @return
	 */
	public String getMail() {
		return mail;
	}
	
	/**
	 * Setea el email que sirve para poder ingresar a la cuenta en el proveedor del servicio SMS.-.
	 * @param mail
	 */
	public void setMail(final String mail) {
		this.mail = "email=" + mail;
	}
	
	/**
	 * Retorna la clave de la cuenta abierta en el servicio SMS.
	 * @return
	 */
	public String getPass() {
		return pass;
	}
	
	/**
	 * Setea la clave que se necesita para ingresar a la cuenta del servicio SMS.	 * 
	 * @param pass
	 */
	public void setPass(final String pass) {
		this.pass = "&clave=" + pass;
	}
	
	/**
	 * Retorna el celular de destino. Ej.: 29957556677 (Celular de Neuquén)
	 * @return
	 */
	public String getCelular() {
		return celular;
	}
	
	/**
	 * Setea el celular de destino. Ej.: 29957556677 (Celular de Neuquén)
	 * @param celular
	 */
	public void setCelular(final String celular) {
		this.celular = "&movil=" + celular;
	}
	
	/**
	 * Retorna el remitente del SMS.
	 * @return
	 */
	public String getRemitente() {
		return remitente;
	}
	
	/**
	 * Setea el remitente del SMS.
	 * @param remitente
	 */
	public void setRemitente(final String remitente) {
		this.remitente = "&remite=" +  remitente;
	}
	
	/**
	 * Retorna el prefijo del país donde se enviará el SMS. Ej.:  0054 (Argentina)
	 * @return
	 */
	public String getPrefijoPais() {
		return prefijoPais;
	}
	
	/**
	 * Setea el prefijo del país donde se enviará el SMS. Ej.:  0054 (Argentina)
	 * @param prefijoPais
	 */
	public void setPrefijoPais(final String prefijoPais) {
		this.prefijoPais = "&pais=" + prefijoPais;
	}
	
	/**
	 * Retorna el texto del mensaje a enviar.
	 * @return
	 */
	public String getTexto() {
		return texto;
	}
	
	/**
	 * Setea el texto del mensaje a enviar.
	 * @param texto
	 */
	public void setTexto(final String texto) {
		this.texto = "&sms=" + texto;
	}	
	
	//Método para probar el envío de SMS a celulares. Hay que sacarlo cuando se implemente en la reserva.
	
	/**
	 * Método que arma la URL completa con todos los datos de la cuenta del proveedor del servicio, los datos del destinatario y el remitente. Abre una conexión HTTP y envía 
	 * el SMS al destinatario.
	 * @param nombreHuesped
	 * @param celular
	 * @param numReserva
	 */
	public void enviarSMS(final String nombreHuesped,final String celular, final String numReserva)
	   {
	      try
	      {
	    	  SMS sms = new SMS();	    	  
	    	  
	    	  sms.setMail("luchitoparada@hotmail.com");
	    	  sms.setPass("Cami%juli76");	    	
	    	  //sms.setCelular("2995741694");
	    	  sms.setCelular(celular);
	    	  sms.setRemitente("La%20Morada");
	    	  sms.setPrefijoPais("0054");
	    	  sms.setTexto("Estimado:%20" + nombreHuesped + "%20La%20Reserva%20nro:%20" + numReserva + "%20fue%20hecha%20correctamente");
	    	  	    	  
	    	  sms.setUrl("http://www.afilnet.com/http/sms/?" + sms.getMail() + sms.getPass() + sms.getCelular() + sms.getRemitente() + sms.getPrefijoPais() + sms.getTexto() );
	    	  
	    	  URL url = new URL(sms.getUrl());
	          URLConnection urlConnection = url.openConnection();
	          HttpURLConnection connection = null;
	          if(urlConnection instanceof HttpURLConnection)
	          {
	             connection = (HttpURLConnection) urlConnection;
	          }
	          else
	          {
	             System.out.println("Ingrese la url");
	             return;
	          }
	          BufferedReader in = new BufferedReader(
	          new InputStreamReader(connection.getInputStream()));
	          String urlString = "";
	          String current;
	          while((current = in.readLine()) != null)
	          {
	             urlString += current;
	          }
	          System.out.println("URL String" + urlString);
	       }catch(IOException e)
	       {
	          e.printStackTrace();
	       }
	   }
}
