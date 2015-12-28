var NativeDialog = Class(function () {

	this.registerHandler = function(cb) {
		NATIVE.events.registerHandler('nativedialog', cb);
	};

    this.showDialog = function(opts, cb) {
    	logger.log("{nativedialog} Open Dialog");
    	NATIVE.events.registerHandler('nativedialog', cb);
    	NATIVE.plugins.sendEvent("NativeDialogPlugin", "showDialog", JSON.stringify(opts));
    }
});

exports = new NativeDialog();
