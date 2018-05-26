require('../../scss/overview.scss');

import * as auth from "../auth";
const { Budget } = require('../budget.vm');
const common = require('../common');

class HomeView {
    constructor(ctx) {
        this.router = ctx.router;
        this.budgets = ko.observableArray([]);

        // New category form
        this.newBudgetForm = ko.observable(false);
        this.form = {
            name: ko.observable(''),
            month: ko.observable(''),
            plannedAmount: ko.observable(''),
        };

        this.getBudgetsFromApi();
    }

    toggleNewBudgetForm () {
        this.newBudgetForm(!this.newBudgetForm());
    }

    getBudgetsFromApi () {
        $.ajax({
            type: "GET",
            url: `/api/budgets/${sessionStorage.getItem('USER_ID')}/${common.getMonthString()}`,
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
            .then(res => {
                res && this.budgets(res.map(item => new Budget(item)));
            })
    }

    addBudget () {
        const params = {
            'name': this.form.name(),
            'month': this.form.month(),
            'plannedAmount': this.form.plannedAmount()
        };

        $.ajax({
            type: "POST",
            url: `/api/budgets/create`,
            data: JSON.stringify({
                "name": this.form.name(),
                "validForMonth": this.form.month(),
                "user": {
                    "id": sessionStorage.getItem('USER_ID')
                }
            }),
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
        .then(() => {
            const newBudget = new Budget(params);
            this.budgets.unshift(newBudget);
            this.resetNewBudgetForm();
        })
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

    saveMonthly () {
        $.ajax({
            type: "POST",
            url: `/api/monthly-incomes/create`,
            data: JSON.stringify({
                "monthlyIncome": this.setMonthly(),
                "validForMonth": common.getMonthString(),
                "user": {
                    "id": sessionStorage.getItem('USER_ID')
                }
            }),
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            },
            contentType: "application/json"
        })
    }

    getIncome () {
        $.ajax({
            type: "GET",
            url: `/api/monthly-incomes/${sessionStorage.getItem('USER_ID')}/${common.getMonthString()}`,
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
