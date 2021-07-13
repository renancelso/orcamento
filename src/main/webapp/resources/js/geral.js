// Popup window code
function newPopup(url) {
    w = screen.width;
    h = screen.height;
    //divide a resolu��o por 2, obtendo o centro do monitor
    meio_w = w / 2;
    meio_h = h / 2;
    altura = 539;
    largura = 765;
    //diminui o valor da metade da resolu��o pelo tamanho da janela, fazendo com q ela fique centralizada
    altura2 = altura / 2;
    largura2 = largura / 2;
    meio1 = meio_h - altura2;
    meio2 = meio_w - largura2;

    //abre a nova janela, j� com a sua devida posi��o

    popupWindow = window.showModalDialog(
            //pega a resolu��o do visitante


            url, 'popUpWindow', 'resizable: false;dialogWidth:765px; dialogHeight:539px; dialogTop:' + meio1 + ';dialogLeft:' + meio2 + ' ')
}


function setarsalvar() {
    var obj = document.getElementById("formPadrao:btnsalvar");
    obj.focus();
}

function iniciarlocate(objeto, event) {
    if (event.keyCode == 113)
        document.getElementById(objeto).click();
}

function setarfoco(objeto) {
    var obj = document.getElementById(objeto);
    obj.focus();
}

function check(obj, master) {

    for (var i = 0; i < document.formPadrao.elements.length; i++) {

        var x = document.formPadrao.elements[i];

        if (x.name.toString().indexOf(obj) > -1) {
            x.checked = master.checked;
        }

    }

}

function refresh() {
    window.location.reload();
}

