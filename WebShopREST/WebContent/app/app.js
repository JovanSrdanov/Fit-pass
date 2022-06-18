const registracija = { template: "<registracija></registracija>" };
const prijava = { template: "<prijava></prijava>" };
const pocetna = { template: "<pocetna></pocetna>" };

const router = new VueRouter({
    mode: "hash",
    routes: [
        { path: "/pocetna", component: pocetna },
        { path: "/", component: pocetna },
        { path: "/registracija", component: registracija },
        { path: "/prijava", component: prijava },
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
