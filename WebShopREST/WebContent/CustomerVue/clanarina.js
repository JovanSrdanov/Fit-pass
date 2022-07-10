Vue.component("clanarina", {
    data: function () {
        return {
            customer: { points: "" },
            typeOfCustomer: "bronze",
            staraClanarina: {},
            selectedBase: { code: "", price: 0, priceMultiplicator: 0 },
            baseMemberships: {},
            hasMembership: false,
            NeogranicenoCheck: false,
            dailyActivty: 5,
            promoCodeEnter: "",
            promoCodeMessage: "",
            promoCodeDiscount: -1,
            isVerified: false,
        };
    },
    template: ` 
  <div>
            <h1>Clanarine</h1>
            <p class="white">
                Tip kupca: {{translateType(typeOfCustomer)}}(ispravi jer je
                fixno) - Broj bodova: {{customer.points}}
            </p>

            <div class="CreateMembership">
               
                <table style="margin: auto">
                    <tr>
                        <td>
                            <div class="WrapTextEnterData">
                                <p>Unesite podatke:</p>
                                <p>Izabrali ste članarinu: {{selectedBase.code}}</p>

                                <p>
                                    Broj dnevnih aktivnosti:
                                    <input
                                        type="number"
                                        min="1"
                                        max="10"
                                        name="promoCode"
                                        id="promoCode"
                                        v-model="dailyActivty"
                                    />
                                </p>
                                <p
                                    style="
                                        display: flex;
                                        align-items: center;
                                        justify-content: center;
                                    "
                                >
                                    <input
                                        class="checkbox"
                                        type="checkbox"
                                        v-model="NeogranicenoCheck"
                                    />

                                    <label for="checkbox"
                                        >Neogranic broj aktivnosti</label
                                    >
                                </p>
                                <input
                                    type="text"
                                    name="promoCode"
                                    id="promoCode"
                                    v-model="promoCodeEnter"
                                    placeholder="Uneti promo kod"
                                />
                                <button>Verifikuj kod</button>
                                <p>Promo kod je: {{promoCodeMessage}}</p>
                                <p>Popust koji kod nudi: {{promoCodeDiscount}}</p>

                                <p>
                                    Konačna cena: {{priceComputed}}
                                    <button>Potvrdi članarinu</button>
                                </p>
                            </div >
                        </td>

                        <td>
                            <h3 class="white">Dostupne članarine</h3>
                            <table class="BaseMTable">
                                <th>Kod članarine</th>
                                <th>Tip članarine (broj dana važenja)</th>
                                <th>Početna cena</th>
                                <th>Multiplikator cene</th>

                                <tbody>
                                    <tr v-for="B in baseMemberships"  v-on:click="selectClanarinabase(B)"
                                    v-bind:class="{selectedBaseClass : selectedBase.id===B.id}">
                                        <td>{{B.code}}</td>
                                        <td>{{B.durationDays}}</td>
                                        <td>{{B.price}}</td>
                                        <td>{{B.priceMultiplicator}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>

            <div v-if="hasMembership" class="WrapTextClanarina">
                <h2>Stara clanarina</h2>
                <p>Kod članarine: {{staraClanarina.code}}</p>
                <p>
                    Datum kreiranja članarine:
                    {{getDate(staraClanarina.startDate)}}
                </p>
                <p>
                    Datum prestanka važenja: {{getDate(staraClanarina.endDate)}}
                </p>
                <p>Cena: {{staraClanarina.price}} RSD</p>
                <p>Status: {{convertStatus(staraClanarina.isActive)}}</p>
                <p>
                    Dnevni broj dozvoljenjih aktivnost :
                    {{staraClanarina.numberOfTrainings}}
                </p>
            </div>
        </div>
      
    
    `,
    mounted() {
        if (JSON.parse(localStorage.getItem("loggedInUser")) === null) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }
        if (
            JSON.parse(localStorage.getItem("loggedInUser")).role != "customer"
        ) {
            alert("Nemate pristup ovom sadržaju");
            window.location.href = "#/pocetna";
            return;
        }

        this.customer = JSON.parse(localStorage.getItem("loggedInUser"));

        yourConfig = {
            headers: {
                Authorization: localStorage.getItem("token"),
            },
        };

        if (this.customer.mebershipId != -1) {
            console.log("Ima clanarinu ", this.customer.mebershipId);

            this.hasMembership = true;

            axios
                .get("rest/membership/customer/" + this.customer.id, yourConfig)
                .then((result) => {
                    this.staraClanarina = result.data;
                });
        }

        axios.get("rest/membership/base").then((result) => {
            this.baseMemberships = result.data;
        });
    },

    computed: {
        priceComputed() {
            if (this.selectedBase.code === "") return 0;

            let forComputePromoCode = this.promoCodeDiscount;
            let forComputeDailyActivity = this.dailyActivty;
            let forComputePriceMulti = this.selectedBase.priceMultiplicator;
            let forStartPrice = this.selectedBase.price;

            if ((forComputePromoCode = -1)) {
                forComputePromoCode = 1;
            }
            if (this.NeogranicenoCheck) {
                dailiactivityCompute = 11;
            }
            if (forComputeDailyActivity == 1) {
                forComputePriceMulti = 1;
            }
            return Math.round(
                forComputePromoCode *
                    forComputeDailyActivity *
                    forComputePriceMulti *
                    forStartPrice
            );
        },
    },
    methods: {
        verifyPromoCode() {},
        CreateClanarina: function () {},
        convertStatus(status) {
            if (status) return "Aktivna";
            return "Nije aktivna";
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

        selectClanarinabase(base) {
            this.selectedBase = base;
        },
    },
});
