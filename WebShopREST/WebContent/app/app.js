const registracija = { template: "<registracija></registracija>" };
const prijava = { template: "<prijava></prijava>" };
const pocetna = { template: "<pocetna></pocetna>" };
const korisnici = { template: "<korisnici></korisnici>" };
const pregledProfila = { template: "<pregledProfila></pregledProfila>" };
const registruj = { template: "<registruj></registruj>" };
const pregledObjekta = { template: "<pregledObjekta></pregledObjekta>" };

const router = new VueRouter({
    mode: "hash",

    routes: [
        { path: "/", component: pocetna, props: true },
        { path: "/pocetna", component: pocetna, props: true },
        { path: "/registracija", component: registracija, props: true },
        { path: "/prijava", component: prijava, props: true },
        { path: "/korisnici", component: korisnici, props: true },
        { path: "/registruj", component: registruj, props: true },
        { path: "/pregledObjekta/:id", component: pregledObjekta, props: true },
        {
            path: "/pregledProfila/:username",
            component: pregledProfila,
            props: true,
        },
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
        goToProfile: function (username) {
            router.push(`/pregledProfila/${username}`);
        },
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
                    localStorage.setItem(
                        "loggedInUser",
                        JSON.stringify(this.loggedInUser)
                    );
                    window.location.href = "#/pocetna";
                })
                .catch((error) => {
                    this.loggedInUser = {};
                    this.status = "notLoggedIn";
                    this.typeUser = "anonymous";
                    window.location.href = "#/pocetna";
                });
        },

        logout: function () {
            localStorage.removeItem("token");
            localStorage.removeItem("loggedInUser");
            this.VarToken();
            window.location.href = "#/prijava";
        },
    },
});
