require('../scss/budget.scss');
const { Category } = require('./category.vm');

class Budget {
    constructor(params) {
        this.params = params;
        this.id = params.id;
        this.name = params.name;
        this.month = params.month;
        this.plannedAmount = params.plannedAmount;
        this.spentAmount = params.spentAmount;
        this.percSpent = ko.computed(() => parseInt(this.spentAmount ? this.spentAmount * 100 / this.plannedAmount : 0));

        this.url = `/budgets/${this.id}`;
    }

    open () {
        Router.update(this.url);
    }

    static fromApi(params) {
        return new Budget(params);
    }
}

ko.components.register('budget', {
    viewModel: Budget,
    template: require('html-loader!../templates/budget.html')
})

module.exports = { Budget };