function PopUp() {
    Width = 800;
    Height = 600;
    if (win && win.open)
        win.close()
    win = window.open("../ReportTemp", "Eurus tecnologia", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,border=0,resizable=yes,width=" + Width + ",height=" + Height + ",copyhistory=no,top=120");
}



function abrirJanela(url) {
    window.open(url, 'page', 'toolbar=no,left=0,top=0,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=300');
}

function grid(event) {
    var dataGrid = f_core.GetElementById("directoryGrid");
    dataGrid.f_setFocus();
    dataGrid.f_refreshContent();

}

function cidade(event) {
    var dataGrid = f_core.GetElementById("grid");
    dataGrid.f_setFocus();
    dataGrid.f_refreshContent();

}

function atualizaM() {
    var tj = document.getElementsByName('forminc:tipoJuros');
    var ij = document.getElementById('forminc:juros');


    if (tj[0].checked)
        ij.disabled = true;
    else
        ij.disabled = false;



}

function testButton() {

    var edit = document.getElementById('forminc:nrchequefinal');
    var t = document.getElementsByName('forminc:tipo');

    if (t[0].checked) {
        edit.disabled = true;
        edit.value = '';
    }
    else
        edit.disabled = false;
}




function testtrib() {

    var aliquota = document.getElementsByName('forminc:aliquota');
    var ufm = document.getElementsByName('forminc:ufm');


    if (aliquota[0].checked) {

        ufm[0].disabled = true;
        ufm[1].disabled = true;
    }
    else {
        ufm[0].disabled = false;
        ufm[1].disabled = false;
        ufm[0].checked = true;
    }


}

documentall = document.all;
/*
 * fun��o para formata��o de valores monet�rios retirada de
 * http://jonasgalvez.com/br/blog/2003-08/egocentrismo
 */

function formatamoney(c) {
    var t = this;
    if (c == undefined)
        c = 2;
    var p, d = (t = t.split("."))[1].substr(0, c);
    for (p = (t = t[0]).length; (p -= 3) >= 1; ) {
        t = t.substr(0, p) + "." + t.substr(p);
    }
    return t + "," + d + Array(c + 1 - d.length).join(0);
}

String.prototype.formatCurrency = formatamoney

function demaskvalue(valor, currency) {
    /*
     * Se currency � false, retorna o valor sem apenas com os n�meros. Se � true, os dois �ltimos caracteres s�o considerados as 
     * casas decimais
     */
    var val2 = '';
    var strCheck = '0123456789';
    var len = valor.length;
    if (len == 0) {
        return 0.00;
    }

    if (currency == true) {
        /* Elimina os zeros � esquerda 
         * a vari�vel  <i> passa a ser a localiza��o do primeiro caractere ap�s os zeros e 
         * val2 cont�m os caracteres (descontando os zeros � esquerda)
         */

        for (var i = 0; i < len; i++)
            if ((valor.charAt(i) != '0') && (valor.charAt(i) != ','))
                break;

        for (; i < len; i++) {
            if (strCheck.indexOf(valor.charAt(i)) != -1)
                val2 += valor.charAt(i);
        }

        if (val2.length == 0)
            return "0.00";
        if (val2.length == 1)
            return "0.0" + val2;
        if (val2.length == 2)
            return "0." + val2;

        var parte1 = val2.substring(0, val2.length - 2);
        var parte2 = val2.substring(val2.length - 2);
        var returnvalue = parte1 + "." + parte2;
        return returnvalue;

    }
    else {
        /* currency � false: retornamos os valores COM os zeros � esquerda, 
         * sem considerar os �ltimos 2 algarismos como casas decimais 
         */
        val3 = "";
        for (var k = 0; k < len; k++) {
            if (strCheck.indexOf(valor.charAt(k)) != -1)
                val3 += valor.charAt(k);
        }
        return val3;
    }
}




function backspace(obj, event) {
    /*
     Essa fun��o basicamente altera o  backspace nos input com m�scara reais para os navegadores IE e opera.
     O IE n�o detecta o keycode 8 no evento keypress, por isso, tratamos no keydown.
     Como o opera suporta o infame document.all, tratamos dele na mesma parte do c�digo.
     */

    var whichCode = (window.Event) ? event.which : event.keyCode;
    if (whichCode == 8 && documentall) {
        var valor = obj.value;
        var x = valor.substring(0, valor.length - 1);
        var y = demaskvalue(x, true).formatCurrency();

        obj.value = ""; //necess�rio para o opera
        obj.value += y;

        if (event.preventDefault) { //standart browsers
            event.preventDefault();
        } else { // internet explorer
            event.returnValue = false;
        }
        return false;

    }// end if		
}// end backspace






function foco(campo) {
    i = document.getElementById('forminc:' + campo);
    //globalvar = campo;
    i.focus();
//setTimeout("globalvar.focus()",50);
}
function Verificar() {
    var tecla = window.event.keyCode;
    if (tecla == 116) {
        event.keyCode = 0;
        event.returnValue = false;
    }
}

function tab(field, event) {
    var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
    if (keyCode == 13) {
        var i;
        for (i = 0; i < field.form.elements.length; i++)
            if (field == field.form.elements[i])
                break;
        i = (i + 1) % field.form.elements.length;
        field.form.elements[i].focus();
        return false;
    }
    else
        return true;
}

function MascaraMoeda(objTextBox, SeparadorMilesimo, SeparadorDecimal, e) {	
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    var aux = aux2 = '';
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if (whichCode == 13 || whichCode == 8 || whichCode == 0)
        return true;
    key = String.fromCharCode(whichCode); // Valor para o código da Chave
    if (strCheck.indexOf(key) == -1)
        return false; // Chave inválida
    len = objTextBox.value.length;
    for (i = 0; i < len; i++)
        if ((objTextBox.value.charAt(i) != '0') && (objTextBox.value.charAt(i) != SeparadorDecimal))
            break;
    aux = '';
    for (; i < len; i++)
        if (strCheck.indexOf(objTextBox.value.charAt(i)) != -1)
            aux += objTextBox.value.charAt(i);
    aux += key;
    len = aux.length;
    if (len == 0)
        objTextBox.value = '';
    if (len == 1)
        objTextBox.value = '0' + SeparadorDecimal + '0' + aux;
    if (len == 2)
        objTextBox.value = '0' + SeparadorDecimal + aux;
    if (len > 2) {
        aux2 = '';
        for (j = 0, i = len - 3; i >= 0; i--) {
            if (j == 3) {
                aux2 += SeparadorMilesimo;
                j = 0;
            }
            aux2 += aux.charAt(i);
            j++;
        }
        objTextBox.value = '';
        len2 = aux2.length;
        for (i = len2 - 1; i >= 0; i--)
            objTextBox.value += aux2.charAt(i);
        objTextBox.value += SeparadorDecimal + aux.substr(len - 2, len);
    }
    return false;
}

function decimal(objTextBox, e) {
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    var aux = aux2 = '';
    if (e.which) {
        var whichCode = e.which;
    } else {
        var whichCode = e.keyCode;
    }
    if ((whichCode == 13) || (whichCode == 0) || (whichCode == 8)) return true;
    key = String.fromCharCode(whichCode); // Valor para o código da Chave
    if (strCheck.indexOf(key) == -1) return false; // Chave inválida
    len = objTextBox.value.length;
    for (i = 0; i < len; i++)
        if ((objTextBox.value.charAt(i) != '0') && (objTextBox.value.charAt(i) != ",")) break;
    aux = '';
    for (; i < len; i++)
        if (strCheck.indexOf(objTextBox.value.charAt(i)) != -1) aux += objTextBox.value.charAt(i);
    aux += key;
    len = aux.length;
    if (len == 0) objTextBox.value = '';
    if (len == 1) objTextBox.value = '0' + "," + '0' + aux;
    if (len == 2) objTextBox.value = '0' + "," + aux;
    if (len > 2 && len < 13) {
        aux2 = '';
        for (j = 0, i = len - 3; i >= 0; i--) {
            if (j == 3) {
                aux2 += ".";
                j = 0;
            }
            aux2 += aux.charAt(i);
            j++;
        }
        objTextBox.value = '';
        len2 = aux2.length;
        for (i = len2 - 1; i >= 0; i--)
            objTextBox.value += aux2.charAt(i);
        objTextBox.value += "," + aux.substr(len - 2, len);
    }
    return false;
}


function mascara(o, f) {
    v_obj = o
    v_fun = f
    setTimeout("execmascara()", 1)
}

function execmascara() {
    v_obj.value = v_fun(v_obj.value)
}

function leech(v) {
    v = v.replace(/o/gi, "0")
    v = v.replace(/i/gi, "1")
    v = v.replace(/z/gi, "2")
    v = v.replace(/e/gi, "3")
    v = v.replace(/a/gi, "4")
    v = v.replace(/s/gi, "5")
    v = v.replace(/t/gi, "7")
    return v
}

function soNumeros(v) {
    return v.replace(/\D/g, "");
}

function telefone(v) {
    v = v.replace(/\D/g, "")                 //Remove tudo o que n�o � d�gito
    v = v.replace(/^(\d\d)(\d)/g, "($1) $2") //Coloca par�nteses em volta dos dois primeiros d�gitos
    //v = v.replace(/(\d{5})(\d)/, "$1-$2")    //Coloca h�fen entre o quarto e o quinto d�gitos
    return v
}
function rg(v) {
    v = v.replace(/D/g, "")                //Remove tudo o que n�o � d�gito
    v = v.replace(/^(\d{3})(\d)/, "$1.$2") //Esse � t�o f�cil que n�o merece explica��es
    return v
}

function tributo(v) {
    v = v.replace(/D/g, "")                //Remove tudo o que n�o � d�gito
    return v
}

function cpf(v) {
    v = v.replace(/\D/g, "")                    //Remove tudo o que n�o � d�gito
    v = v.replace(/(\d{3})(\d)/, "$1.$2")       //Coloca um ponto entre o terceiro e o quarto d�gitos
    v = v.replace(/(\d{3})(\d)/, "$1.$2")       //Coloca um ponto entre o terceiro e o quarto d�gitos
    //de novo (para o segundo bloco de n�meros)
    v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2") //Coloca um h�fen entre o terceiro e o quarto d�gitos
    return v
}

function cep(v) {
    v = v.replace(/D/g, "")                //Remove tudo o que n�o � d�gito
    v = v.replace(/^(\d{5})(\d)/, "$1-$2") //Esse � t�o f�cil que n�o merece explica��es
    return v
}

function cnpj(v) {
    v = v.replace(/\D/g, "")                           //Remove tudo o que n�o � d�gito
    v = v.replace(/^(\d{2})(\d)/, "$1.$2")             //Coloca ponto entre o segundo e o terceiro d�gitos
    v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3") //Coloca ponto entre o quinto e o sexto d�gitos
    v = v.replace(/\.(\d{3})(\d)/, ".$1/$2")           //Coloca uma barra entre o oitavo e o nono d�gitos
    v = v.replace(/(\d{4})(\d)/, "$1-$2")              //Coloca um h�fen depois do bloco de quatro d�gitos
    return v
}

function romanos(v) {
    v = v.toUpperCase()             //Mai�sculas
    v = v.replace(/[^IVXLCDM]/g, "") //Remove tudo o que n�o for I, V, X, L, C, D ou M
    //Essa � complicada! Copiei daqui: http://www.diveintopython.org/refactoring/refactoring.html
    while (v.replace(/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/, "") != "")
        v = v.replace(/.$/, "")
    return v
}


function formatar(src, mask, e) {

    var tecla = (window.event) ? e.keyCode : e.which;/*pega qual tecla foi pressionada*/

    if (tecla == 8 || tecla == 0)
        return true; /*retorna verdadeiro se foi delete, backspace...*/

    var i = src.value.length;
    var saida = mask.substring(0, 1);
    var texto = mask.substring(i)

    if (texto.substring(0, 1) != saida) {
        src.value += texto.substring(0, 1);
    }


}

//function formatar(src, mask){
//
//    var i = src.value.length;
//    var saida = mask.substring(0,1);
//    var texto = mask.substring(i)
//    if (texto.substring(0,1) != saida){
//        src.value += texto.substring(0,1);
//    }
//}


function MascaraData(data){
	if(mascaraInteiro(data)==false){
		event.returnValue = false;
	}	
	return formataCampo(data, '00/00/0000', event);
}

function formataCampo(campo, Mascara, evento) { 
	var boleanoMascara; 
	
	var Digitato = evento.keyCode;
	exp = /\-|\.|\/|\(|\)| /g;
	campoSoNumeros = campo.value.toString().replace( exp, "" ); 
   
	var posicaoCampo = 0;	 
	var NovoValorCampo="";
	var TamanhoMascara = campoSoNumeros.length;; 
	
	if (Digitato != 8) { // backspace 
		for(i=0; i < TamanhoMascara+1; i++) { 
			boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
								|| (Mascara.charAt(i) == "/")) 
			boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
								|| (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " ")) 
			if (boleanoMascara) { 
				NovoValorCampo += Mascara.charAt(i); 
				  TamanhoMascara++;
			}else { 
				NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
				posicaoCampo++; 
			  }	   	 
		  }	 
		campo.value = NovoValorCampo;
		return true; 
	}else { 
		return true; 
	}
}

