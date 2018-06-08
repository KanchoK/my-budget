require('../../scss/overview.scss');
require('../../scss/budget.scss');
require('../../scss/home-view.scss');

const budgetApi = require('../api/budget');
const categoryApi = require('../api/category');
const paymentApi = require('../api/payment');

import * as auth from "../auth";
const { Budget } = require('../budget.vm');
const { Category } = require('../category.vm');
const { Payment } = require('../payment.vm');
const common = require('../common');

let overview = null;

class HomeView {
    constructor(ctx) {
        this.router = ctx.router;

        this.budgets = ko.observableArray([]);
        this.selectedBudget = ko.observable();
        this.copyBudgetDialog = ko.observable(false);
        this.oldBudgets = ko.observableArray();

        this.categories = ko.observableArray([]);
        this.selectedCategory = ko.observable();

        this.payments = ko.observableArray([]);
        this.selectedPayment = ko.observable();

        // New category form
        this.newBudgetForm = ko.observable(false);
        this.newCategoryForm = ko.observable(false);
        this.newPaymentForm = ko.observable(false);

        this.form = {
            name: ko.observable('May budget'),
            month: ko.observable(common.getMonthString()),
            plannedAmount: ko.observable(200),
        };

        this.categoryForm = {
            name: ko.observable('Category'),
            budget: ko.computed(() => this.selectedBudget() && this.selectedBudget().name),
            plannedAmount: ko.observable(50)
        };

        this.paymentForm = {
            name: ko.observable('Payment'),
            budget: ko.computed(() => this.selectedBudget() && this.selectedBudget().name),
            category: ko.computed(() => this.selectedCategory() && this.selectedCategory().name),
            date: ko.observable(common.getDate()),
            plannedAmount: ko.observable(50),
            description: ko.observable('')
        };

        this.getBudgetsFromApi();
    }

    toggleNewBudgetForm () {
        this.newBudgetForm(!this.newBudgetForm());
    }

    toggleNewCategoryForm () {
        this.newCategoryForm(!this.newCategoryForm());
    }

