import * as auth from "../auth";

const { Budget } = require('../budget.vm');

const d = new Date();

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

    logout () {
        auth.logout();
    }
}

class Overview {
    constructor () {
        this.monthlyIncome = '';
        this.monthlyPlanned = '';
        this.monthlySpent = '';
        this.monthlyDiff = '';

        this.editMonthly = ko.observable(false);
        this.setMonthly = ko.observable('');

        this.getIncome();
    }

    toggleEdit () {
        this.editMonthly(!this.editMonthly());
    }

    getMonthString () {
        const m = d.getMonth() + 1;
        const mStr = m < 10 ? '0' + m.toString() : m.toString();
        return mStr + '-' + d.getFullYear();
    }

    saveMonthly () {
        $.ajax({
            type: "POST",
            url: `/api/monthly-incomes/create`,
            data: JSON.stringify({
                "monthlyIncome": this.setMonthly(),
                "validForMonth": this.getMonthString(),
                "user": {
                    "id": sessionStorage.getItem('USER_ID')
                }
            }),
            contentType: "application/json"
        })
    }

    getIncome () {
        $.ajax({
            type: "GET",
            url: `/api/monthly-incomes/${sessionStorage.getItem('USER_ID')}/${this.getMonthString()}`,
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
            .then(res => {
                console.log(res)
            })
    }
}

ko.components.register('overview', {
    viewModel: Overview,
    template: require('html-loader!../../templates/components/overview.html')
})

ko.components.register('home-view', {
    viewModel: HomeView,
    template: require('html-loader!../../templates/views/home-view.html')
});
