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
    mounted() {
        this.VarToken();
    },
    methods: {
        VarToken: function () {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };
            axios
                .get("rest/customers/info", yourConfig)
                .then((response) => {
                    this.loggedInUser = response.data;
                    this.status = "loggedIn";
                    this.typeUser = this.loggedInUser.role;
                })
                .catch((error) => {
                    this.loggedInUser = {};
                    this.status = "notLoggedIn";
                    this.typeUser = "anonymous";
                });
        },

        logout: function () {
            localStorage.removeItem("token");
            this.VarToken();
            window.location.href = "#/pocetna";
        },
    },
});