    toggleNewPaymentForm () {
        this.newPaymentForm(!this.newPaymentForm());
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
                this.selectedBudget(res[0]);
                categoryApi.getCategoriesByBudget(res[0].id)
                    .then(cats => {
                        this.categories(cats.map(c => Category.fromApi(c)));
                        this.selectedCategory(cats[0]);
                        paymentApi.getByCategoryId(cats[0].id)
                            .then(pays => {
                                this.payments(pays.map(p => Payment.fromApi(p)));
                                this.selectedPayment(pays[0]);
                            })
                    });
            })
    }

    promptCopyBudget () {
        budgetApi.getBudgetsByUserId(sessionStorage.getItem('USER_ID'))
            .then(res => {
                if (res) {
                    this.copyBudgetDialog(true);
                    this.oldBudgets(res
                        .filter(r => r.month !== common.getMonthString())
                        .map(r => new Budget(r)));
                }
            });
    }

    toggleCopyBudgetDialog () {
        this.copyBudgetDialog(!this.copyBudgetDialog());
    }

    copyBudget (budgetId) {
        budgetApi.copyBudget(sessionStorage.getItem('USER_ID'), common.getMonthString(), budgetId)
            .then(res => {
                const newBudget = new Budget(res);
                if (this.budgets().some(b => b.name === res.name)) {
                    throw "You already have a budget with this name!"
                }
                this.budgets.unshift(newBudget);
                this.selectBudget(newBudget);
                alert(`Budget ${res.name} copies successfully!`);
                this.toggleCopyBudgetDialog();
            })
            .catch(err => {
                this.toggleCopyBudgetDialog();
                alert(`Budget copy failed!\n${err}`);
            })
    }

    addBudget () {
        $.ajax({
            type: "POST",
            url: `/api/budgets/create`,
            data: JSON.stringify({
                "name": this.form.name(),
                "month": this.form.month(),
                "plannedAmount": this.form.plannedAmount(),
                "user": {
                    "id": sessionStorage.getItem('USER_ID')
                }
            }),
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
            .then(data => {
                const newBudget = new Budget(data);
                this.budgets.unshift(newBudget);
                this.selectBudget(newBudget);
                this.resetNewBudgetForm();

                overview.updateAll();
            })
    }

    addCategory () {
        categoryApi.create(this.categoryForm.name(), this.categoryForm.plannedAmount(), this.selectedBudget().id)
            .then(data => {
                const newCategory = Category.fromApi(data);
                this.categories.unshift(newCategory);
                this.selectCategory(newCategory);
                this.toggleNewCategoryForm();

                overview.updateAll();
            });
    }

    addPayment () {
        paymentApi.create(
            this.paymentForm.name(),
            this.paymentForm.date(),
            this.paymentForm.plannedAmount(),
            this.selectedCategory().id,
		    this.paymentForm.description()
        ).then(data => {
            this.payments.unshift(Payment.fromApi(data));
            this.toggleNewPaymentForm();

            this.updateSelectedBudget();
            this.updateSelectedCategory();

            overview.updateAll();
        });
    }

    updateSelectedBudget () {
        const id = this.selectedBudget().id;
        budgetApi.getById(id)
            .then(res => {
                this.budgets(this.budgets()
                    .map(b => b.id === id ? Budget.fromApi(res) : b));
            });
    }

    updateSelectedCategory () {
        const id = this.selectedCategory().id;
        categoryApi.getById(id)
            .then(res => {
                this.categories(this.categories()
                    .map(c => c.id === id ? Category.fromApi(res) : c));
            });
    }

    resetNewBudgetForm () {
        this.newBudgetForm(false);
        this.form.name('');
    }

    removeBudget (budget) {
        $.ajax({
            type: "DELETE",
            url: `/api/budgets/${budget.id}`,
            contentType: "application/json",
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            }
        })
            .then(() => {
                this.budgets.remove(budget);

                overview.updateAll();
            })

    }

    removeCategory (id) {
        categoryApi.deleteCategory(id)
            .then(() => {
                this.categories(this.categories().filter(c => c.id !== id));

                this.updateSelectedBudget();
                overview.updateAll();
            });
    }

    removePayment (id) {
        paymentApi.delete(id)
            .then(() => {
                this.payments(this.payments().filter(p => p.id !== id));

                this.updateSelectedBudget();
                this.updateSelectedCategory();
                overview.updateAll();
            });
    }

    selectBudget (budget) {
        categoryApi.getCategoriesByBudget(budget.id)
            .then(res => {
                this.selectedBudget(budget);
                this.categories(res.map(category => Category.fromApi(category)))
            });
    }

    selectCategory (category) {
        paymentApi.getByCategoryId(category.id)
            .then(res => {
                this.selectedCategory(category);
                this.payments(res.map(payment => Payment.fromApi(payment)))
            });
    }

    logout () {
        auth.logout();
    }
}

class Overview {
    constructor () {
        this.monthlyIncome = ko.observable('');
        this.monthlyPlanned = ko.observable('');
        this.monthlySpent = ko.observable('');
        this.monthlyDiff = ko.computed(() => this.monthlyPlanned() - this.monthlySpent());

        this.editMonthly = ko.observable(false);
        this.setMonthly = ko.observable('');
        this.monthlyIncomeId = ko.observable('');

        this.updateAll();

        overview = this;
    }

    updateAll () {
        this.getIncome();
        this.getPlanned();
        this.getSpent();
    }

    getPlanned () {
        $.ajax({
            type: "GET",
            url: `/api/budgets/plannedAmount/${sessionStorage.getItem('USER_ID')}/${common.getMonthString()}`,
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            },
            contentType: "application/json"
        })
            .then(data => {
                this.monthlyPlanned(data);
            })
    }

    getSpent () {
        $.ajax({
            type: "GET",
            url: `/api/budgets/spentAmount/${sessionStorage.getItem('USER_ID')}/${common.getMonthString()}`,
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('USER_SESSION_TOKEN')
            },
            contentType: "application/json"
        })
            .then(data => {
                this.monthlySpent(data);
            })
    }

    toggleEdit () {
        this.editMonthly(!this.editMonthly());
    }

    saveMonthly () {
        if (this.monthlyIncome() == undefined){
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
                .then(() => {
                    this.monthlyIncome(this.setMonthly());
                    this.toggleEdit();
                })
        } else {
            $.ajax({
                type: "PUT",
                url: `/api/monthly-incomes/update/` + JSON.stringify(this.monthlyIncomeId()),
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
                .then(() => {
                    this.monthlyIncome(this.setMonthly());
                    this.toggleEdit();
                })
        }    
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
                this.monthlyIncome(res.monthlyIncome);
                this.monthlyIncomeId(res.id);
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
