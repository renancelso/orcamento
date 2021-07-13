// ------------------------------ icpBravoAccessPort ------------------------------
var icpBravoAccessPort = (function () {

	// BASE ==========================================================================================================
	// ===============================================================================================================
	var isChrome = false;

	function connect(args) {
		if (browserInfo().indexOf("Chrome") >= 0) {
			isChrome = true;
			if (!args.onNotInstalled) {
				args.onNotInstalled = function (response) {
					 if (response.instalationStatus == 1) {
						// Extensão não instalada
					 	window.location.href = 'http://icpbravoaccess.com.br/#/install?redirectUrl=' + getCurrentURL();
					} else if (response.instalationStatus == 2 || response.instalationStatus == 3) {
						//Native outdated ou desinstalado
						window.location.href = 'http://icpbravoaccess.com.br/#/install2?redirectUrl=' + getCurrentURL();
					} else if (response.instalationStatus == 4) {
					    //Javascript outdated
					    response.responseText = "IcpBravoAccess.ext.js está desatualizado";
					    args.onError(response);
						//console.log('IcpBravoAccess.ext.js outdated');
					}
				}
			}
			args.onErrorOld = args.onError;
			args.onError = function (response) {
				response.responseText = response.statusMessage;
				args.onErrorOld(response);
			}
			icpBravoAccessExt.connect(args);
		} else {
			isChrome = false;
			icpBravoAccess.connect(args);
		}
	}

	function getCertificateList(args) {
		if (isChrome) {
			if (args.icpbrasil) {				
				args.type = args.type ? args.type + "|icpbrasil" : 'icpbrasil';
			}
			args.filter = {
				type: args.type,
				cpf: args.cpf
			};
			args.onErrorOld = args.onError;
			args.onError = function (response) {
				response.responseText = response.statusMessage;
				args.onErrorOld(response);
			}

			// Copy object
			args.onSuccessOld = args.onSuccess;
			//jQuery.extend(true, args.onSuccessOld, args.onSuccess);
			args.onSuccess = function (response) {
				var certInfo = [];
				for (var i = 0; i < response.certificates.length; i++) {
					var current = response.certificates[i];
					certInfo.push({
						name: current.subjectName,
						responsavel: current.respName,
						cpf: current.cpf,
						cnpj: current.cnpj,
						notAfter: current.notAfter,
						notBefore: current.notBefore,
						thumbprint: current.thumbprint,
						issuerName: current.issuerName,
						type: current.mediaType,
						isValid: current.isValid,
						certType: current.certType
					});
				}
				response.certificatesInfos = certInfo;
				args.onSuccessOld(response);
			}
			icpBravoAccessExt.getCertificateList(args);
		} else {
			icpBravoAccess.getCertificateList(args);
		}
	}

	function getCertificate(args) {
		if (isChrome) {
			args.onErrorOld = args.onError;
			args.onError = function (response) {
				response.responseText = response.statusMessage;
				args.onErrorOld(response);
			}

			args.onSuccessOld = args.onSuccess;
			args.onSuccess = function (response) {
				response.certB64 = response.certificate;
				args.onSuccessOld(response);
			}
			icpBravoAccessExt.getCertificate(args);
		} else {
			icpBravoAccess.getCertificate(args);
		}
	}

	function sign(args) {
		if (isChrome) {
			args.onErrorOld = args.onError;
			args.onError = function (response) {
				response.responseText = response.statusMessage;
				args.onErrorOld(response);
			}

			args.onSuccessOld = args.onSuccess;
			args.onSuccess = function (response) {
				response.value = response.signature;
				args.onSuccessOld(response);
			}
			icpBravoAccessExt.sign(args);
		} else {
			icpBravoAccess.sign(args);
		}
	}

	function signWithBufferedKey(args) {
		if (isChrome) {
			sign(args);
		} else {
			icpBravoAccess.signWithBufferedKey(args);
		}
	}

	function getCurrentURL() {
		return (window.location.href);
	}

	function browserInfo() {
		var ua = navigator.userAgent, tem,
		M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
		if (/trident/i.test(M[1])) {
			tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
			return 'IE ' + (tem[1] || '');
		}
		if (M[1] === 'Chrome') {
			tem = ua.match(/\b(OPR|Edge)\/(\d+)/);
			if (tem != null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
		}
		M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
		if ((tem = ua.match(/version\/(\d+)/i)) != null) M.splice(1, 1, tem[1]);
		return M.join(' ');
	};

	return {
		connect: connect,
		getCertificateList: getCertificateList,
		getCertificate: getCertificate,
		sign: sign,
		signWithBufferedKey: signWithBufferedKey
	}
})();