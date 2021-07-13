package br.com.orcamento.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Renan Celso
 */
@FacesConverter(value = "dataHora", forClass = Date.class)
public class DataHora implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String valorTela) throws ConverterException {
        Date valorRetorno;       
        try {
            if (valorTela.length() == 10) {

                Calendar c = Calendar.getInstance();

                int ano = Integer.parseInt(valorTela.substring(6));
                int mes = Integer.parseInt(valorTela.substring(3, 5)) - 1;
                int dia = Integer.parseInt(valorTela.substring(0, 2));

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
            String formato = "dd/MM/yyyy HH:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            Date data = (Date) valorTela;
            return formatter.format(data);
        } catch (Exception e) {
            return null;
        }
    }
}