function site(v) {
    //Esse sem comentarios para que voc� entenda sozinho ;-)
    v = v.replace(/^http:\/\/?/, "")
    dominio = v
    caminho = ""
    if (v.indexOf("/") > -1)
        dominio = v.split("/")[0]
    caminho = v.replace(/[^\/]*/, "")
    dominio = dominio.replace(/[^\w\.\+-:@]/g, "")
    caminho = caminho.replace(/[^\w\d\+-@:\?&=%\(\)\.]/g, "")
    caminho = caminho.replace(/([\?&])=/, "$1")
    if (caminho != "")
        dominio = dominio.replace(/\.+$/, "")
    v = "http://" + dominio + caminho
    return v
}

function MascaraCEP(campo, teclaPress) {

    if (window.event) {
        var tecla = teclaPress.keyCode;

    } else {
        tecla = teclaPress.which;
    }

    if (tecla == 8) {
        return true;
    }

    var s = new String(campo.value);
    s = s.replace(/(\.|\(|\)|\/|\-| )+/g, '');
    tam = s.length + 1;
    if (tam > 5 && tam < 7)
        campo.value = s.substr(0, 5) + '-' + s.substr(5, tam);
}

function digitos(event) {
    if (key == 8) {
        return true;
    }
    if (window.event) {
        // IE
        key = event.keyCode;
    }
    else if (event.which) {
        // netscape
        key = event.which;
    }
    if (key != 8 || key != 13 || key < 48 || key > 57)
        return (((key > 47) && (key < 58)) || (key == 8) || (key ==
                13));
    return true;
}

