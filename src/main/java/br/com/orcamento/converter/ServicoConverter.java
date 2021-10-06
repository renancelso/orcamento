package br.com.orcamento.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;

import br.com.orcamento.model.Servico;
import br.com.orcamento.service.ServicoServiceLocal;


@FacesConverter(value = "ServicoConverter", forClass = Servico.class)
public class ServicoConverter implements Converter {
			
	@Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (arg2 != null && !"".equalsIgnoreCase(arg2)) {
        	Servico servico = (Servico) lookupServicoService().consultarPorChavePrimaria(new Servico(), Long.parseLong(arg2));
            return servico;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent arg1, Object arg2) {
        try {        	
            if (arg2 != null && !"".equals(arg2)) {
            	Servico entity = (Servico) arg2;
                String key = entity.getId().toString();
                arg1.getAttributes().put(key, entity);
                String codigo = entity.getId().toString();
                if (codigo != null) {
                    return codigo;
                }
            }            
            return (String) arg2;            
        } catch (Exception e) {
            return "";
        }
    }
    
    private ServicoServiceLocal lookupServicoService() {
		try {
			Context c = new InitialContext();
			return (ServicoServiceLocal) c.lookup("java:global/orcamento/ServicoService!br.com.orcamento.service.ServicoServiceLocal");
		} catch (Exception e) {			
			e.printStackTrace();
			throw new RuntimeException(e);			
		}
	}
}
