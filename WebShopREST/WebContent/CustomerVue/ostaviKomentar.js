Vue.component("ostaviKomentar", {
    data: function () {
        return {
            staraClanarina: {},
            selectedBase: {},
        };
    },
    template: `
    <div>
                <h1>Ostavi komentar</h1>
           
    </div>
    `,
    mounted() {
        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "customer"
        ) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };
    },

    methods: {
        CreateClanarina: function () {},
        selectClanarinabase(base) {
            this.selectedBase = base;
        },
    },
});
