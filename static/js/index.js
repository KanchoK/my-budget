window.ko = require('knockout');
window.$ = require('jquery');
const { Router } = require('ko-component-router');

Router.setConfig({
  base: '',
  hashbang: false
});

const requiresLogin = function () {
    if (!sessionStorage.getItem('USER_SESSION_TOKEN')) {
        location.href = '/login'
    }
};

Router.useRoutes({
    '/': [requiresLogin, 'home-view'],
    '/register': 'register-view',
    '/login': 'login-view',
    '/profile': [requiresLogin, 'profile-view'],
    '/budgets/:id': [requiresLogin, 'budget-view'],
    '/categories/:id/': [requiresLogin, 'category-view']
});

window.Router = Router;

require('./views/home-view');
require('./views/budget-view');
require('./views/category-view');
require('./views/register-view');
require('./views/login-view');
require('./views/profile-view');

ko.applyBindings();