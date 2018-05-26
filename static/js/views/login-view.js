import * as auth from "../auth";

require('../../scss/style.scss');
const atob = require('atob');

class Login {
    constructor () {
        this.username = ko.observable('');
        this.password = ko.observable('');
    }

    login () {
        auth.login(this.username(), this.password());
    }
}

ko.components.register('login-view', {
    viewModel:  Login,
    template: require('html-loader!../../templates/views/login-view.html')
});
