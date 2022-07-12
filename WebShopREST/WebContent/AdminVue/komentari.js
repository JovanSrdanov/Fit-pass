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
                <th>Kupac</th>
                <th>Sportski objekat</th>
                <th>Tekst</th>
                <th>Ocena</th>
                <th>Odobri / Odbij</th>
           

                <tbody>
                    <tr v-for="c in comments">
                        <td>{{c.username}}</td>
                        <td>{{c.facilityName}}</td>
                        <td>{{c.comment.commentText}}</td>
                        <td>{{c.comment.rating}}</td>            
                        <td >
                        <button v-on:click="ApproveFunction(c.comment.id)" class="Odobri" >Odobri</button>
                        &nbsp;
                        
                        <button class="Odbij"  v-on:click="RejectFunction(c.comment.id)" >Odbij</button>            
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
        axios.get("rest/comment/allWaiting/", yourConfig).then((result) => {
            this.comments = result.data;
        });
    },
    methods: {
        ApproveFunction: function (id) {
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };
            axios
                .put("rest/comment/changeStatus/" + id, 0, yourConfig)
                .then((result) => {
                    axios
                        .get("rest/comment/allWaiting/", yourConfig)
                        .then((result) => {
                            this.comments = result.data;
                        });
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
                        .get("rest/comment/allWaiting/", yourConfig)
                        .then((result) => {
                            this.comments = result.data;
                        });
                });
        },
    },
});
