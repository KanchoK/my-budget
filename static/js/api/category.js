const categoryApi = {
    create: function (name, plannedAmount, budgetId) {
        return $.ajax({
            type: "POST",
            url: `/api/categories/create`,
            data: JSON.stringify({
                "name": name,
                "plannedAmount": plannedAmount,
                "budget": {
                    "id": budgetId
                }
            }),
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    },
    getCategoriesByBudget: function (id) {
        return $.ajax({
            type: "GET",
            url: `/api/categories/${id}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    },
    deleteCategory: function (id) {
        return $.ajax({
            type: "DELETE",
            url: `/api/categories/${id}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    }
};

module.exports = categoryApi;