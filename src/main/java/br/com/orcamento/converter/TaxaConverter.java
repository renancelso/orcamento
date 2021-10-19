package br.com.orcamento.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;

import br.com.orcamento.model.Taxa;
import br.com.orcamento.service.TaxaServiceLocal;


@FacesConverter(value = "TaxaConverter", forClass = Taxa.class)
public class TaxaConverter implements Converter {
			
	@Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (arg2 != null && !"".equalsIgnoreCase(arg2)) {
        	Taxa taxa = (Taxa) lookupTaxaService().consultarPorChavePrimaria(new Taxa(), Long.parseLong(arg2));
            return taxa;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent arg1, Object arg2) {
        try {        	
            if (arg2 != null && !"".equals(arg2)) {
            	Taxa entity = (Taxa) arg2;
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
    
    private TaxaServiceLocal lookupTaxaService() {
		try {
			Context c = new InitialContext();
			return (TaxaServiceLocal) c.lookup("java:global/orcamento/TaxaService!br.com.orcamento.service.TaxaServiceLocal");
		} catch (Exception e) {			
			e.printStackTrace();
			throw new RuntimeException(e);			
		}
	}
}
