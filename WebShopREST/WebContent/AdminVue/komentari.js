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
                        <td>{{c.customer.name}} {{c.customer.surname}}</td>
                        <td>{{c.facility.name}}</td>
                        <td>{{c.text}}</td>
                        <td>{{c.rating}}</td>            
                        <td><button>Odobri</button><button>Odbij</button></td>
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
    },
});
