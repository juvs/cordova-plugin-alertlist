// window.plugins.alertlist

function AlertList() {
}

AlertList.prototype.showList = function(callback, title, data, selected) {
	cordova.exec(callback, null, "AlertList", "alertlist", [title, data, selected]);
};

cordova.addConstructor(function()  {
	if(!window.plugins) {
	   window.plugins = {};
	}

   // shim to work in 1.5 and 1.6
   if (!window.Cordova) {
	   window.Cordova = cordova;
   };

   window.plugins.alertList = new AlertList();
});