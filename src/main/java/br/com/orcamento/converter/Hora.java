package br.com.orcamento.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@FacesConverter(value = "hora", forClass = Date.class)
public class Hora implements Converter {

    public Hora() {
    }

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String valorTela) throws ConverterException {
        Date valorRetorno;

        String formato = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(formato);
        try {
            valorRetorno = formatter.parse(valorTela);
            return valorRetorno;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }


    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object valorTela) throws ConverterException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        return dateFormat.format(valorTela);
    }
}

