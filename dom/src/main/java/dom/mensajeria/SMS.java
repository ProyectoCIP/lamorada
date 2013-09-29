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
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = "email=" + mail;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = "&clave=" + pass;
	}
	
	public String getCelular() {
		return celular;
	}
	
	public void setCelular(String celular) {
		this.celular = "&movil=" + celular;
	}
	
	public String getRemitente() {
		return remitente;
	}
	
	public void setRemitente(String remitente) {
		this.remitente = "&remite=" +  remitente;
	}
	
	public String getPrefijoPais() {
		return prefijoPais;
	}
	
	public void setPrefijoPais(String prefijoPais) {
		this.prefijoPais = "&pais=" + prefijoPais;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = "&sms=" + texto;
	}	
	
	//Método para probar el envío de SMS a celulares. Hay que sacarlo cuando se implemente en la reserva.
	public static void main(String [] args)
	   {
	      try
	      {
	    	  SMS sms = new SMS();	    	  
	    	  
	    	  sms.setMail("Mail de la cuenta en afilnet");
	    	  sms.setPass("Clave de la cuenta");
	    	  sms.setCelular("Celular destino");
	    	  sms.setRemitente("La%20Morada");
	    	  sms.setPrefijoPais("0054");
	    	  sms.setTexto("El mensaje a enviar");
	    	  	    	  
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
	          System.out.println(urlString);
	       }catch(IOException e)
	       {
	          e.printStackTrace();
	       }
	   }
}
