const paymentApi = {
    create: function (name, date, amount, categoryId) {
        return $.ajax({
            type: "POST",
            url: `/api/payments/create`,
            data: JSON.stringify({
                "title": name,
                "date": date,
                "amount": amount,
                "category": {
                    "id": categoryId
                }
            }),
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    },
    getByCategoryId: function (id) {
        return $.ajax({
            type: "GET",
            url: `/api/payments/${id}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    },
    delete: function (id) {
        return $.ajax({
            type: "DELETE",
            url: `/api/payments/${id}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
    }
};

module.exports = paymentApi;