function moeda(campo, e)
{
    var SeparadorDecimal = ","
    var SeparadorMilesimo = "."
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    var aux = aux2 = '';
    var whichCode = (window.Event) ? e.which : e.keyCode;

    if (whichCode == 13)
        return true;
    key = String.fromCharCode(whichCode); // Valor para o código da Chave

    if (strCheck.indexOf(key) == -1)
        return true; // Chave inválida
    len = campo.value.length;
    for (i = 0; i < len; i++)
        if ((campo.value.charAt(i) != '0') && (campo.value.charAt(i) != SeparadorDecimal))
            break;
    aux = '';
    for (; i < len; i++)
        if (strCheck.indexOf(campo.value.charAt(i)) != -1)
            aux += campo.value.charAt(i);
    aux += key;
    len = aux.length;

    if (len == 0)
        campo.value = '';
    if (len == 1)
        campo.value = '0' + SeparadorDecimal + '0' + aux;
    if (len == 2)
        campo.value = '0' + SeparadorDecimal + aux;
    if (len > 2) {
        aux2 = '';
        for (j = 0, i = len - 3; i >= 0; i--) {
            if (j == 3) {
                aux2 += SeparadorMilesimo;
                j = 0;
            }
            aux2 += aux.charAt(i);
            j++;
        }
        campo.value = '';
        len2 = aux2.length;
        for (i = len2 - 1; i >= 0; i--)
            campo.value += aux2.charAt(i);
        campo.value += SeparadorDecimal + aux.substr(len - 2, len);
    }
    return false;

}


