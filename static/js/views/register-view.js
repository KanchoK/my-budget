class Register {
    constructor () {
        this.username = ko.observable('');
        this.password = ko.observable('');
    }

    register () {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/my-budget/rest/users/registration",
            data: JSON.stringify({
                "username": this.username(),
                "password": this.password()
            }),
            contentType: "application/json"
        });
    }
}

ko.components.register('register-view', {
    viewModel:  Register,
    template: require('html-loader!../../templates/views/register-view.html')
});