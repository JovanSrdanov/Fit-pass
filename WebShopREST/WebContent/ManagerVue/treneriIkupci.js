Vue.component("treneriIkupci", {
    data: function () {
        return {
            customers: {},
            trainers: {},
            facId: null,
        };
    },
    template: ` 
        <div class="WholeScreen">
            <h1>Treneri i kupci</h1>
            <table class="tableForPocetna">
                <td>
                    <h2 class="white">
                        Treneri koji drže treninge u mom objektu:
                    </h2>
                    <div class="treneriIkupci">
                        <table>
                            <th>Ime</th>
                            <th>Prezime</th>
                            <th>Korisničko ime</th>
                            <tbody>
                                <tr v-for="T in trainers">
                                    <td>{{T.name}}</td>
                                    <td>{{T.surname}}</td>
                                    <td>{{T.username}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
                <td>
                    <h2 class="white">Kupci koji su posetili moj objekat:</h2>
                    <div class="treneriIkupci">
                        <table>
                            <th>Ime</th>
                            <th>Prezime</th>
                            <th>Korisničko ime</th>
                            <tbody>
                                <tr v-for="T in customers">
                                    <td>{{T.name}}</td>
                                    <td>{{T.surname}}</td>
                                    <td>{{T.username}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
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
            .get("rest/customer/visited/" + this.facId, yourConfig)
            .then((result) => {
                this.customers = result.data;
            });
        axios
            .get("rest/trainer/dig/" + this.facId, yourConfig)
            .then((result) => {
                this.trainers = result.data;
            });
    },
    methods: {},
});
