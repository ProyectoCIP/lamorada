package dom.asterisk;

import java.io.IOException;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.SipPeersAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeerStatusEvent;
import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.response.ManagerResponse;

/**
 * Esta clase permite conectar con la central PBX Asterisk y poder realizar llamdas a trav&eacute;s de internet.-
 * @author ProyectoCIP
 *
 */
public class Asterisk extends AbstractManagerEventListener
{
    private ManagerConnection managerConnection;

    /**
     * Realiza la conexión en el estado INITIAL con la IP, usuario y password de una cuenta existente en Asterisk.-
     * @throws IOException
     */
    public Asterisk() throws IOException
    {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
               "127.0.0.1", "admin", "1234");
        this.managerConnection = factory.createManagerConnection();
    }

    /**
     * M&eacute;todo que registra los eventos de las llamadas, se loguea en una cuenta creada en Asterisk, hace un listado de los usuarios conectados
     * espera 60 segundos para registrar eventos y se desloguea.-
     * @throws Exception
     */
    public void run() throws Exception{
        
        managerConnection.addEventListener(this);        
        managerConnection.login();
        managerConnection.sendAction(new StatusAction());
        listarUsuarios();         
        Thread.sleep(600000);       
        managerConnection.logoff();
    }    
    
    /**
     * M&eacute;todo que detecta tipos de eventos PeerStatusEvent y se activa cuando un usuario cambia su estado
     * de Conectado/Desconectado.-
     */
    protected void handleEvent(PeerStatusEvent event){
        String usuario=event.getPeer()+": ";
        if(event.getPeerStatus().compareTo("Registered")==0)
            usuario=usuario+"Conectado a PBX La Morada.";
        if(event.getPeerStatus().compareTo("Unregistered")==0)
            usuario=usuario+"Desconectado de PBX La Morada.";
        System.out.println(usuario);
    }

    /**
     * M&eacute;todo que detecta e informa en que estado est&aacute; la llamada de un usuario determinado.-
     */
    protected void handleEvent(ExtensionStatusEvent event){
        String usuario=event.getExten()+": ";
        Integer estado=event.getStatus();
        switch(estado.intValue()){
            case ExtensionStatusEvent.NOT_INUSE:
                usuario=usuario+"Conectado";
                break;
            case ExtensionStatusEvent.INUSE:
                usuario=usuario+"Hablando";
                break;
            case ExtensionStatusEvent.BUSY:
                usuario=usuario+"Ocupado";
                break;
            case ExtensionStatusEvent.UNAVAILABLE:
                usuario=usuario+"Desconectado";
                break;
            case ExtensionStatusEvent.RINGING:
                usuario=usuario+"Recibiendo llamada";
                break;
                default :System.out.print("No hay nada");
                break;
        
        }         
        System.out.println(usuario);
    }

    /**
     * M&eacute;todo que detecta eventos como el tipo de canal, el usuario y su estado.-
     */
    protected void handleEvent(PeerEntryEvent event){
        String tipoCanal=event.getChannelType();
        String usuario=event.getObjectName();
        String estado=event.getStatus();
        System.out.println(tipoCanal+"/"+usuario+": "+estado);
    }

    /**
     * M&eacute;todo que informa la cantidad de usuarios.
     */
    protected void handleEvent(PeerlistCompleteEvent event){
        Integer numUsuarios=event.getListItems();
        System.out.println("Usuarios mostrados: "+numUsuarios);
    }

    /**
     * M&eacute;todo que permite inicializar la lista de usuarios y los eventos de cada uno.-
     * @throws Exception
     */
    public void listarUsuarios() throws Exception{        
        SipPeersAction accion=new SipPeersAction();
        managerConnection.sendAction(accion);        
    }

    /**
     * M&eacute;todo que recibe por par&aacute;metro el n&uacute;mero de tel&eacute;fono de destino y realiza la llamada. Setea datos como el contexto, canal, extensión, etc.
     * Una vez que se realiza la llamda se espera 30 segundos para que el destinatario conteste y luego se desloguea.
     * @param destino
     * @throws IOException
     * @throws AuthenticationFailedException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public void call(final String destino) throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
        OriginateAction originateAction;
        @SuppressWarnings("unused")
		ManagerResponse originateResponse;
        managerConnection.login();
        
        originateAction = new OriginateAction();
        originateAction.setContext("internal");
        originateAction.setChannel("SIP/"+destino); 
        originateAction.setCallerId("Recepción");
        originateAction.setExten("81");
        originateAction.setPriority(Integer.valueOf(1)); 
        originateAction.setAsync(Boolean.TRUE);       
        
        originateResponse = managerConnection.sendAction(originateAction, 5000);        
        
        managerConnection.logoff();
    }   
}

