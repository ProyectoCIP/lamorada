package dom.contacto;

import org.apache.isis.applib.adapters.AbstractValueSemanticsProvider;

public final class ContactoVOValueSemanticsProvider extends AbstractValueSemanticsProvider <ContactoVO> {
	
	public void getParsed() {
		// TODO Auto-generated method stub
		
	}
	
	protected String doEncode(final Object object) {
		final ContactoVO contacto = (ContactoVO) object;
		final String valor = String.valueOf(contacto.getDomicilio() + "-" + contacto.getEmail() + "-" + contacto.getTelefono());
		return valor;
	}
	
	protected ContactoVO doRestore(final String data) {
		final String[] partes = data.split("-");
		final String domicilio = partes[0];
		final String correo = partes[1];
		final String telefono = partes[2];
		return new ContactoVO(domicilio, correo, telefono);		
	}
	
}
