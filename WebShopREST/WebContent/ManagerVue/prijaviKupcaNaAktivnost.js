Vue.component("prijaviKupcaNaAktivnost", {
    data: function () {
        return {
            facId: null,

            allCustomers: {},
            allActivitys: {},

            customerUsername: "",
            selectedActivity: { workout: { id: "" } },

            enterAllData: "",
            errorMessage: "",
        };
    },
    template: `
            <div class="WholeScreen">
                <h1>Prijavi kupca na aktivnost</h1>
                <table  class="tableForPocetna">
                    <tr>
                        <td>
                    
                            <input
                                v-model="customerUsername"
                                type="text"
                                name="customerUsername"
                                id="customerUsername"
                                placeholder="Korisničko ime"
                            />
                           
                             <button v-on:click="CheckInUser" >Prijavi kupca</button>
                          
                            <p  class="white">{{enterAllData}}{{errorMessage}}</p>     
                                           
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h2  class="white">Aktivnosti:</h2>
                             <div class="treneriIkupci" height="300">
                                    <table>
                                    <th>Naziv</th>
                                    <th>Tip</th>
                                    <th>Trajanje</th>
                                    <tbody>
                                        <tr v-for="A in allActivitys" v-on:click="selectA(A)"
                                         v-bind:class="{SelectedActivityClass : selectedActivity.workout.id===A.workout.id}"
                                        >
                                            <td>{{A.workout.name}}</td>
                                            <td>{{TypeConvert(A.workout.workoutType)}}</td>
                                            <td>{{A.workout.durationInMinutes}}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
               

               
        </div>  
  `,
    mounted() {
        this.facId = this.$route.params.id;

        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "manager"
        ) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).facilityId !=
            this.facId
        ) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };
        axios
            .get("rest/workout/inFacility/" + this.facId, yourConfig)
            .then((response) => {
                this.allActivitys = response.data;
            });
    },
    methods: {
        TypeConvert: function (type) {
            if (type === "personal") return "Personalni trening";
            if (type === "group") return "Grupni trening";
            if (type === "solo") return "Aktivnost bez trenera";

            return "Greska";
        },
        CheckInUser: function () {
            this.enterAllData = "";
            this.errorMessage = "";
            if (
                this.customerUsername === "" ||
                this.selectedActivity.workout.id === ""
            ) {
                this.enterAllData = "Unesite sve podatke";
                return;
            }

            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/x-www-form-urlencoded",
                },
            };
            const params = new URLSearchParams();
            params.append("customerUsername", this.customerUsername);
            params.append("workoutId", this.selectedActivity.workout.id);

            axios
                .post("rest/membership/check-in", params, yourConfig)
                .then((response) => {
                    window.location.href = "#/pregledObjekta/" + this.facId;
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        selectA: function (A) {
            console.log(A);
            this.selectedActivity = A;
        },
    },
});
