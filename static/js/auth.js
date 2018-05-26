const auth = {
    logout: function () {
        sessionStorage.removeItem('USER_SESSION_TOKEN');
        sessionStorage.removeItem('USER_ID');
        location.href = '/login';
    },
    login: function (username, password) {
        $.ajax({
            type: "POST",
            url: "/api/auth/login",
            data: JSON.stringify({
                "username": username,
                "password": password
            }),
            contentType: "application/json"
        })
            .then(res => {
                sessionStorage.setItem('USER_SESSION_TOKEN', res.message);
                const userId = JSON.parse(atob(res.message.split('.')[1])).sub;
                sessionStorage.setItem('USER_ID', userId);
                location.href = '/';
            })
            .catch(() => alert('Login failed.'))
    }
};

module.exports = auth;

