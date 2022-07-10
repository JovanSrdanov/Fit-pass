Vue.component("komentari", {
    data: function () {
        return {
            comments: {},
        };
    },
    template: `
    <div>
        <h1>Komentari</h1>
        <div class="tabelaKomentara">
            <table>
                <th>Kupac (URADI)</th>
                <th>Sportski objekat</th>
                <th>Tekst</th>
                <th>Ocena</th>
                <th>Odobri / Odbij</th>
           

                <tbody>
                    <tr v-for="c in comments">
                        <td>{{c.customerId}}</td>
                        <td>{{c.facilityId}}</td>
                        <td>{{c.commentText}}</td>
                        <td>{{c.rating}}</td>            
                        <td >
                        <button v-on:click="ApproveFunction(c.id)" class="Odobri" >Odobri</button>
                        &nbsp;
                        
                        <button class="Odbij"  v-on:click="RejectFunction(c.id)" >Odbij</button>            
                        </td>

                       
                    </tr>
                </tbody>
            </table>
        </div>        
    </div>      
  `,
    mounted() {
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert(
                "Nije vam dozvoljeno da vidite ovu stranicu jer niste ulogovani kao odgovarajuća uloga!"
            );
            window.location.href = "#/pocetna";
            return;
        }

        if (JSON.parse(localStorage.getItem("loggedInUser")).role !== "admin") {
            alert(
                "Nije vam dozvoljeno da vidite ovu stranicu jer ste ulogovani kao uloga koja nije odgovarajuća!"
            );
            window.location.href = "#/pocetna";
            return;
        }
        axios.get("rest/comment/all/", yourConfig).then((result) => {
            this.comments = result.data;
        });
    },
    methods: {
        ApproveFunction: function (id) {
            console.log("Usao u approve");
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };
            console.log("Id je " + id);
            axios
                .put("rest/comment/changeStatus/" + id, 0, yourConfig)
                .then((result) => {
                    alert("USPEO");
                })
                .catch((error) => {
                    console.log("greska je:");
                    console.log(error);
                });
        },
        RejectFunction: function (id) {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };

            axios
                .put("rest/comment/changeStatus/" + id, 2, yourConfig)
                .then((result) => {
                    axios
                        .get("rest/comment/all/", yourConfig)
                        .then((result) => {
                            this.comments = result.data;
                        });
                });
        },
    },
});
