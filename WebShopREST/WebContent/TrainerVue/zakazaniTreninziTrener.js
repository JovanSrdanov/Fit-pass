Vue.component("zakazaniTreninziTrener", {
    data: function () {
        return {
            appointments: {},
        };
    },
    template: `
        <div class="WholeScreen">
            <h1>Zakazani treninzi </h1>
            
                    <div class="TabelaKorisnika">
                        <table>
                            <th>Naziv treninga</th>
                            <th>Naziv objekta</th>             
                            <th>Datum</th>
                            <th>Vreme</th>
                         
                            <th>Kupac</th>
                        
                            <th>Otkaži</th>
                      
                            <tbody>
                                <tr
                                    v-for="u in appointments"
                                >
                                    <td>{{u.workoutName}}</td>
                                    <td>{{u.facilityName}}</td>
                                   
                                    <td>{{getDate(u.workoutDate)}}</td>
                                    <td>{{getTime(u.workoutDate)}}</td>

                                   
                                    <td>{{u.customerFullName}}</td>

                                    <td v-if="u.canceled" >Otkazan</td>
                                    <td v-if="!u.canceled">  <button v-on:click="OtkaziTrening(u.appointentId)" class="ObrisiDugme">Otkaži</button></td>
                                
                                </tr>
                            </tbody>
                        </table>
                    </div>
             
        </div>
  `,

    mounted() {
        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaja");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "trainer"
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

        axios.get("rest/workout/appointments", yourConfig).then((result) => {
            this.appointments = result.data;
        });
    },
    methods: {
        OtkaziTrening: function (appointmentId) {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            };
            console.log(yourConfig);
            axios
                .put("rest/workout/cancel/" + appointmentId, "", yourConfig)
                .then((result) => {
                    alert("Otkazan trening");
                    /*
                    axios
                        .get("rest/workout/appointments", yourConfig)
                        .then((result) => {
                            this.appointments = result.data;
                        });
                        */
                })
                .catch((err) => {
                    alert("Ne moze da se otkaze dva dana pre treninga");
                });
        },
        ConvertZakazan: function (Z) {
            if (!Z) return "Zakazan";
            return "Otkazan";
        },
        getPrice: function (price) {
            if (price == -1) return "Nema doplate";
            return price + " RSD";
        },
        TypeConvert: function (type) {
            if (type === "personal") return "Personalni trening";
            if (type === "group") return "Grupni trening";
            if (type === "solo") return "Aktivnost bez trenera";

            return "Greska";
        },
        getDate: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleDateString("sr-RS");
        },

        getTime: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleTimeString("sr-RS");
        },
    },
});
