package br.com.orcamento.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.com.orcamento.util.Contexto;

/**
 *
 * @author Renan Celso
 */
@FacesConverter(value = "data", forClass = Date.class)
public class Data implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String valorTela) throws ConverterException {
        Date valorRetorno;       
        try {
            if (valorTela.length() == 10) {

                Calendar c = Calendar.getInstance();

                int ano = Integer.parseInt(valorTela.substring(6));
                int mes = Integer.parseInt(valorTela.substring(3, 5)) - 1;
                int dia = Integer.parseInt(valorTela.substring(0, 2));
                                
                if(mes > 11){
//                	FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Valor '"+valorTela+"' Inválido para Campo de Data.", "Valor '"+valorTela+"' Inválido para Campo de Data.");
//            		FacesContext fc = FacesContext.getCurrentInstance();
//            		fc.addMessage(null, facesMsg);            		
            		Contexto.getSessao().setAttribute("erroData","Valor '"+valorTela+"' Inválido para Campo de Data.");
            		return null;
                }
                
                Calendar cal = Calendar.getInstance();
                cal.set(ano,mes,1);
                               
                if(dia > cal.getActualMaximum(Calendar.DAY_OF_MONTH)){
//                	FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Valor '"+valorTela+"' Inválido para Campo de Data.", "Valor '"+valorTela+"' Inválido para Campo de Data.");
//            		FacesContext fc = FacesContext.getCurrentInstance();
//            		fc.addMessage(null, facesMsg);
            		Contexto.getSessao().setAttribute("erroData","Valor '"+valorTela+"' Inválido para Campo de Data.");
			  		return null;
                }

                c.set(ano, mes, dia);

                valorRetorno = c.getTime();
                
                return valorRetorno;
                
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object valorTela) throws ConverterException {
        try {           
            String formato = "dd/MM/yyyy";
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            Date data = (Date) valorTela;
            return formatter.format(data);
        } catch (Exception e) {
            return null;
        }
    }
}
