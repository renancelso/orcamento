package br.com.orcamento.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {
	
	
	
	public static String removeMascara(String processo){
		
		return processo.replaceAll("[\\s.]", "").replaceAll("-", "");
		
	}
	
	public static String mascaraProcesso(String numeroProcesso) {
        if (!StringUtils.isEmpty(numeroProcesso)) {
        	
        	if(numeroProcesso.contains(".") || numeroProcesso.contains("-"))
        		numeroProcesso = removeMascara(numeroProcesso);
        	
            try {
                MaskFormatter mf = new MaskFormatter("#######-##.####.#.##.####");
                mf.setValueContainsLiteralCharacters(false);
                return mf.valueToString(numeroProcesso);
            } catch (ParseException e) {
            	
            }
        }
        return null;
    }
	
	public static String formatarValorMoeda(Double valor, boolean incluirSimboloMonetario) {
		if (valor != null) {
			String mascaraFormatacao = (incluirSimboloMonetario ? "R$ " : StringUtils.EMPTY) + "###,###,###,##0.00";
			DecimalFormatSymbols formatador = new DecimalFormatSymbols(new Locale("pt", "BR"));
			DecimalFormat valorFormatado = new DecimalFormat(mascaraFormatacao, formatador);
			return valorFormatado.format(valor);
		}
		return StringUtils.EMPTY;
	}
	
	
}
