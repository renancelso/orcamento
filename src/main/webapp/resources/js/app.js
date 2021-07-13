function showDialog(widgetVar){
	console.log("widgetVar: " + widgetVar);
	PF(widgetVar).show();
}
function hide(widgetVar){
	PF(widgetVar).hide();
} 