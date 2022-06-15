const registration = { template: "<registration></registration>" };
const login = { template: "<login></login>" };

const router = new VueRouter({
    mode: "hash",
    routes: [
        { path: "/", component: login },
        { path: "/registration", component: registration },
        { path: "/login", component: login },
    ],
});
var app = new Vue({
    router,
    el: "#firstPage",
    data: {
        status: "notLoggedIn",
        typeUser: "anonymous",
        loggedInUser: {},
    },
});
