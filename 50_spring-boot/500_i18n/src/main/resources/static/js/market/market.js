
$(document).ready(function(){
	loadProperties();
	$('#jstoshow').html($.i18n.prop('market'));
	alert(navigator.language);
})



 function loadProperties(){ 
	$.i18n.properties({
	    name: 'messages',
	    path: '/i18n/market/',
	    mode: 'map',
	    language: navigator.language||'zh_CN'
	});
 }


//localhost:8080/i18n/message.properties?_=1479992950356 404 (
//localhost:8080/i18n/message_zh.properties?_=1479992950357 40
//localhost:8080/i18n/message_zh_CN.properties?_=1479992950358