var parse = Class(function () {

	this.registerHandler = function(cb) {
		NATIVE.events.registerHandler('parseevent', cb);
	};

    this.sendTestEvent = function() {
    	console.log("Tiendv parse already there")
    	NATIVE.plugins.sendEvent("ParsePlugin", "sendTestEvent", JSON.stringify({}));
    };

    this.reloadData = function(opts) {
    	console.log("Tiendv parse already there")
    	NATIVE.plugins.sendEvent("ParsePlugin", "reloadData", JSON.stringify(opts));    	
    }
});

exports = new parse();

