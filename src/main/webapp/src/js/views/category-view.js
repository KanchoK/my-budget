const { Payment } = require('../payment.vm');

class CategoryView {
    constructor(ctx) {
        this.ctx = ctx;
        this.categoryId = ctx.params.id;

        this.payments = ko.observableArray([]);

        // New category form
        this.viewPayments = ko.observable(false);
        this.newPaymentForm = ko.observable(false);
        this.form = {
            name: ko.observable(''),
            description: ko.observable(''),
            amount: ko.observable('')
        };

        this.getPaymentsFromApi();
    }

    getPaymentsFromApi () {
        const apiResponse = [
            {
                'id': 1,
                'name': 'dinner',
                'description': 'Steak and beer.',
                'amount': 5
            },
            {
                'id': 2,
                'name': 'breakfast',
                'description': 'Eggs and bread.',
                'amount': 10
            }
        ];

        this.payments(apiResponse
            .map(item => Payment.fromApi(item))
        );
    }

    togglePayments () {
        this.viewPayments(!this.viewPayments());
    }

    toggleNewPaymentForm () {
        this.newPaymentForm(!this.newPaymentForm());
    }

    resetNewPaymentForm () {
        this.newPaymentForm(false);
        this.form.name('');
        this.form.description('');
        this.form.amount('');
    }

    addPayment () {
        const params = {
            'name': this.form.name(),
            'description': this.form.description(),
            'amount': parseFloat(this.form.amount(), 10),

        }
        const newPayment = new Payment(params);
        this.payments.unshift(newPayment);
        this.resetNewPaymentForm();
    }

    removePayment (payment) {
        this.payments.remove(payment)
    }
}

ko.components.register('category-view', {
    viewModel: CategoryView,
    template: require('html-loader!../../templates/views/category-view.html')
});