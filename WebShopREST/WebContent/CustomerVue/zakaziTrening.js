Vue.component("zakaziTrening", {
    data: function () {
        return {
            Customer: {},

            dateTimeSelected: "",
            trainingId: 0,
            errorMessage: "",
        };
    },
    template: `    
         <div>
            <h1>Zakaži trening</h1>
            <p class="white">Izaberite datum i vreme:</p>
            <input type="datetime-local" v-model="dateTimeSelected" />

            <p><button v-on:click="MakeApp">Zakaži</button></p>
            <p class="white">{{errorMessage}}</p>
        </div>
    
    `,
    mounted() {
        this.trainingId = this.$route.params.trainingId;
        this.facilityID = this.$route.params.id;

        let today = new Date();
        today.setMinutes(today.getMinutes() - today.getTimezoneOffset());
        today.setDate(today.getDate() + 5);
        this.dateTimeSelected = today.toISOString().slice(0, 16);

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
        this.Customer = JSON.parse(localStorage.getItem("loggedInUser"));
    },

    methods: {
        MakeApp: function () {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };

            axios
                .get(
                    "rest/workout/schedule",
                    { date: this.dateTimeSelected, workoutId: this.trainingId },
                    yourConfig
                )
                .then((result) => {
                    window.location.href =
                        "#/pregledObjekta/" + this.facilityID;
                })
                .catch((err) => {
                    alert("neka greska");
                });
        },
    },
});
