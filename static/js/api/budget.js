const budgetsApi = {
    getById: function (id) {
        return $.ajax({
            type: "POST",
            url: `/api/budgets`,
            data: JSON.stringify({
                "id": id
            }),
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    }
};

module.exports = budgetsApi;
