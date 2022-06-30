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
            <td>Korisničko ime</td>
            <td>Šifra</td>
            <td>Ime</td>
            <td>Prezime</td>
            <td>Pol</td>
            <td>Datum rođenja</td>
            <td>Uloga</td>
            <td>Tip korisnika</td>
            <td>Broj bodova</td>
            <td>Obriši</td>

            <tbody>
                <tr v-for="u in Users" >
                <td>{{u.username}}</td>
                <td>{{u.password}}</td>
                <td>{{u.name}}</td>
                <td>{{u.surname}}</td>
                <td>{{translateGender(u.gender)}}</td>
                <td>{{getDate(u.birthDate)}}</td>
                <td>{{getRole(u.role)}}</td>
                <td>{{getCustomerType(u)}}</td>
                <td>{{u.points}}</td>
                <td><button >Obriši</button></td>          
                </tr>
            </tbody>
        </table>
    </div>
    
   
 </div>
  `,

    mounted() {
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

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        ///
        axios.get("rest/customers/all", yourConfig).then((response) => {
            this.Users = response.data;
        });
    },
    methods: {
        translateGender: function (gender) {
            if (gender == "male") return "Muški";
            else return "Ženski";
        },
        getCustomerType: function (user) {
            if (user.customerType === null) return this.getRole(user.role);
            else return this.translateType(user.customerType.type);
        },
        translateType: function (type) {
            switch (type) {
                case "Bronze":
                    return "Bronzani";
                case "Silver":
                    return "Srebrni";
                case "Gold":
                    return "Zlatni";

                default:
                    return type;
            }
        },

        getDate: function (milliseconds) {
            const d = new Date();
            d.setTime(milliseconds);
            return d.toLocaleDateString("sr-RS");
        },
        getRole: function (role) {
            switch (role) {
                case "customer":
                    return "Kupac";
                case "admin":
                    return "Administrator";
                case "manager":
                    return "Menadžer";
                case "trainer":
                    return "Trener";
                default:
                    return role;
            }
        },
    },
});
