package br.com.orcamento.util;

import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.MaskFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

public class Util implements Serializable {

	private static final long serialVersionUID = 1285735409768909144L;
	
	private transient static Logger log = Logger.getLogger(Util.class.getSimpleName());	

	public static String getIpServidor() throws UnknownHostException {

		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
	public static String converteFormatoData(String data) throws ParseException{
		
		if(data.contains("/")) return data;
		
		return (data != null  && !data.equals("") && !data.equals("0"))  ? converteFormatoDataSefin(data):"";
	}
	
	public static String converteFormatoDataSefin(String data) throws ParseException{
		if(data == null) return "";
		final String OLD_FORMAT = "yyyyMMdd";
		final String NEW_FORMAT = "dd/MM/yyyy";


		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = sdf.parse(data);
		sdf.applyPattern(NEW_FORMAT);
		return  sdf.format(d);
	}
	public static String converteFormatoTimestamp(String data) throws ParseException{
		if(data == null) return "";
		final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
		final String NEW_FORMAT = "dd/MM/yyyy HH:mm:ss";
		
		
		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = sdf.parse(data);
		sdf.applyPattern(NEW_FORMAT);
		return  sdf.format(d);
	}
	
	public static String converteFormatoDataSefa(String data) throws ParseException{
		if(data == null) return "";
		final String OLD_FORMAT = "yyyy-MM-dd";
		final String NEW_FORMAT = "dd/MM/yyyy";


		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = sdf.parse(data);
		sdf.applyPattern(NEW_FORMAT);
		return  sdf.format(d);
	}
	
	public static String adicionaMascaraCpfCnpj(String valor, String tipo) throws ParseException{
		
		String mascara = null;
		if(!tipo.equalsIgnoreCase("2"))// OBS: n�o foi feito pelo n�mero de caracter por motivos de for�a maior.
			mascara = "##.###.###/####-##";
		else mascara = "###.###.###-##";
		 
		MaskFormatter mask = new MaskFormatter(mascara);
		mask.setValueContainsLiteralCharacters(false);
		
		return  mask.valueToString(String.format((tipo.equalsIgnoreCase("2") ? "%011d" : "%014d"), new BigInteger(valor)));
	
	}
	
	public static String adicionaMascaraCep(String valor) throws ParseException{
		
		String mascara = "#####-###";
		 
		MaskFormatter mask = new MaskFormatter(mascara);
		mask.setValueContainsLiteralCharacters(false);
		
		return  mask.valueToString(valor);
	}
	
	public static String adicionaMascaraProcesso(String valor) throws ParseException{
		
		String mascara = "#######-##.####.#.##.####";
		 
		MaskFormatter mask = new MaskFormatter(mascara);
		mask.setValueContainsLiteralCharacters(false);
		
		return  mask.valueToString(valor);
	}
	
	/**DEVIDO AS CONSTANTES MUDANÇAS NESSE WS POR PARTE DA SEFA O METODO ESPERA STRING POR MOTIVOS DE FORÇA MAIOR.*/
	public static String converteMonetario(String valor) {
		if(valor != null){
		Double valorD = null;
		Locale ptBr = new Locale("pt", "BR");
		NumberFormat format = NumberFormat.getCurrencyInstance(ptBr);
		/*try{*/
			 valorD = Double.parseDouble(valor);
		/*}catch (Exception e) {
			throw new Exception("N�o foi poss�vel converter valor :"+valor);
		}*/
			 return format.format(valorD);
		}
		return null;        
	}
	
	public static String converteMonetario2(Double valor) throws Exception{
		try{
			Locale ptBr = new Locale("pt", "BR");
			NumberFormat format = NumberFormat.getCurrencyInstance(ptBr);
			return format.format(valor);
		}catch(Exception e){
			throw e;
		}
	}
	
	public static String extrairAnoData(Date dta){
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String reportDate = df.format(dta);
		return reportDate.split("/")[2];
	}
	
	public static String Date2String(Date dta){
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return  df.format(dta);
		
	}
	
	public static Date String2Date(String data) throws ParseException{
		
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 return formatter.parse(data);
		 
	}

	public static Timestamp getHoraExata() {
		return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
	}

	public static String extraiModulo(Class<?> clazz) {
		return clazz.getPackage().getName().split(Pattern.quote("."))[5];
	}

	public static String decodePass(String encoded) throws UnsupportedEncodingException {
		/*
		 * Vem no formato xx-yy--zz xx:ipcliente yy:senha zz:login
		 */

		org.apache.commons.codec.binary.Base64 b64 = new org.apache.commons.codec.binary.Base64();
		byte[] byteToDecode = b64.decode(encoded);
		String passDecBase64 = new String(byteToDecode);
		//System.out.println(passDecBase64);
		// System.out.println(pwdDecBase64);

		// Faz decode do parametro
		// byte[] answer = Base64.getDecoder().decode(encoded);
		// Faz separa??o por -
		// String[] parts = new String(answer).split("-");
		// Pega somente a senha
		// String pass = new String(parts[1]);

		// Retorna senha decidad
		return passDecBase64;
	}
	
	public static String dataPorExtenso(Date date){
		 Date dataAtual = new Date();

	      // CRIO AQUI UM FORMATADOR
	      // PASSANDO UM ESTILO DE FORMATA??O : DateFormat.FULL
	      // E PASSANDO UM LOCAL DA DATA : new Locale("pt", "BR")
	      DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));

