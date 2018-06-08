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
    },
    getBudgetsByUserId: function (id) {
        return $.ajax({
            type: "GET",
            url: `/api/budgets/${id}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    },
    copyBudget: function (userId, month, budgetId) {
        return $.ajax({
            type: "POST",
            url: `/api/copyBudget/${userId}/${month}/${budgetId}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        });
    }
};

module.exports = budgetsApi;
