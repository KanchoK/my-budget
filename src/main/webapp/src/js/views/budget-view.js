const { Category } = require('../category.vm');

class BudgetView {
    constructor(ctx) {
        this.categories = ko.observableArray([]);
        // New category form

        this.newCategoryForm = ko.observable(false);
        this.form = {
            name: ko.observable(''),
            planned: ko.observable(100)
        };

        this.getCategoriesFromApi();
    }

    getCategoriesFromApi () {
        const apiResponse = [
            {
                'id': 1,
                'name': 'Food',
                'moneyPlanned': 300
            },
            {
                'id': 2,
                'name': 'Drink',
                'moneyPlanned': 200
            }
        ];

        this.categories(apiResponse
           .map(item => Category.fromApi(item))
        );
    }

    toggleCategories () {
        this.viewCategories(!this.viewCategories());
    }

    toggleNewCategoryForm () {
        this.newCategoryForm(!this.newCategoryForm());
    }

    resetNewCategoryForm () {
        this.newCategoryForm(false);
        this.form.name('');
        this.form.planned(100);
    }

    addCategory () {
        const params = {
            'name': this.form.name(),
            'moneyPlanned': parseInt(this.form.planned(), 10)
        }
        const newCategory = new Category(params);
        this.categories.unshift(newCategory);

        this.resetNewCategoryForm();
    }

    removeCategory (category) {
        this.categories.remove(category);
    }
}

ko.components.register('budget-view', {
    viewModel: BudgetView,
    template: require('html-loader!../../templates/views/budget-view.html')
});