	      // FORMATO A DATA, O FORMATADOR ME RETORNA 
	      // A STRING DA DATA FORMATADA 
	    return  formatador.format(dataAtual);
	}
	
	public static String encodePass(String input) {
		byte[] byteArray = org.apache.commons.codec.binary.Base64.encodeBase64(input.getBytes());
		String encodedString = new String(byteArray);
		return encodedString;
	}
	
	public static String getIpCliente(){
		HttpServletRequest req = Contexto.getHttpServletRequest();
		String ipCliente = "";
		String ip = req.getRemoteAddr();	// IP do Cliente		
		if (req.getHeader("X-Forwarded-For") != null){
			String ipIntenroCliente = req.getHeader("X-Forwarded-For");
			ipCliente = ip.concat(",").concat(ipIntenroCliente);
		} else {
			ipCliente = ip;
	    }
		return ipCliente;
	}
	
	public static Object converteXML2Object(Class<?> clazz, String xml) throws JAXBException{
		
		StringReader sr = new StringReader(xml);
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		return unmarshaller.unmarshal(sr);
		
	}
	
	public static String extrairTagXML(String tag, String xml){
		final Pattern pattern = Pattern.compile("<"+tag+">(.+?)</"+tag+">");
		final Matcher matcher = pattern.matcher(xml);
		matcher.find();

		return matcher.group(1);
	}
	
	public static String removeCaracteres(String numeroFormatado) {
			Pattern patternSemCaracteres = Pattern.compile("([0-9])");
			if (numeroFormatado == null) {
				return "";
			}
			Matcher matcherNumero = patternSemCaracteres.matcher(numeroFormatado.trim());
			StringBuilder numeroSemFormatacao = new StringBuilder();
			while (matcherNumero.find()) {
				numeroSemFormatacao.append(matcherNumero.group());
			}
			
			return numeroSemFormatacao.toString(); 
	}
	
	public static String formatar(String valor, String mascara) {
		StringBuilder dado = new StringBuilder("");
		if (valor!=null){
			for (int i = 0; i < valor.length(); i++) {
				char c = valor.charAt(i);
				if (Character.isDigit(c)) {
					dado.append(c);
				}
			}
		}
		int indMascara = mascara.length();
		int indCampo = dado.toString().length();
		for (; indCampo > 0 && indMascara > 0;) {
			if (mascara.charAt(--indMascara) == '#') {
				indCampo--;
			}
		}
		StringBuilder saida = new StringBuilder("");
		for (; indMascara < mascara.length(); indMascara++) {
			saida.append((mascara.charAt(indMascara) == '#') ? dado.toString()
					.charAt(indCampo++) : mascara.charAt(indMascara));
		}
		return saida.toString();
	}
}


