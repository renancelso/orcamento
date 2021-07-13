package br.com.orcamento.padrao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 * @author Renan Celso
 */
public class BaseControl implements Serializable{
	
	private static final long serialVersionUID = 6653407730404794541L;
	private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
	
	private transient Logger log = Logger.getLogger(BaseControl.class.getSimpleName());	
			

	public BaseControl() {
		Locale.setDefault(new Locale("pt", "BR"));	
	}

	public static void addErrorMessage(List<?> listaErro) {
		for (int i = 0; i < listaErro.size(); i++) {
			String msg = (String) listaErro.get(i);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, facesMsg);
		}
	}

	public static void moverScrolParaTopo() {
		PrimeFaces.current().executeScript("$('html, body').animate({scrollTop:0}, 'slow');");
	}
	
	public static String addErrorMessage(String erro) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, erro, erro);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, facesMsg);
		return null;
	}

	public static String addInfoMessage(List<?> listaMsg) {
		for (int i = 0; i < listaMsg.size(); i++) {
			String msg = (String) listaMsg.get(i);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage("Info", facesMsg);
		}
		return null;
	}

	public static String addInfoMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage("Info", facesMsg);
		return null;
	}

	public static String addWarnMessage(List<?> listaMsg) {
		for (int i = 0; i < listaMsg.size(); i++) {
			String msg = (String) listaMsg.get(i);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage("Warn", facesMsg);
		}
		return null;
	}
    public static boolean validaCNPJCPF(String cpfCnpj) {
        return (isCPFValido(cpfCnpj) || isCNPJValido(cpfCnpj));
    }
	private static boolean isCPFValido(String cpf) {
	    cpf = cpf.trim().replace(".", "").replace("-", "");
	    if ((cpf==null) || (cpf.length()!=11)) return false;
	
	    for (int j = 0; j < 10; j++)
	        if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
	            return false;
	
	    Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
	    Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
	    return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
	}

    private static boolean isCNPJValido(String cnpj) {
        cnpj = cnpj.trim().replace(".", "").replace("-", "").replace("/", "");
        if ((cnpj==null)||(cnpj.length()!=14)) return false;

        Integer digito1 = calcularDigito(cnpj.substring(0,12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0,12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0,12) + digito1.toString() + digito2.toString());
    }
	private static int calcularDigito(String str, int[] peso) {
	    int soma = 0;
	    for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
	        digito = Integer.parseInt(str.substring(indice,indice+1));
	        soma += digito*peso[peso.length-str.length()+indice];
	    }
	    soma = 11 - soma % 11;
	    return soma > 9 ? 0 : soma;
	}
	
	private static String padLeft(String text, char character) {
	    return String.format("%11s", text).replace(' ', character);
	}
	/**
	 * 
	 * @author Renan Celso
	 * 
	 * @param msg
	 * @return
	 */
	public static String addWarnMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage("Warn", facesMsg);
		return null;
	}

	/**
	 * @author Renan Celso
	 * 
	 * @param listaMsg
	 * @return
	 */
	public static String addFatalMessage(List<?> listaMsg) {
		for (int i = 0; i < listaMsg.size(); i++) {
			String msg = (String) listaMsg.get(i);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg);
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage("Fatal", facesMsg);
		}
		return null;
	}

	/**
	 * 
	 * @author Renan Celso
	 * 
	 * @param msg
	 * @return
	 */
	public static String addFatalMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage("Fatal", facesMsg);
		return null;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 * 
	 * @author Renan Celso
	 * 
	 *         Transforma um objeto do tipo File em byte[]
	 * 
	 */
	public byte[] fileToByte(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		fis.close();
		return baos.toByteArray();
	}

	public boolean validarEmail(String email) {
		boolean isEmailIdValid = false;
		if (email != null && email.length() > 0) {
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				isEmailIdValid = true;
			}
		}
		return isEmailIdValid;
	}

	public static String removerAcentos(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public void redirect(String page) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(
					("/" + FacesContext.getCurrentInstance().getExternalContext().getContextName().toLowerCase() + page)
						   .replace(FacesContext.getCurrentInstance().getExternalContext().getContextName().toLowerCase()+"/",""));
		} catch (IOException e) {
			log.error(e);	
		}
	}				
	
	public static double diferencaEmDias(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24; //resultado é diferença entre as datas em dias
        long horasRestantes = (diferenca / 1000) / 60 / 60 % 24; //calcula as horas restantes
        result = diferencaEmDias + (horasRestantes / 24d); //transforma as horas restantes em fração de dias

        return result;
    }
	
	public String substituirAcentosPorPercentual(String txt) {
		String s = "";
		for (int i = 0; i < txt.length(); i++) {
			char c = txt.charAt(i);
			switch (c) {
			case 'Á':
			case 'À':
			case 'Ã':
				c = '%';
				break;
			case 'É':
			case 'Ê':
				c = '%';
				break;
			case 'Í':
				c = '%';
				break;
			case 'Ó':
			case 'Õ':
			case 'Ô':
				c = '%';
				break;
			case 'Ú':
				c = '%';
				break;
			case 'Ç':
				c = '%';
			case 'á':
			case 'à':
			case 'ã':
				c = '%';
				break;
			case 'é':
			case 'ê':
				c = '%';
				break;
			case 'í':
				c = '%';
				break;
			case 'ó':
			case 'õ':
			case 'ô':
				c = '%';
				break;
			case 'ú':
				c = '%';
				break;
			case 'ç':
				c = '%';
				break;
			}
			s += c;
		}
		return s;
	}
}