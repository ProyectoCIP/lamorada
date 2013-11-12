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
 * Esta clase permite conectar con la central PBX Asterisk y poder realizar llamdas a través de internet.
 * @author ProyectoCIP
 *
 */
public class Asterisk extends AbstractManagerEventListener
{
    private ManagerConnection managerConnection;

    /**
     * Realiza la conexión en el estado INITIAL con la IP, usuario y password de una cuenta existente en Asterisk
     * @throws IOException
     */
    public Asterisk() throws IOException
    {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
               "127.0.0.1", "admin", "1234");
        this.managerConnection = factory.createManagerConnection();
    }

    
    public void run() throws Exception{
        // register for events
        managerConnection.addEventListener(this);

        // connect to Asterisk and log in
        managerConnection.login();

        // request channel state
        managerConnection.sendAction(new StatusAction());
        listarUsuarios();    
        
        
        // wait 10 seconds for events to come in         
        Thread.sleep(600000);
        
        
        // and finally log off and disconnect
        managerConnection.logoff();
    }

    // Método para detectar el tipo de evento PeerStatusEvent
    // Salta cuando un usuario cambia su estado (conectado/desconectado)
    protected void handleEvent(PeerStatusEvent event){
        String usuario=event.getPeer()+": ";
        if(event.getPeerStatus().compareTo("Registered")==0)
            usuario=usuario+"Conectado a PBX La Morada.";
        if(event.getPeerStatus().compareTo("Unregistered")==0)
            usuario=usuario+"Desconectado de PBX La Morada.";
        System.out.println(usuario);
    }

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


    protected void handleEvent(PeerEntryEvent event){
        String tipoCanal=event.getChannelType();
        String usuario=event.getObjectName();
        String estado=event.getStatus();
        System.out.println(tipoCanal+"/"+usuario+": "+estado);
    }

    protected void handleEvent(PeerlistCompleteEvent event){
        Integer numUsuarios=event.getListItems();
        System.out.println("Usuarios mostrados: "+numUsuarios);
    }

    // Método que permite inicializar la lista de usuarios
    public void listarUsuarios() throws Exception{
        // Generamos la accion que devolverá eventos con los usuarios
        SipPeersAction accion=new SipPeersAction();
        managerConnection.sendAction(accion);
        
    }

    public void call(final String destino) throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
        OriginateAction originateAction;
        ManagerResponse originateResponse;
        managerConnection.login();
        
        originateAction = new OriginateAction();
        originateAction.setContext("internal");
        originateAction.setChannel("SIP/"+destino); 
        originateAction.setCallerId("Recepción");
        originateAction.setExten("80");
        originateAction.setPriority(Integer.valueOf(1)); 
        originateAction.setAsync(Boolean.TRUE); 
       
        // send the originate action and wait for a maximum of 30 seconds for Asterisk to send a reply
        originateResponse = managerConnection.sendAction(originateAction, 5000);
        
        // and finally log off and disconnect
        managerConnection.logoff();
    }   
}

