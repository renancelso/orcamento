package br.com.orcamento.converter;

import java.text.NumberFormat;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Renan Celso
 */
@FacesConverter(value = "percentual", forClass = Double.class)
public class Percentual implements Converter {

    public Percentual() {
    }

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String valorTela) throws ConverterException {
        Double valorRetorno;

        if (valorTela == null || valorTela.equals("")) {
            return null;
        } else {
            valorTela = valorTela.replace("%", "");
            valorTela = valorTela.replace(" ", "");
            valorTela = valorTela.replace(".", "");
            valorTela = valorTela.replace(",", ".");
            valorRetorno = Double.valueOf(valorTela);
            return valorRetorno;
        }

    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object valorTela) throws ConverterException {

        if (valorTela == null || valorTela.equals("")) {
            return null;

        } else {
            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);

            String valorTela2 = valorTela.toString();


            valorTela2 = valorTela2.replace("%", "");


            return nf.format(Double.valueOf(valorTela2)) + " %";
        }


    }
}
