// ------------------------------ icpBravoAccess ------------------------------
var icpBravoAccess = (function() {
	var communicationPort = 0;
//	var httpPorts = [ 8347, 10434];
	var httpsPorts = [ 8348, 10435];
	var index = 0;
	
	var firstCheck = true;	
	var maxConnectionTrys = 2;

	var dataTypes = {
		Json : "json",
		JsonP : "jsonp"
	}

	var actions = {
		// SHARED
		Connect : "connect",

		// DEFAULT
		Auth : "auth",
		Sign : "sign",
		SignWithBufferedKey : "signWBK",
		Certificate : "certificate",
		Certificates : "certificates",

		// IE 7/8/9
		Auth_XDR : "auth_xdr",
		Sign_XDR : "sign_xdr",
		SignWithBufferedKey_XDR : "signWBK_xdr",
		Certificate_XDR : "certificate_xdr",
		Certificates_XDR: "certificates_xdr"
	}	

	// BASE ==========================================================================================================
	// ===============================================================================================================

	function connect(args){		
		if(firstCheck){
			args.onCBSuccess = args.onSuccess; 
			args.onCBError = args.onError;			
		}
		
		args.onSuccess = function(result){			
			if (result.message == 'OK') {
				isAppConnected = true;
				onConnectSuccessDefault(args, result);
			}		
		};
		
		args.onError = function(){
			onConnectionError(args, httpsPorts);
		};		
	
		if (isIE9or8()) {
			connectDefault(args, httpsPorts, dataTypes.JsonP);
			// connectXDR(args, ports, protocol);
		} else {
			connectDefault(args, httpsPorts, dataTypes.Json);
		}		
	}

	function onConnectionError (args, ports){
		
        if ((index == 0) && (firstCheck)) {
			firstCheck = false;
            window.location = 'icpbravoaccess://';
        }
		
		if (index + 1 == ports.length) {			
			if (maxConnectionTrys > 0) {
				index = 0;				
				
				sleep(5000);				
				
				maxConnectionTrys--;			
				connect(args);
			} else {
				args.onCBError({					
					responseText : "O ICPBravoAccess não está instalado ou não está sendo executado."
				});
			}
		} else {
			++index;
			connect(args);
		}
	}
	
	//http://stackoverflow.com/a/7282347
	function sleep(miliseconds) {
        var currentTime = new Date().getTime();

        while (currentTime + miliseconds >= new Date().getTime()) {
        }
    }
	
	function getCertificateList(args) {
		verifyPort();

		if (isIE9or8()) {
			return getCertificateListDefault(args, dataTypes.JsonP);
			//getCertificateListXDR(args);
		} else {
			return getCertificateListDefault(args, dataTypes.Json);
		}
	}	

	// certificate info retrieval. only available if the communicationPort has been established
	function getCertificate(args) {
		verifyPort();

		if (isIE9or8()) {
			return getCertificateDefault(args, dataTypes.JsonP);
			// getCertificateXDR(args);
		} else {
			return getCertificateDefault(args, dataTypes.Json);
		}
	}
	
	// signature procedure. only available if the communicationPort has been established
	function sign(args) {
		verifyPort();

		if (isIE9or8()) {
			return signDefault(args, dataTypes.JsonP);
			// signXDR(args);
		} else {
			return signDefault(args, dataTypes.Json);
		}
	}

	function signWithBufferedKey(args) {
		verifyPort();

		if (isIE9or8()) {
			return signWithBufferedKeyDefault(args, dataTypes.JsonP);
			// signWithBufferedKeyXDR(args);
		} else {
			return signWithBufferedKeyDefault(args, dataTypes.Json);
		}
	}
	
	// centralize ajax calls
	function accessCall(args, datatype, data, action, communicationPort){
			
		$.ajax({
			dataType : datatype,
			type : "POST",			
			crossDomain: true,
			url : BuildUrl(action, communicationPort),
			data : data,
			success : args.onSuccess,
			error : args.onError
		});
	}
	
	// END BASE ======================================================================================================
	// ===============================================================================================================
	
	// EXTERNAL FUNCTIONS ============================================================================================
	//================================================================================================================
		
	function getPortError(trials, portsLength) {
		if (trials == portsLength) {
			alert("ICPBravoAccess não encontrado");
		}
	}

	function verifyPort() {
		if (communicationPort == 0) {
			throw "ICPBravoAccess não encontrado";
		}
	}

	function getCurrentURL() {
		return (window.location.href);
	}

	// Check if InternetExplorer version is 9 or 8.
	function isIE9or8(){
		var version = getInternetExplorerVersion();
		return(version != -1 && version <= 9 && version >= 7);
		
		//return ($.browser.msie && $.browser.version <= 9 && window.XDomainRequest);
	}
	
	/**
	 * Returns the version of Internet Explorer or a -1
	 * (indicating the use of another browser).
	 */
	function getInternetExplorerVersion()
	{
	    var rv = -1; // Return value assumes failure.

	    if (navigator.appName == 'Microsoft Internet Explorer')
	    {
	        var ua = navigator.userAgent;
	        var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
	        if (re.exec(ua) != null)
	            rv = parseFloat( RegExp.$1 );
	    }

	    return rv;
	}

	function BuildUrl(action, port) {
		return "https://localhost:" + port + "/" + action;
	}
	
	/* http://stackoverflow.com/questions/2400935/browser-detection-in-javascript */
	var browser = function(){
	    var ua= navigator.userAgent, tem,
	    M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
	    if(/trident/i.test(M[1])){
	        tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
	        return 'IE '+(tem[1] || '');
	    }
	    if(M[1]=== 'Chrome'){
	        tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
	        if(tem!= null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
	    }
	    M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
	    if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
	    return M.join(' ');
	};
	
	// EXTERNAL FUNCTIONS ============================================================================================
	//================================================================================================================	

	// DEFAULT =======================================================================================================
	// ===============================================================================================================

	function connectDefault(args, ports, dataType) {		
		return accessCall(args, dataType, null, actions.Connect, ports[index]);   
	}

	function onConnectSuccessDefault(args, result){		
		communicationPort = result.httpsPort;
		
		var data = {
			license : args.license,
			url : getCurrentURL()
		}	
		
		var dataType = "";
		if (isIE9or8()) {
			dataType = dataTypes.JsonP;
		} else {
			dataType = dataTypes.Json;
		}		
				
		args.onSuccess = args.onCBSuccess;
		args.onError = args.onCBError;	

		return accessCall(args, dataType, data, actions.Auth, communicationPort);

	}

	function getCertificateListDefault(args, dataType){

		var data = {
			icpBrasil : args.icpbrasil, //DEPRECATED, should use type
			type : args.type,
			//Revoked is Deprecated (always false)
			revoked : false,
			reload : args.reload,
			valid : args.valid,
			cpf : args.cpf,
			url : getCurrentURL()
		}

		return accessCall(args, dataType, data, actions.Certificates, communicationPort);
	}

	function getCertificateDefault(args, dataType){
		var data = {
			thumbprint : args.thumbprint,
			url : getCurrentURL()
		}

		return accessCall(args, dataType, data, actions.Certificate, communicationPort);
	}

	function signDefault(args, dataType){

		var data = {
			toSign : args.toSign,
			thumbprint : args.thumbprint,
			url : getCurrentURL()
		}

		return accessCall(args, dataType, data, actions.Sign, communicationPort);
	}

	function signWithBufferedKeyDefault(args, dataType){

		var data = {
			toSign : args.toSign,
			thumbprint : args.thumbprint,
			url : getCurrentURL()
		}

		return accessCall(args, dataType, data, actions.SignWithBufferedKey, communicationPort);
	}


	// END DEFAULT =====================================================================================
	// =================================================================================================

	// IE 7/8/9 DEPRECATED =============================================================================
	// =================================================================================================

	function connectXDR(args, ports) {

		 var xdr = new XDomainRequest();
		 xdr.open("post", BuildUrl(actions.Connect, ports[index]));
		 xdr.onload = function(result) {
		 var result = $.parseJSON(xdr.responseText);
			 if (result.message == 'OK') {
				 onConnectSuccessXDR(args, result);
			 	}
		 };
		 xdr.onprogress = function(){ };
		 xdr.ontimeout = function(){ };
		 xdr.onerror = function(){onConnectionError(args, ports);}
		 setTimeout(function(){
			 xdr.send();
		 },0);
	}

	function onConnectSuccessXDR(args, result){

		communicationPort = result.httpsPort;		

		 var xdr = new XDomainRequest();
		 xdr.open("post", BuildUrl(actions.Auth_XDR, communicationPort));
		 xdr.onload = function(){args.onSuccess()};
		 xdr.onprogress = function(){ };
		 xdr.ontimeout = function(){ };
		 xdr.onerror = function(){args.onError()}

		 setTimeout(function(){
		 var data = {
			 license : args.license,
			 url : getCurrentURL()
		 };

		 xdr.send(JSON.stringify(data));
		 },0);

	}

	function getCertificateListXDR(args){

		 var xdr = new XDomainRequest();
		 xdr.open("post", BuildUrl(actions.Certificates_XDR, communicationPort));
		 xdr.onload = function(){
		 var result = $.parseJSON(xdr.responseText);
		 	args.onSuccess(result);
		 };
		 xdr.onprogress = function(){ };
		 xdr.ontimeout = function(){ };
		 xdr.onerror = function(){args.onError();}

		 setTimeout(function(){

		 var data = {
			 icpBrasil : args.icpbrasil,
			 revoked : args.revoked,
			 reload : args.reload,
			 valid : args.valid,
			 url : getCurrentURL()
		 };


		 xdr.send(JSON.stringify(data));

		 },0);

	}

	function getCertificateXDR(args){

		 var xdr = new XDomainRequest();
		 xdr.open("post", BuildUrl(actions.Certificate_XDR, communicationPort));
		 xdr.onload = function(){
		 var result = $.parseJSON(xdr.responseText);
		 	args.onSuccess(result);
		 }
		 xdr.onprogress = function(){ };
		 xdr.ontimeout = function(){ };
		 xdr.onerror = function(){args.onError();}

		 setTimeout(function(){

		 var data = {
			 thumbprint : args.thumbprint,
			 url : getCurrentURL()
		 };

		 xdr.send(JSON.stringify(data));

		 },0);
	}

	function signXDR(args){

		 var xdr = new XDomainRequest();
		 xdr.open("post", BuildUrl(actions.Sign_XDR, communicationPort));
		 xdr.onload = function(){
		 var result = $.parseJSON(xdr.responseText);
		 args.onSuccess(result);
		 }
		 xdr.onprogress = function(){ };
		 xdr.ontimeout = function(){ };
		 xdr.onerror = function(){args.onError();}

		 setTimeout(function(){

		 var data = {
			 toSign : args.toSign,
			 thumbprint : args.thumbprint,
			 url : getCurrentURL()
		 };

		 xdr.send(JSON.stringify(data));

		 },0);

	}

	function signWithBufferedKeyXDR(args){

		 var xdr = new XDomainRequest();
		 xdr.open("post", BuildUrl(actions.SignWithBufferedKey_XDR,
		 communicationPort));
		 xdr.onload = function(){
		 var result = $.parseJSON(xdr.responseText);
		 args.onSuccess(result);
		 }
		 xdr.onprogress = function(){ };
		 xdr.ontimeout = function(){ };
		 xdr.onerror = function(){args.onError();}

		 setTimeout(function(){

		 var data = {
			 toSign : args.toSign,
			 thumbprint : args.thumbprint,
			 url : getCurrentURL()
		 };

		 xdr.send(JSON.stringify(data));
		 
		 },0);
	}

	// END IE 7/8/9; Deprecated ================================================================================
	// =========================================================================================================

	return {
		connect : connect,
		getCertificateList : getCertificateList,
		getCertificate : getCertificate,
		sign : sign,
		signWithBufferedKey : signWithBufferedKey
	}
})();