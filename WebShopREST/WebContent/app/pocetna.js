Vue.component("pocetna", {
    data: function () {
        return {
            OpenFacilitys: false,
            SportFacility: null,
            name: "",
            facilityType: "",
            locationString: "",
            rating: 1,
            loggedInUser: {},
            facilityTypeFilter: "",
        };
    },
    template: `
   <div >

             
                <h1>Sportski objekti</h1>
    <div class="pretragaFiltriranjeSortiranje">
        <div class="optionsWrapper" >
            <p>Pretraga:</p>
            
                         <input
                         v-model="name"
                            type="text"
                            name="name"
                            id="name"
                            placeholder="Naziv objekta"
                        />
                        
                            <br/>
                            <br/>
                         <input
                         v-model="facilityType"
                            type="text"
                            name="facilityType"
                            id="facilityType"
                            placeholder="Tip objekta"
                        />
                            <br/>
                            <br/>
                         <input
                         v-model="locationString"
                            type="text"
                            name="locationString"
                            id="locationString"
                            placeholder="Grad/Država"
                        />
                            <br/>
                           
                             <p>Prosečna ocena:
                         <input
                            min="1" max="5"
                            v-model="rating"
                            type="number"
                            name="rating"
                            id="rating"/>
                         </p>
                             
                           
                        <button v-on:click="Search" >Pretraži</button>  
            
        </div>


            <div class="optionsWrapper" >
                 <p>Filtriranje:</p>
                         <input
                         v-model="facilityTypeFilter"
                            type="text"
                            name="facilityTypeFilter"
                            id="facilityTypeFilter"
                            placeholder="Tip objekta"
                        />

                     <div style=" display: flex;
                    align-items: center; justify-content: center;"">

                    <input type="checkbox" id="OtvoreniObjekti" name="OtvoreniObjekti"    v-model="OpenFacilitys">
                    <label for="OtvoreniObjekti">Otvoreni objekti</label><br>
                    
                    </div>
                     </div>

             <div class="optionsWrapper" >
             <p>Sortiranje: TBA</p>
             </div>
</div>


 <div class="prostorZatabelu">
            <table>
                <tbody>
                    <tr v-for="p in searchFilterSort" >
                        <td>
                            <img
                              
                                v-bind:src="getImgUrl(p.facility.logo)"
                                alt="LOGO"
                                height="200"
                                width="200"
                            />
                        </td>
                        <td>
                            <ul>
                                <li>Naziv: {{p.facility.name}}</li>
                                <li>Tip: {{p.facility.facilityType}}</li>
                                <li>Adresa: {{p.location.address}}</li>
                                <li>Prosečna ocena: {{p.facility.rating}}</li>
                                <li>Radno vreme: {{p.facility.workStart}} - {{p.facility.workEnd}} </li>
                                <div v-if="loggedInUser.role=='admin'">
                                <br/>
                                <li><button >Obriši</button></li>  
                                </div>  
                            </ul>
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
        
                
            
        </div>`,

    mounted() {
        axios.get("rest/facilitys").then((response) => {
            this.SportFacility = response.data;
            this.loggedInUser = JSON.parse(
                localStorage.getItem("loggedInUser")
            );
            if (
                this.loggedInUser === null ||
                this.loggedInUser.role != "admin"
            ) {
                this.loggedInUser = {};
                this.loggedInUser.role = null;
            }
        });
    },
    computed: {
        searchFilterSort() {
            if (!this.SportFacility) return null;
            return this.SportFacility.filter((sp) => {
                return (
                    sp.facility.facilityType
                        .toLowerCase()
                        .indexOf(this.facilityTypeFilter.toLowerCase()) > -1
                );
            });
        },
    },

    methods: {
        getImgUrl(slika) {
            return "FacilityLogo/" + slika;
        },

        Search() {
            const params = new URLSearchParams();
            params.append("name", this.name);
            params.append("facilityType", this.facilityType);
            params.append("locationString", this.locationString);
            params.append("rating", this.rating);
            axios
                .post("rest/facilitys/search", params, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                })
                .then((response) => {
                    this.SportFacility = response.data;
                })
                .catch((error) => {
                    alert("Greska u search metodi");
                });
        },
    },
});
