const { Budget } = require('../budget.vm');

class HomeView {
    constructor(ctx) {
        this.router = ctx.router;
        this.budgets = ko.observableArray([]);

        // New category form
        this.newBudgetForm = ko.observable(false);
        this.form = {
            name: ko.observable('')
        };

        this.getBudgetsFromApi();
    }

    toggleNewBudgetForm () {
        this.newBudgetForm(!this.newBudgetForm());
    }

    getBudgetsFromApi () {
        const apiResponse = [
            {
                'id': 1,
                'name': 'First Budget'
            },
            {
                'id': 2,
                'name': 'Second Budget'
            }
        ];

        this.budgets(apiResponse
            .map(item => new Budget(item))
        );
    }

    addBudget () {
        const params = {
            'name': this.form.name()
        }
        const newBudget = new Budget(params);
        this.budgets.unshift(newBudget);

        this.resetNewBudgetForm();
    }

    resetNewBudgetForm () {
        this.newBudgetForm(false);
        this.form.name('');
    }

    removeBudget (budget) {
        this.budgets.remove(budget);
    }

    open (budget) {
        this.router.update(budget.url);
    }
}

ko.components.register('home-view', {
    viewModel: HomeView,
    template: require('html-loader!../../templates/views/home-view.html')
});