function UR_Start()
{
    UR_Nu = new Date;
    UR_Indhold = showFilled(UR_Nu.getHours()) + ":" + showFilled(UR_Nu.getMinutes()) + ":" + showFilled(UR_Nu.getSeconds());
    document.getElementById("ur").innerHTML = UR_Indhold;
    setTimeout("UR_Start()", 1000);
}

function showFilled(Value)
{
    return (Value > 9) ? "" + Value : "0" + Value;
}

function carregaData() {
    var data = datas();
    document.forms[0].dataAtual.value = data;
}

function datas() {
    var hoje = new Date();
    var hojeDia = hoje.getDate();
    var hojeMes = hoje.getMonth();
    var hojeAno = hoje.getFullYear();

    var meses = new Array(12);
    meses[0] = "Janeiro";
    meses[1] = "Fevereiro";
    meses[2] = "Março";
    meses[3] = "Abril";
    meses[4] = "Maio";
    meses[5] = "Junho";
    meses[6] = "Julho";
    meses[7] = "Agosto";
    meses[8] = "Setembro";
    meses[9] = "Outubro";
    meses[10] = "Novembro";
    meses[11] = "Dezembro";
    document.getElementById("dataAtual").innerHTML = hojeDia + " de " + meses[hojeMes] + " de " + hojeAno + ".";

}

function ValidarCPF(Objcpf){
	 str = Objcpf.value;
	 str = str.replace('.','');
	 str = str.replace('.','');
	 str = str.replace('-','');
	 cpf = str;
	 if (cpf.length != 11 ||
			  cpf == "00000000000" ||
			  cpf == "11111111111" ||
			  cpf == "22222222222" ||
			  cpf == "33333333333" ||
			  cpf == "44444444444" ||
			  cpf == "55555555555" ||
			  cpf == "66666666666" ||
			  cpf == "77777777777" ||
			  cpf == "88888888888" ||
			  cpf == "99999999999"){					 
			return false;
	 }
			 
	add = 0;
	 
	for (i=0; i < 9; i ++){
		add += parseInt(cpf.charAt(i)) * (10 - i);
	}

	rev = 11 - (add % 11);

	if (rev == 10 || rev == 11){
		rev = 0;
	}
	
	if (rev != parseInt(cpf.charAt(9))){
		alert('O CPF INFORMADO É INVÁLIDO.');
		Objcpf.value = '';				
		return false;
	}
	add = 0;
	
	for (i = 0; i < 10; i ++){
		add += parseInt(cpf.charAt(i)) * (11 - i);
	}

	rev = 11 - (add % 11);
	
	if (rev == 10 || rev == 11){
		rev = 0;
	}

	if (rev != parseInt(cpf.charAt(10))){
		alert('O CPF INFORMADO É INVÁLIDO.');
		Objcpf.value = '';
		return false;
	}					
	
	return true;
}

function MascaraCPF(cpf){
	if(mascaraInteiro(cpf)==false){
		event.returnValue = false;
	}	
	return formataCampo(cpf, '000.000.000-00', event);
}

function mascaraInteiro(){
	if (event.keyCode < 48 || event.keyCode > 57){
		event.returnValue = false;
		return false;
	}
	return true;
}

