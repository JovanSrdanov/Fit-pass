Vue.component("korisnici", {
    data: function () {
        return {
            Users: {},
        };
    },
    template: `
     
 <div>
   <h1>Korisnici</h1>
    <div class="TabelaKorisnika">
            <table>
              <td>Korisnicko ime</td>
              <td>Sifra</td>
              <td>Ime</td>
              <td>Prezime</td>
              <td>Pol</td>
              <td>Datum rodjenja:</td>
              <td>Uloga</td>
              <td>Tip korisnika</td>
              <td>Broj bodova</td>
            <td>Obrisi</td>

                <tbody>
                    <tr v-for="u in Users" >
                    <td>{{u.username}}</td>
                        <td>{{u.password}}</td>
                        <td>{{u.name}}</td>
                        <td>{{u.surname}}</td>
                        <td>{{u.gender}}</td>
                            <td>{{getDate(u.birthDate)}}</td>
                            <td>{{u.role}}</td>
                             <td>TBA</td>
                             <td>{{u.points}}</td>
                            <td><button >Obrisi</button></td>
                           
                             
                    </tr>
                </tbody>
            </table>
        </div>
        
   
 </div>
  `,

    mounted() {
        ///
        var a = JSON.parse(localStorage.getItem("loggedInUser"));
        if (a === null || a.role != "admin") {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
        }
        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        ///
        axios.get("rest/customers", yourConfig).then((response) => {
            this.Users = response.data;
        });
    },
    methods: {
        getDate: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleDateString("sr-RS");
        },
    },
});
