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
import dom.disponibilidad.HabitacionFecha;
import dom.enumeradores.FormaPago;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("EMPRESA")
@AutoComplete(repository=EmpresaServicio.class, action="completaEmpresas")
@Audited
public class Empresa {
		
	public String iconName() {
		return "empresa";
	}
	
	/*
	 * Razon Social de la empresa
	 */
	private String razonSocial;

	@Title
	//Letras, números y espacios
	@RegEx(validation="[\\w\\s]+")
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/*
	 * Cuit de la empresa
	 */
	private String cuit;
	//2 dígitos, un guión, 8 dígitos, otro guión y puede ser 1 o 2 dígitos
	@RegEx(validation="\\d{2}-\\d{8}-\\d{1,2}")
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	/*
	 * Tarifa especial por convenio
	 */
	private BigDecimal tarifa;	

	@RegEx(validation="\\d+\\.\\d{2}")
	public BigDecimal getTarifa() {
		return tarifa;
	}

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
	
	public Contacto getContacto() {
		return contacto;
	}
	
	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
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
	
    public FormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}
		
	/*
	 * Estado : false (baja) - true (activo)
	 */
	private boolean estado;
	
	/*
	 * No se publica el estado del objeto
	 */
	@Hidden
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
   
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
    @Named("Listado de huéspedes")
    @Render(Type.EAGERLY)
    public List<Huesped> getHuespedes() { return huespedes; } 
    
    @Named("Agregar Huesped")
    @PublishedAction
    @MemberOrder(name="huespedes",sequence="1")
    public Empresa add(final Huesped huesped) {
    	getHuespedes().add(huesped);
    	return this;
    }
    
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
