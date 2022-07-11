const registracija = { template: "<registracija></registracija>" };
const prijava = { template: "<prijava></prijava>" };
const pocetna = { template: "<pocetna></pocetna>" };
const pregledObjekta = { template: "<pregledObjekta></pregledObjekta>" };
const pregledProfila = { template: "<pregledProfila></pregledProfila>" };
const loadCreateSP = { template: "<loadCreateSP></loadCreateSP>" };

const korisnici = { template: "<korisnici></korisnici>" };
const registruj = { template: "<registruj></registruj>" };
const napraviObjekat = { template: "<napraviObjekat></napraviObjekat>" };
const promoKod = { template: "<promoKod></promoKod>" };
const komentari = { template: "<komentari></komentari>" };
const zamenaMenagera = { template: "<zamenaMenagera></zamenaMenagera>" };

const dodajAktivnost = { template: "<dodajAktivnost></dodajAktivnost>" };
const treneriIkupci = { template: "<treneriIkupci></treneriIkupci>" };
const izmeniAktivnost = { template: "<izmeniAktivnost></izmeniAktivnost>" };
const prijaviKupcaNaAktivnost = {
    template: "<prijaviKupcaNaAktivnost></prijaviKupcaNaAktivnost>",
};
const istorijaAktivnostiUObjektu = {
    template: "<istorijaAktivnostiUObjektu></istorijaAktivnostiUObjektu>",
};

const zakazaniTreninziTrener = {
    template: "<zakazaniTreninziTrener></zakazaniTreninziTrener>",
};
const istorijaTreningaTrener = {
    template: "<istorijaTreningaTrener></istorijaTreningaTrener>",
};

const zakazaniTreninziKupac = {
    template: "<zakazaniTreninziKupac></zakazaniTreninziKupac>",
};
const istorijaTreningaKupac = {
    template: "<istorijaTreningaKupac></istorijaTreningaKupac>",
};

const clanarina = {
    template: "<clanarina></clanarina>",
};

const ostaviKomentar = {
    template: "<ostaviKomentar></ostaviKomentar>",
};
const zakaziTrening = {
    template: "<zakaziTrening></zakaziTrening>",
};

const router = new VueRouter({
    mode: "hash",

    routes: [
        {
            path: "/pregledObjekta/:id/zakaziTrening/:trainingId",
            component: zakaziTrening,
            props: true,
        },
        {
            path: "/pregledObjekta/:id/ostaviKomentar",
            component: ostaviKomentar,
            props: true,
        },
        { path: "/zamenaMenagera/:id", component: zamenaMenagera, props: true },
        { path: "/clanarina", component: clanarina, props: true },
        { path: "/prijava", component: prijava, props: true },
        { path: "/", component: pocetna, props: true },
        { path: "/komentari", component: komentari, props: true },
        { path: "/loadCreateSP", component: loadCreateSP, props: true },

        {
            path: "/istorijaTreningaTrener",
            component: istorijaTreningaTrener,
            props: true,
        },
        {
            path: "/zakazaniTreninziTrener",
            component: zakazaniTreninziTrener,
            props: true,
        },

        {
            path: "/zakazaniTreninziKupac",
            component: zakazaniTreninziKupac,
            props: true,
        },
        {
            path: "/istorijaTreningaKupac",
            component: istorijaTreningaKupac,
            props: true,
        },

        { path: "/pocetna", component: pocetna, props: true },
        { path: "/registracija", component: registracija, props: true },
        { path: "/korisnici", component: korisnici, props: true },
        { path: "/registruj", component: registruj, props: true },
        { path: "/pregledObjekta/:id", component: pregledObjekta, props: true },
        { path: "/promoKod", component: promoKod, props: true },
        {
            path: "/pregledObjekta/:id/istorijaAktivnostiUObjektu",
            component: istorijaAktivnostiUObjektu,
            props: true,
        },
        {
            path: "/pregledObjekta/:id/izmeniAktivnost/:activityId",
            component: izmeniAktivnost,
            props: true,
        },

        {
            path: "/pregledObjekta/:id/treneriIkupci",
            component: treneriIkupci,
            props: true,
        },

        {
            path: "/pregledObjekta/:id/prijaviKupcaNaAktivnost",
            component: prijaviKupcaNaAktivnost,
            props: true,
        },
        {
            path: "/pregledObjekta/:id/dodajAktivnost",
            component: dodajAktivnost,
            props: true,
        },
        { path: "/napraviObjekat", component: napraviObjekat, props: true },
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
        goToPersonalSP: function (id) {
            if (id === -1) {
                alert("Nemate još svoj objekat");
                return;
            }

            router.push(`/pregledObjekta/${id}`);
        },
        VarToken: function () {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };
            axios
                .get("rest/customer/info", yourConfig)
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
