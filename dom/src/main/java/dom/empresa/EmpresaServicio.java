package dom.empresa;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.contacto.Contacto;
import dom.enumeradores.FormaPago;

@Named("Empresas")
public class EmpresaServicio extends AbstractFactoryAndRepository {
	
	public String iconName() {
		return "empresa";
	}
	
    @Named("Crear")
    @MemberOrder(sequence = "1")
    public Empresa nuevaEmpresa(
            @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*") // words, spaces and selected punctuation
            @Named("CUIT") String cuit, 
            @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*") // words, spaces and selected punctuation
            @Named("Razón Social") String razonSocial,
            @RegEx(validation = "[0-9]")
            @Named("Tarifa") float tarifa,            
            @Named("Forma de Pago") FormaPago fPago,
            @Named("Dirección") String direccion,
			@Optional
			@Named("Télefono") String telefono,
			@Optional
			@Named("Celular") String celular,
			@Optional
			@Named("E-mail") String mail
            /*,
            @Optional
            @Named("Dirección") String direccion,
            @Optional
            @Named("Télefono") String telefono,
            @Optional
            @Named("Email") String correo*/
            ) {
    	//final ContactoVO contacto = new ContactoVO(direccion,telefono,correo);
    	final String creadoPor = usuarioActual();
        return nEmpresa(cuit, razonSocial, tarifa, fPago, direccion, telefono, celular, mail, creadoPor);
    }
    
    @Hidden
    public Empresa nEmpresa(
            final String cuit, 
            final String razonSocial, 
            final float tarifa,
            final FormaPago fPago, 
            final String direccion,
            final String telefono,
            final String celular,
            final String mail,
            final String usuario) {
        final Empresa empresa = newTransientInstance(Empresa.class);
        empresa.setCuit(cuit);
        empresa.setRazonSocial(razonSocial);
        empresa.setTarifa(tarifa);
        empresa.setEstado(true);
        empresa.setFormaPago(fPago);
        empresa.setUsuario(usuario);
        
        Contacto contacto = newTransientInstance(Contacto.class);
		
		contacto.setDomicilio(direccion);
		contacto.setTelefono(telefono);
		contacto.setCelular(celular);
		contacto.setEmail(mail);
		
		persistIfNotAlready(contacto);
        
		empresa.setContacto(contacto);
        
        persistIfNotAlready(empresa);
        
        return empresa;
    }
    
    @Named("Listar")
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public List<Empresa> listaEmpresas() {
    	
    	/*
    	 * Repositorio: se listan las empresas creadas por el usuario logeado y las que estan dadas de alta
    	 */
    	
        final List<Empresa> listaEmpresas = allMatches(Empresa.class, 
        	new Filter<Empresa>() {
   			@Override
       			public boolean accept(final Empresa empresa) {
       				return Objects.equal(empresa.getUsuario(), usuarioActual())&&Objects.equal(empresa.isEstado(), true);
       			}
        	}
        );   
        return listaEmpresas;
    }    

    /*
     * Método para llenar el DropDownList de empresas, con la posibilidad de que te autocompleta las coincidencias al ir tipeando
     */
    @Hidden
    public List<Empresa> completaEmpresas(final String nombre) {
        return allMatches(Empresa.class, new Filter<Empresa>() {
        	@Override
            public boolean accept(final Empresa e) {
                return creadoPorActualUsuario(e) && e.getRazonSocial().contains(nombre) && e.isEstado();
            }
        });
    }
    
    protected boolean creadoPorActualUsuario(final Empresa e) {
        return Objects.equal(e.getUsuario(), usuarioActual());
    }
    protected String usuarioActual() {
        return getContainer().getUser().getName();
    }

	
	
}
