var SignUpManager = function(options) {
    this.options = _.defaults(options, {
        signUpFormSelector: null,
    });

    this.signUpFormSelector = this.options.signUpFormSelector;

    this.initEvent();
}

SignUpManager.prototype.fetch = function() {
    var self = this;
    window.location.href = _contextPath + url;
}

SignUpManager.prototype.initEvent = function() {
    var self = this;

    self.buttonSelector.on('click', function(e) {
        self.fetch();
    });
}