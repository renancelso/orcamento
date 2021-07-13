package br.com.orcamento.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;


/**
 *
 * @author Renan Celso
 * 
 */
@FacesConverter(value = "cpfCnpj", forClass = String.class)
public class CpfCnpj implements Converter {
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
       
         String cpfCnpj = value;         
         if (value!= null && !"".equals(value)){
        	 
        	 if(value.length() == 11){   //CPF
        		 
        		 cpfCnpj = value.replaceAll("\\.", "").replaceAll("\\-", "");
        		 
        	 } else {//CNPJ
        		 
        		 cpfCnpj = value.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("/", "");
        		 
        	 }
         }

         return cpfCnpj;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
       
         String cpfCnpj= (String) value;
         
         if (cpfCnpj != null && cpfCnpj.length() == 11){ //CPF
        	 
             cpfCnpj = cpfCnpj.substring(0, 3) + "." + cpfCnpj.substring(3, 6) + "." + cpfCnpj.substring(6, 9) + "-" + cpfCnpj.substring(9, 11);
        
         } else if(cpfCnpj != null && cpfCnpj.length() > 11) {//CNPJ
        	 
        	 cpfCnpj = cpfCnpj.substring(0, 2) + "." + cpfCnpj.substring(2, 5) + "." + cpfCnpj.substring(5, 8 ) + "/" + cpfCnpj.substring(8, 12) + "-" + cpfCnpj.substring(12, 14);
        
         }
         
         return cpfCnpj;
    }
}