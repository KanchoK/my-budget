require('../scss/budget.scss');
const { Category } = require('./category.vm');

class Budget {
    constructor(params) {
        this.params = params;
        this.id = params.id;
        this.name = params.name;
        this.url = `/budgets/${this.id}`;
    }

    open () {
        Router.update(this.url);
    }
}

ko.components.register('budget', {
    viewModel: Budget,
    template: require('html-loader!../templates/budget.html')
})

module.exports = { Budget };