function validar(dom,tipo){
		switch(tipo){
			case'num':var regex=/[A-Za-z]/g;break;
			case'text':var regex=/\d/g;break;
		}
		dom.value=dom.value.replace(regex,'');
	}
	
	function mascaraCpfCpnj(cpfCnpj) {			
		str = cpfCnpj.value;
		str = str.replace('.','');
		str = str.replace('.','');
		str = str.replace('/','');
		str = str.replace('-','');		
		cpfCnpjAux = str;
		if(cpfCnpjAux.length < 12) {
			MascaraCPF(cpfCnpj);
		} else {						
			MascaraCNPJ(cpfCnpj);
		}		
	}

	function validarCpfCnpj(cpfCnpj){
		str = cpfCnpj.value;
		str = str.replace('.','');
		str = str.replace('.','');
		str = str.replace('/','');
		str = str.replace('-','');		
		cpfCnpjAux = str;

		if(cpfCnpjAux.length > 0) {
			if(cpfCnpjAux.length < 11){
				alert('CPF / CNPJ Invalido!');
				cpfCnpj.value = '';
			} else {		
				if(cpfCnpjAux.length < 12) {
					ValidarCPF(cpfCnpj);
				} else {
					ValidarCNPJ(cpfCnpj);
				}
			}
		} 
	
	}

	function MascaraCNPJ(cnpj){
		if(mascaraInteiro(cnpj)==false){
			event.returnValue = false;
		}	
		return formataCampo(cnpj, '00.000.000/0000-00', event);
	}

	//valida o CNPJ digitado
	function ValidarCNPJ(ObjCnpj){
		var cnpj = ObjCnpj.value;
		var valida = new Array(6,5,4,3,2,9,8,7,6,5,4,3,2);
		var dig1= new Number;
		var dig2= new Number;
		
		exp = /\.|\-|\//g
		cnpj = cnpj.toString().replace( exp, "" ); 
		var digito = new Number(eval(cnpj.charAt(12)+cnpj.charAt(13)));
			
		for(i = 0; i<valida.length; i++){
			dig1 += (i>0? (cnpj.charAt(i-1)*valida[i]):0);	
			dig2 += cnpj.charAt(i)*valida[i];	
		}
		dig1 = (((dig1%11)<2)? 0:(11-(dig1%11)));
		dig2 = (((dig2%11)<2)? 0:(11-(dig2%11)));
		
		if(((dig1*10)+dig2) != digito)	{
			alert('CNPJ Invalido!');
			ObjCnpj.value = '';
		}
			
	}
	
	
	function maskIt(w,e,m,r,a){
		// Cancela se o evento for Backspace
		if (!e) var e = window.event
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		// Variáveis da função
		var txt  = (!r) ? w.value.replace(/[^\d]+/gi,'') : w.value.replace(/[^\d]+/gi,'').reverse();
		var mask = (!r) ? m : m.reverse();
		var pre  = (a ) ? a.pre : "";
		var pos  = (a ) ? a.pos : "";
		var ret  = "";
		if(code == 9 || code == 8 || txt.length == mask.replace(/[^#]+/g,'').length) return false;
		// Loop na máscara para aplicar os caracteres
		for(var x=0,y=0, z=mask.length;x<z && y<txt.length;){
		if(mask.charAt(x)!='#'){
		ret += mask.charAt(x); x++; } 
		else {
		ret += txt.charAt(y); y++; x++; } }
		// Retorno da função
		ret = (!r) ? ret : ret.reverse()	
		w.value = pre+ret+pos; }
		// Novo método para o objeto 'String'
		String.prototype.reverse = function(){
		return this.split('').reverse().join(''); };
			
		function number_format( number, decimals, dec_point, thousands_sep ) {
		var n = number, c = isNaN(decimals = Math.abs(decimals)) ? 2 : decimals;
		var d = dec_point == undefined ? "," : dec_point;
		var t = thousands_sep == undefined ? "." : thousands_sep, s = n < 0 ? "-" : "";
		var i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
		return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
		}
	
		function calcula(operacion){ 
		var operando1 = parseFloat( document.calc.operando1.value.replace(/\./g, "").replace(",", ".") );
		var operando2 = parseFloat( document.calc.operando2.value.replace(/\./g, "").replace(",", ".") );
		var result = eval(operando1 + operacion + operando2);
		document.calc.resultado.value = number_format(result,2, ',', '.');
	} 	
		
	function maiuscula(z){
        v = z.value.toUpperCase();
        z.value = v;
    }

PrimeFaces.locales['pt_BR'] = {
    closeText: 'Fechar',
    prevText: 'Anterior',
    nextText: 'Próximo',
    currentText: 'Hoje',
    monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
    monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
    dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
    dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
    dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Só Horas',
    timeText: 'Tempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    ampm: false,
    month: 'Mês',
    week: 'Semana',
    day: 'Dia',
    allDayText: 'Todo Dia'
};