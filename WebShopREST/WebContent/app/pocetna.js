Vue.component("pocetna", {
    data: function () {
        return {
            SportFacility: {},
            name: "",
            facilityType: "",
            locationString: "",
            rating: 0,
        };
    },
    template: `
   <div >

             
                <h1>Sportski objekti </h1>
    <div class="pretragaFiltriranjeSortiranje">
        <h2>Opcije</h2>
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


         <br/>
         <br/>
         <br/>
           <br/>
         <br/>
         <br/>
        <p>Filtriranje: TBA</p>
        <p>Sortiranje: TBA</p>

</div>


 <div class="prostorZatabelu">
            <table>
                <tbody>
                    <tr v-for="p in SportFacility" >
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
        });
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
                .post("rest/facility/search", params, {
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
