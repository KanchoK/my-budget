import * as auth from "../auth";

require('../../scss/style.scss');

class Register {
    constructor () {
        this.username = ko.observable('');
        this.password = ko.observable('');
        this.passwordConfirm = ko.observable('');
    }

    register () {
        if (this.password() === this.passwordConfirm() &&
            this.password().length > 3 &&
            this.username().length >= 5) {
            $.ajax({
                type: "POST",
                url: "/api/users/registration",
                data: JSON.stringify({
                    "username": this.username(),
                    "password": this.password()
                }),
                contentType: "application/json"
            })
                .then(() => auth.login(this.username(), this.password()))
                .catch(() => alert('Registration failed.'))
        } else {
            alert('Username should be at least 5 symbols. \n' +
                'Password and Confirm password fields should match and should be longer than 3 symbols.')
        }
    }
}

ko.components.register('register-view', {
    viewModel:  Register,
    template: require('html-loader!../../templates/views/register-view.html')
});
