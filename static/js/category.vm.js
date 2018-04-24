const { Payment } = require('./payment.vm');

class Category {
    constructor(params) {
        this.params = params;
        this.id = params.id;
        this.name = params.name;
        this.moneyPlanned = ko.observable(params.moneyPlanned);
        this.moneySpent = ko.observable(0);
        this.url = `/categories/${this.id}/`

        this.difference = ko.computed(
            () => this.moneyPlanned() - this.moneySpent()
        )
    }

    static fromApi(data) {
        return new Category({
            'id': data.id,
            'name': data.name,
            'moneyPlanned': data.moneyPlanned,
            'moneySpent': data.moneySpent
        });
    }

    static forApi(category) {
        return {
            "id": category.id,
            "name": category.name,
            "moneyPlanned": category.moneyPlanned(),
            "moneySpent": category.moneySpent()
        }
    }

}

ko.components.register('category', {
    viewModel: Category,
    template: require('html-loader!../templates/category.html')
});

module.exports = { Category };