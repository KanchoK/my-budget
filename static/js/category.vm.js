const { Payment } = require('./payment.vm');

class Category {
    constructor(params) {
        this.params = params;
        this.id = params.id;
        this.name = params.name;
        this.budget = params.budget;
        this.plannedAmount = params.plannedAmount;
        this.spentAmount = params.spentAmount;

        this.difference = ko.computed(
            () => parseInt(this.spentAmount ? this.spentAmount*100 / this.plannedAmount : 0) + '%'
        )
    }

    static fromApi(data) {
        return new Category({
            id: data.id,
            name: data.name,
            plannedAmount: data.plannedAmount,
            spentAmount: data.spentAmount
        });
    }

    static forApi(category) {
        return {
            "id": category.id,
            "name": category.name,
            "plannedAmount": category.plannedAmount(),
            "spentAmount": category.spentAmount()
        }
    }
}

ko.components.register('category', {
    viewModel: Category,
    template: require('html-loader!../templates/category.html')
});

module.exports = { Category };