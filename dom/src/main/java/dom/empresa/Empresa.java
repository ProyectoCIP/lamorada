package dom.empresa;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.jdo.annotations.Persistent;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import dom.huesped.Huesped;
import dom.contacto.Contacto;
import dom.enumeradores.FormaPago;

/**
 * Crea una nueva empresa la cual puede ser relacionada a uno o varios hu&eacute;spedes.
 * @author ProyectoCIP
 * @see dom.huesped.Huesped
 * @see dom.contacto.Contacto
 * @see dom.enumeradores.FormaPago
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("EMPRESA")
@AutoComplete(repository=EmpresaServicio.class, action="completaEmpresas")
@Audited
public class Empresa {
		
	/**
	 * Retorna el nombre del &iacute;cono que va a ser usado en el viewer
	 * @return
	 */	
	public String iconName() {
		return "empresa";
	}
	
	/*
	 * Razon Social de la empresa
	 */
	private String razonSocial;

	/**
	 * Retorna el nombre o Raz&oacute;n Social de la empresa creada.
	 * @return
	 */
	@Title
	//Letras, números y espacios
	@RegEx(validation="[\\w\\s]+")
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * Setea el nombre o Raz&oacute;n Social de la empresa.
	 * @param razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/*
	 * Cuit de la empresa
	 */
	private String cuit;
	//2 dígitos, un guión, 8 dígitos, otro guión y puede ser 1 o 2 dígitos
	/**
	 * Retorna el CUIT de la empresa.
	 * @return
	 */
	@RegEx(validation="\\d{2}-\\d{8}-\\d{1,2}")
	public String getCuit() {
		return cuit;
	}

	/**
	 * Setea el CUIT de la empresa.
	 * @param cuit
	 */
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	
	private BigDecimal tarifa;	

	/**
	 *Retorna la Tarifa especial por convenio. 
	 * @return
	 */
	@RegEx(validation="\\d+\\.\\d{2}")
	public BigDecimal getTarifa() {
		return tarifa;
	}

	/**
	 * Setea la torifa especial por convenio.
	 * @param tarifa
	 */
	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
	}
		
	/*
	 * Datos de las distintas maneras
	 * de contactarnos con la empresa.
	 * Contacto -> Value Object
	 */	
	
	//{{
	@Persistent(mappedBy="empresa")
	private Contacto contacto;
	
	/**
	 * Retorna las distintas formas de contactarnos con la empresa.
	 * @return
	 */
	public Contacto getContacto() {
		return contacto;
	}
	
	/**
	 * Setea los datos para poder contactarnos con la empresa.
	 * @param contacto
	 */
	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
	/**
	 * Crea y asocia a la empresa todos los datos para poder contactarla.
	 * @param direccion
	 * @param celular
	 * @param telefono
	 * @param email
	 * @return
	 */
	@Named("Nuevo")
	@MemberOrder(name="contacto",sequence="1")
	public Empresa crearContacto(
			@RegEx(validation="[\\w\\s]+")
			@Named("Dirección") String direccion,
			@RegEx(validation="\\d{3,7}(-)?\\d{6}")
			@Named("Celular") String celular,
			@RegEx(validation="\\d{7,10}")
			@Named("Teléfono") String telefono,
			@RegEx(validation="(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
			@Named("Email") String email) {
		
		Contacto contacto = container.newTransientInstance(Contacto.class);
		
		contacto.setDomicilio(direccion);
		contacto.setCelular(celular);
		contacto.setTelefono(telefono);
		contacto.setEmail(email);
		contacto.setUsuario(container.getUser().getName());
		
		container.persistIfNotAlready(contacto);
		
		setContacto(contacto);
		
		return this;
	}	
	//}}
	
	
	/*
	 * Forma en que la empresa le paga al hotel
	 * Crédito, Débito, Efectivo, Transferencia
	 */
	private FormaPago formaPago;
	
	/**
	 * Retorna la forma de pago que puede utilizar la empresa.
	 * @return
	 */
    public FormaPago getFormaPago() {
		return formaPago;
	}

    /**
     * Setea la forma de pago que puede utilizar la empresa.
     * @param formaPago
     */
	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}
		
	/*
	 * Estado : false (baja) - true (activo)
	 */
	private boolean estado;
	
	/**
	 * Retorna el estado de la empresa.
	 * 
	 */
	@Hidden
	public boolean isEstado() {
		return estado;
	}

	/**
	 * Setea el estado de la empresa.
	 * @param estado
	 */
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
   
	/**
	 * Permite borrar la empresa creada.
	 * @return
	 */
    @Named("Borrar")
    @Bulk
    @MemberOrder(name="accionesEmpresa", sequence = "1")
    public List<Empresa> baja() {
    	
    	container.removeIfNotAlready(this);
    	
    	return empresaServicio.listaEmpresas();
    }
	
    //{{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.setContainer(container);
    }

	public DomainObjectContainer getContainer() {
		return container;
	}

	public void setContainer(DomainObjectContainer container) {
		this.container = container;
	}
	
	/*
	 * Inyección del servicio
	 */
	
	private EmpresaServicio empresaServicio;
	
	public void injectEmpresaServicio(final EmpresaServicio serviceEnterprise) {
        this.empresaServicio = serviceEnterprise;
    }
    
    /*
     * 
     * Relacion bidireccional entre la empresa y los huespedes
     */
    @Persistent(mappedBy="empresa")
    private List<Huesped> huespedes = new ArrayList<Huesped>();
    
    //Eagerly para desplegar la lista
    /**
     * Retorna un listado de hu&eacute;spedes.
     * @return
     */
    @Named("Listado de huéspedes")
    @Render(Type.EAGERLY)
    public List<Huesped> getHuespedes() { return huespedes; } 
    
    /**
     * Agrega un hu&eacute;sped que puede ser relacionado con la empresa.
     * @param huesped
     * @return
     */
    @Named("Agregar Huesped")
    @PublishedAction
    @MemberOrder(name="huespedes",sequence="1")
    public Empresa add(final Huesped huesped) {
    	getHuespedes().add(huesped);
    	return this;
    }
    
    /**
     * Borra un hu&eacute;sped.
     * @param huesped
     * @return
     */
    @Named("Borrar Huesped")
    @MemberOrder(name="huespedes",sequence="2")
    public Empresa remove(final Huesped huesped) {
    	getHuespedes().remove(huesped);
    	return this;
    }
    
    // provide a drop-down
    public List<Huesped> choices0Remove() {
        return Lists.newArrayList(getHuespedes());
    }
    
    @Hidden
    public void addToHuesped(final Huesped huesped) {
    	if(huesped == null || huespedes.contains(huesped)) {
    		return;
    	}
    	huesped.setEmpresa(this);
    	huespedes.add(huesped);
    }
    
    //{{Usuario actual
  	private String usuario;

  	@Hidden
  	public String getUsuario() {
  	    return usuario;
  	}

  	public void setUsuario(final String usuario) {
  	    this.usuario = usuario;
  	}//}}
  		
  	public static Filter<Empresa> creadoPor(final String usuarioActual) {
  	    return new Filter<Empresa>() {
  	        @Override
  	        public boolean accept(final Empresa empresa) {
  	            return Objects.equal(empresa.getUsuario(), usuarioActual);
  	        }
  	    };
  	}	
	
}
