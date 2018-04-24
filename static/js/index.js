window.ko = require('knockout');
window.$ = require('jquery');
const { Router } = require('ko-component-router');

Router.setConfig({
  base: '',
  hashbang: false
});

Router.useRoutes({
    '/': 'home-view',
    '/register': 'register-view',
    '/budgets/:id': 'budget-view',
    '/categories/:id/': 'category-view'
});

window.Router = Router;

require('./views/home-view');
require('./views/budget-view');
require('./views/category-view');
require('./views/register-view');

ko.applyBindings();