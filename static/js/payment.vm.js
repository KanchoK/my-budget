class Payment {
    constructor(params) {
        this.params = params;
        this.id = params.id;
        this.name = params.name;
        this.budget = params.budget;
        this.category = params.category;
        this.date = params.date;
        this.amount = params.amount;
        this.description = params.description;
    }

    static fromApi(data) {
        return new Payment({
            'id': data.id,
            'name': data.title,
            'budget': data.budget,
            'category': data.category.name,
            'date': data.date,
            'amount': data.amount,
            'description': data.description,
        });
    }

    static forApi(payment) {
        return {
            "id": payment.id,
            "name": payment.name,
            "description": payment.description,
            "amount": payment.amount,
        }
    }
}

const paymentApi = {
    getPayment (id) {
        // The API should return object structured
        // like the "fromApi" method expects
    },
    addPayment (payment, categoryId) {
        // We add a new Payment to the database
        // and we associate it with a database category
        // using the categoryId param as a foreign key

        // For example:
        return $.ajax({
            type: 'POST',
            url: `/restapi/payments/`,
            data: {
                "payment": Payment.forApi(payment),
                "categoryId": categoryId
            },
            contentType: "application/json",
            dataType: "json"
        });
    },
    deletePayment (id) {
        // Delete payment by id
    },
    editPayment (id, data) {
        // Edit payment with id using the data provided
    }
};

ko.components.register('payment', {
    viewModel: Payment,
    template: require('html-loader!../templates/payment.html')
});


module.exports = { Payment };