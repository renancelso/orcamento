/*0.7*/
var icpbravoaccess_ie = null;

var icpBravoAccessExt = (function () {
    var license = null;
    var angularScope = null;

    /*config*/
    var isLogEnable = false;

    //this js version
    var clientVersion = "0.7";
    //Minimal IE Extension supported version
    var IE_MIN_SUPPORTED = "0.7";

    var browser = {
        Default: 0,
        IE: 1,
        Chrome: 2,
        Firefox: 3
    }

    var appStatus = {
        BROWSER_NOT_SUPPORTED: 0,
        EXTENSION_NOT_INSTALLED: 1,
        NATIVE_NOT_INSTALLED: 2,
        NATIVE_OUTDATED: 3,
        JS_OUTDATED: 4,
        INSTALLED: 5
    }

    /*========================================================================================================================
	* Control Object
	*/

    var _control = new function () {

        var responseStatus = {
            Error: 0,
            Success: 1
        };

        //detect current browser version
        var browserInfo = (function () {
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
        })();

        /*
         * http://stackoverflow.com/questions/105034/create-guid-uuid-in-javascript
         * uuid format :  xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
         * ex          : (227211e5-57e0-072c-3515-8eff3a2c2e3f)
         */
        function generateSecureIdentifier() {
            function s4() {
                return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
            }
            return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
        }

        function build(content, command, requestId) {

            var message = {
                command: command,
                requestId: requestId,
                license: icpBravoAccessExt.license,
                domain: window.location.hostname,
                content: JSON.stringify(content),
                browser: getBrowser()
            }
            return message;
        }


        function getBrowser() {
            if (browserInfo.indexOf("Chrome") >= 0) {
                return browser.Chrome
            } else if (browserInfo.indexOf("Firefox") >= 0) {
                return browser.Firefox
            } else if (browserInfo.indexOf("IE") >= 0) {
                return browser.IE;
            }
        }

        switch (getBrowser()) {
            case browser.Chrome:
            case browser.Firefox:
                /*request dictionary, all requests are registered here.*/
                var requestPool = {};

                /*request actions events*/
                var requestEventName = 'com.scytl.icpbravoaccess.request';
                /*response action events*/
                var responseEventName = 'com.scytl.icpbravoaccess.response';

                /*request*/
                var requestCommand = function (callback, content, command) {

                    var uuid = generateSecureIdentifier();

                    /*create pool item request */
                    var requestPoolItem = {
                        callback: callback,
                        requestTime: new Date()
                    };

                    /*register in request poll a new request intent*/
                    requestPool[uuid] = requestPoolItem;

                    /*build command.*/
                    var requestData = (build(content, command, uuid));

                    log("Command: " + command + " uuid: " + uuid);

                    /*create and dispatch a custon event request*/
                    var customEvent = new CustomEvent(requestEventName, { 'detail': requestData });
                    //customEvent.initEvent(requestEventName);
                    document.dispatchEvent(customEvent);
                }

                function checkNativeApp(callback, requestData) {

                    var callbackHandle = _callbackRegister({
                        onSuccess: function (response) {
                            if (response.instalationStatus === appStatus.NATIVE_OUTDATED) {
                                callback._dispatchSuccess(response);
                                knockNative(callback, requestData);
                            } else {
                                callback._dispatchSuccess(response);
                            }
                        },
                        onError: function (response) {
                            if (response.instalationStatus === appStatus.NATIVE_NOT_INSTALLED) {
                                callback._dispatchSuccess(response);
                                knockNative(callback, requestData);
                            } else if (response.notInstalled) {/* to remove, just for backward compatibility */
                                response.instalationStatus = appStatus.NATIVE_NOT_INSTALLED;
                                callback._dispatchSuccess(response);
                                knockNative(callback, requestData);
                            } else {
                                callback._dispatchSuccess(response);
                            }
                        },
                    });

                    requestCommand(callbackHandle, requestData, actions.check);
                };

                var knockNative = function (callback, requestData) {

                    var callbackHandle = _callbackRegister({
                        onSuccess: function (response) {
                            if (response.instalationStatus === appStatus.NATIVE_OUTDATED) {
                                setTimeout(function () {
                                    knockNative(callback, requestData);
                                }, 1000);
                            } else {
                                callback._dispatchSuccess(response);
                            }
                        },
                        onError: function (response) {
                            setTimeout(function () {
                                knockNative(callback, requestData);
                            }, 1000);
                        },
                    });

                    requestCommand(callbackHandle, requestData, actions.check);
                };

                var checkExtension = function (callback, tries) {
                    log("Remaining tries: " + tries);

                    var div = document.getElementById("icpbravoaccess_loaded");
                    if (div === null) {
                        if (tries > 1) {
                            setTimeout(function () {
                                checkExtension(callback, (tries - 1));
                            }, 200);
                        } else {
                            callback._dispatchSuccess({
                                instalationStatus: appStatus.EXTENSION_NOT_INSTALLED
                            });
                        }
                        return;
                    }

                    var requestData = {
                        jsClientVersion: clientVersion                     
                    }

                    checkNativeApp(callback, requestData);
                };

                /* event listener to capture responses from extension */
                document.addEventListener(responseEventName, function (response) {
                    var message = response.detail;

                    var requestPoolItem = requestPool[message.requestId];

                    if (!requestPoolItem) {
                        /*error invalid request id               */
                    } else {
                        delete requestPool[message.requestId];

                        log("Response uuid: " + message.requestId);

                        if (message.statusCode === responseStatus.Error) {
                            requestPoolItem.callback._dispatchError(message);
                        } else if (message.statusCode === responseStatus.Success) {

                            /*parse received message to object*/
                            var parsedResponse = JSON.parse(message.content);

                            if (requestPoolItem.callback.onReceiveReponse) {
                                parsedResponse = requestPoolItem.callback.onReceiveReponse(parsedResponse);
                            }

                            requestPoolItem.callback._dispatchSuccess(parsedResponse);
                        }
                    }
                });
                break;
            case browser.IE:
                /*request dictionary, all requests are registered here.*/
                var requestPool = {};

                var checkExtension = function (callback, tries) {
                    log("Remaining tries: " + tries);

                    if (typeof (icpbravoaccess_ie) === "undefined" || icpbravoaccess_ie === null) {
                        if (tries > 1) {
                            setTimeout(function () {
                                checkExtension(callback, (tries - 1));
                            }, 200);
                        } else {
                            callback._dispatchSuccess({
                                instalationStatus: appStatus.EXTENSION_NOT_INSTALLED
                            });
                        }
                        return;
                    }

                    var requestData = {                      
                        jsClientVersion: clientVersion,
                        ieMinSupportedVersion: IE_MIN_SUPPORTED
                    }

                    checkApp(callback, requestData);
                };

                function checkApp(callback, requestData) {

                    var callbackHandle = _callbackRegister({
                        onSuccess: function (response) {
                            callback._dispatchSuccess(response);
                        },
                        onError: function (response) {
                            callback._dispatchSuccess(response);
                        },
                    });

                    requestCommand(callbackHandle, requestData, actions.check);
                };

                /*request*/
                var requestCommand = function (callback, content, command) {

                    var uuid = generateSecureIdentifier();

                    /*build command.*/
                    var requestData = (build(content, command, uuid));

                    log("Command: " + command + " uuid: " + uuid);

                    var jsonRequestData = JSON.stringify(requestData);
                    var result = icpbravoaccess_ie.request(jsonRequestData);

                    if (result) {

                        var requestPoolItem = {
                            callback: callback,
                            requestTime: new Date()
                        };


                        requestPool[uuid] = requestPoolItem;
                    }

                }

                var verifyRequest = function () {

                    if (typeof (icpbravoaccess_ie) !== "undefined" && icpbravoaccess_ie !== null) {

                        var completeRequestsJson = icpbravoaccess_ie.GetPool();
                        var completeRequestsObjs = JSON.parse(completeRequestsJson);


                        for (var i = 0; i < completeRequestsObjs.length; i++) {

                            var responseObject = completeRequestsObjs[i];
                            var response = JSON.parse(responseObject);

                            var requestPoolItem = requestPool[response.requestId];

                            if (!requestPoolItem) {
                                /*error invalid request id                    */
                            } else {
                                delete requestPool[response.requestId];

                                log("Response uuid: " + response.requestId);

                                if (response.statusCode === responseStatus.Error) {
                                    requestPoolItem.callback._dispatchError(response);
                                } else if (response.statusCode === responseStatus.Success) {

                                    /*parse received message to object*/
                                    var parsedResponse = JSON.parse(response.content);

                                    if (requestPoolItem.callback.onReceiveReponse) {
                                        parsedResponse = requestPoolItem.callback.onReceiveReponse(parsedResponse);
                                    }

                                    icpbravoaccess_ie.RemovePool(response.requestId);
                                    requestPoolItem.callback._dispatchSuccess(parsedResponse);
                                }
                            }
                        }
                    }

                    setTimeout(function () {
                        verifyRequest();
                    }, 300);
                }

                verifyRequest();
                break;
            default:
                var checkExtension = function (callback, tries) {
                    callback._dispatchSuccess({
                        instalationStatus: appStatus.BROWSER_NOT_SUPPORTED
                    });
                }
        }

        /*make methods accessible.*/
        this.requestCommand = requestCommand;
        this.checkExtension = checkExtension;
    }

    /*========================================================================================================================
	* Callback Object
	*/

    this.callbackHandle = function (angular) {
        /*success callback registered by app user*/
        this.onSuccess = new function () { };
        /*error callback registered by app user*/
        this.onError = new function () { };

        this.onReceiveReponse = null;
        this.angularScope = angular;

        /*treat and dispach requests with success*/
        this._dispatchSuccess = function (response) {
            this._safeApply(this.onSuccess, response);
        }

        /*treat and dispache requests with error */
        this._dispatchError = function (response) {
            this._safeApply(this.onError, response);
        }

        /*'Safe' $apply in Angular.JS*/
        this._safeApply = function (fn, response) {
            /*if the application uses angular.js, '_safeApply' method checks the current phase before executing your function. */
            if (icpBravoAccessExt.angularScope) {
                var phase = this.angularScope.$root.$$phase;
                if (phase == '$apply' || phase == '$digest') {
                    if (fn && (typeof (fn) === 'function')) {
                        fn(response);
                    }
                } else {
                    this.angularScope.$apply(
						 fn(response)
					);
                }
            } else {
                fn(response);
            }
        };
    }

    /*CallbackManager*/
    this._callbackRegister = function (args) {
        var ch = new callbackHandle(icpBravoAccessExt.angularScope);

        if (args.onSuccess) {
            ch.onSuccess = args.onSuccess;
        }

        if (args.onError) {
            ch.onError = args.onError;
        }

        if (args.onReceiveReponse) {
            ch.onReceiveReponse = args.onReceiveReponse;
        }

        return ch;
    }

    /*========================================================================================================================
	*  Action Objects
	*/

    var actions = {
        certificates: "certificates",
        certificate: "certificate",
        remove: "remove",
        sign: "sign",
        check: "check",
        decrypt: "decrypt",
        encrypt: "encrypt",
    }

    var onExtensionCheck = function (onInstalled, onNotInstalled, onError, response) {

        switch (response.instalationStatus) {

            case appStatus.BROWSER_NOT_SUPPORTED:
                response.statusMessage = "O navegador não tem suporte para esta extensão.";
                response.info = "Navegador não suportado";
                onNotInstalled(response);
                break;
            case appStatus.EXTENSION_NOT_INSTALLED:
                response.statusMessage = "A Extensão não está instalada. Instale para continuar.";
                response.info = "Extensão não Instalada";
                onNotInstalled(response);
                break;
            case appStatus.NATIVE_NOT_INSTALLED:
                response.statusMessage = "A aplicação nativa não está instalada. Faça o download da aplicação e instale para continuar.";
                response.info = "Aplicação não Instalada";
                response.downloadRequired = true;
                onNotInstalled(response);
                break;
            case appStatus.NATIVE_OUTDATED:
                response.statusMessage = "A aplicação nativa está desatualiziada. Faça o download da versão mais atual e instale para continuar.";
                response.info = "Atualização Disponível";
                response.downloadRequired = true;
                onNotInstalled(response);
                break;
            case appStatus.JS_OUTDATED:
                response.statusMessage = "O JavaScript Client está desatualizado. Entre em contato com o administrador do site.";
                response.info = "Erro";
                response.downloadRequired = false;
                onNotInstalled(response);
                break;
            case appStatus.INSTALLED:
                onInstalled();
                break;
            default:
                response.info = "Erro";
                response.downloadRequired = false;
                onError(response)
                break;
        }

    };

    var filterCollection = function (collection, callback) {
        var data = [];

        for (var i = 0; collection.length > i; i++) {
            if (callback(collection[i])) {
                data.push(collection[i]);
            }
        }

        return data;
    }

    var filterCertificates = function (filter, certificates) {

        if (!filter) {
            return certificates;
        }

        if (filter.cpf) {
            var cpfs = (filter.cpf.replace(/\./g, "").replace(/-/g, "")).split("|");
            certificates.certificates = filterCollection(certificates.certificates,
                function (it) {
                    for (cpfIndex in cpfs) {
                        if (it.cpf == cpfs[cpfIndex]) {
                            return true;
                        }
                    }
                    return false;
                }
            );
        }

        if (filter.type) {
            filter.type = filter.type.toLowerCase();

            if (filter.type.search("icpbrasil") >= 0) {
                certificates.certificates = filterCollection(certificates.certificates,
                    function (it) {
                        return it.certType === certType.ICPBrasil;
                    }
                );
            } else if (filter.type.search("unknown") >= 0) {
                certificates.certificates = filterCollection(certificates.certificates,
                    function (it) {
                        return it.certType === certType.Unknown;
                    }
                );
            }
        }

        if (filter.valid) {
            certificates.certificates = filterCollection(certificates.certificates,
                function (it) {
                      return it.isValid;;
                }
            );
        }

        return certificates;
    }

    /*
	* Certificates:
	* 
	*/
    this.certificates = function (args) {

        var reportResponse = validateCertificatesAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        var callbackHandle = _callbackRegister({
            onSuccess: args.onSuccess,
            onError: args.onError,
            onReceiveReponse: function (certificates) {
                return filterCertificates(args.filter, certificates);
            }
        });

        _control.requestCommand(callbackHandle, null, actions.certificates);
    }

    var certificate = function (args) {

        var reportResponse = validateCertificateAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        /*create a certificate callbackHandler*/
        var callbackHandle = _callbackRegister({
            onSuccess: args.onSuccess,
            onError: args.onError
        });

        /*mount certificate request data object*/
        var requestData = {
            thumbprint: args.thumbprint
        }

        _control.requestCommand(callbackHandle, requestData, actions.certificate);
    }

    var sign = function (args) {

        var reportResponse = validateSignAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        /*create a sign callbackHandler*/
        var callbackHandle = _callbackRegister({
            onSuccess: args.onSuccess,
            onError: args.onError
        });

        /*mount sign request data object*/
        var requestData = {
            thumbprint: args.thumbprint,
            toSign: args.toSign,
            signatureAlgorithm: args.signatureAlgorithm
        }

        _control.requestCommand(callbackHandle, requestData, actions.sign);
    }


    var encrypt = function (args) {
        var reportResponse = validateEncryptAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        /*create a encrypt callbackHandler*/
        var callbackHandle = _callbackRegister({
            onSuccess: args.onSuccess,
            onError: args.onError
        });

        /*mount encrypt request data object*/
        var requestData = {
            thumbprint: args.thumbprint,
            toEncrypt: args.toEncrypt,
            oaep: args.oaep
        }

        _control.requestCommand(callbackHandle, requestData, actions.encrypt);
    }

    var decrypt = function (args) {
        var reportResponse = validateDecryptAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        /*create a decrypt callbackHandler*/
        var callbackHandle = _callbackRegister({
            onSuccess: args.onSuccess,
            onError: args.onError
        });

        /*mount decrypt request data object*/
        var requestData = {
            thumbprint: args.thumbprint,
            toDecrypt: args.toDecrypt,
            oaep : args.oaep
        }

        _control.requestCommand(callbackHandle, requestData, actions.decrypt);
    }

    var remove = function (args) {

        var reportResponse = validateRemoveAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        /*create a sign callbackHandler*/
        var callbackHandle = _callbackRegister({
            onSuccess: args.onSuccess,
            onError: args.onError
        });

        /*mount sign request data object*/
        var requestData = {
            thumbprint: args.thumbprint
        }

        _control.requestCommand(callbackHandle, requestData, actions.remove);
    }

    var connect = function (args) {
        var reportResponse = validateConnectAttrs(args);
        if (reportResponse) {
            args.onError(reportResponse);
            return;
        }

        if (args.license) {
            icpBravoAccessExt.license = args.license;
        }

        if (args.angular) {
            icpBravoAccessExt.angularScope = args.angular;
        }

        var callbackHandle = _callbackRegister({
            onSuccess: function (response) {
                onExtensionCheck(args.onSuccess, args.onNotInstalled, args.onError, response)
            },
            onError: function (response) {
                args.onError(response);
            },
        });

        /*run function to check if extension is already installed*/
        _control.checkExtension(callbackHandle, 25);
    }

    function attrFail(attr, method) {
        var response = {
            statusMessage: "Não foi possível localizar o parâmetro : [" + attr + "] na chamada ao método : " + method,
        }

        return response;
    }

    function validateCertificateAttrs(args) {
        if (!args.thumbprint) {
            return attrFail("thumbprint", "certificate");
        }

        if (!args.onSuccess) {
            return attrFail("onSuccess", "certificate");
        }
    }

    function validateCertificatesAttrs(args) {
        if (!args.onSuccess) {
            return attrFail("onSuccess", "certificates");
        }
    }

    function validateSignAttrs(args) {

        if (!args.thumbprint) {
            return attrFail("thumbprint", "sign");
        }

        if (!args.toSign) {
            return attrFail("toSign", "sign")
        }

        if (!args.onSuccess) {
            return attrFail("onSuccess", "sign");
        }
    }

    function validateDecryptAttrs(args) {
        if (!args.toDecrypt) {
            return attrFail("toDecrypt", "decrypt")
        }
        if (!args.thumbprint) {
            return attrFail("thumbprint", "decrypt");
        }
    }

    function validateEncryptAttrs(args) {
        if (!args.toEncrypt) {
            return attrFail("toEncrypt", "encrypt")
        }
        if (!args.thumbprint) {
            return attrFail("thumbprint", "encrypt");
        }
    }

    function validateRemoveAttrs(args) {
        if (!args.thumbprint) {
            return attrFail("thumbprint", "remove");
        }
    }

    function validateConnectAttrs(args) {
        if (!args.onNotInstalled) {
            return attrFail("onNotInstalled", "connect");
        }

        if (!args.onSuccess) {
            return attrFail("onSuccess", "connect");
        }
    }

    /*========================================================================================================================
    *  Util
    */

    var certType = {
        Unknown: 0,
        ICPBrasil: 1
    }

    /*logs*/
    function log(message) {
        if (isLogEnable) {
            console.log(message);
        }
    }

    /*avaliable actions*/
    return {
        /*commands*/
        connect: connect,
        getCertificateList: certificates,
        getCertificate: certificate,
        sign: sign,
        remove: remove,
        encrypt: encrypt,
        decrypt: decrypt,
        /*config*/
        enableLog: isLogEnable,
    }

})();