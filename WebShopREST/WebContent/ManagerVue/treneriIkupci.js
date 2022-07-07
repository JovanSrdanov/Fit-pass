Vue.component("treneriIkupci", {
    data: function () {
        return {
            customers: {},
            trainers: {},
            facId: null,
        };
    },
    template: `
    <div>
        <h1>Treneri i kupci</h1>

        <div  class="showTrainers">
            <p>Treneri:</p>
            <table>
                
                <td>Ime</td>
                <td>Prezime</td>
                <td>Korisničko ime</td>
                <tbody>       
                    <tr v-for="T in trainers"  v-on:click="selectT(T)">                
                        <td>{{T.name}}</td>
                        <td>{{T.surname}}</td>
                        <td>{{T.username}}</td>                            
                    </tr>
                </tbody>
            </table>
        </div>

            <div  class="showCustomers">
                <p>Kupci:</p>
                <table>
                <td>Ime</td>
                <td>Prezime</td>
                <td>Korisničko ime</td>
                <tbody>       
                    <tr v-for="T in customers"  v-on:click="selectT(T)">                       
                        <td>{{T.name}}</td>
                        <td>{{T.surname}}</td>
                        <td>{{T.username}}</td>                            
                    </tr>
                </tbody>
            </table>
        </div>


    
        
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
        axios.get("rest/customers/", yourConfig).then((result) => {
            this.customers = result.data;
        });
        axios.get("rest/trainers/", yourConfig).then((result) => {
            this.trainers = result.data;
        });
    },
    methods: {},
});
