Vue.component("dodajAktivnost", {
    data: function () {
        return {
            facId: null,
            allEntered: "",
            alreadyExists: "",

            selectedTrainer: {},
            allTrainers: {},

            name: "",
            workoutType: "solo",
            durationInMinutes: null,
            description: "",
            picture: null,
            showPicture: null,
        };
    },
    template: `
      <div class="WholeScreen">
            <h1>Dodaj aktivnost</h1>
            <div
                v-if="workoutType=='personal' || workoutType=='group' "
                class="ChooseTrainer"
            >
                <table>
                    <th>Ime</th>
                    <th>Prezime</th>
                    <th>Korisničko ime</th>
                    <tbody>
                        <tr
                            v-for="T in allTrainers"
                            v-on:click="selectT(T)"
                            v-bind:class="{selectedTrainerClass : selectedTrainer.id===T.id}"
                        >
                            <td>{{T.name}}</td>
                            <td>{{T.surname}}</td>
                            <td>{{T.username}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <p class="white">Naziv:</p>
            <input
                type="text"
                name="name"
                id="name"
                placeholder="Naziv"
                v-model="name"
            />

            <p class="white">Tip aktivnosti</p>
            <select name="workoutType" id="workoutType" v-model="workoutType">
                <option value="personal">Personalni trening</option>
                <option value="group">Grupni trening</option>
                <option value="solo">Aktivnost bez trenera</option>
            </select>

            <p
                class="white"
                v-if="workoutType=='personal' || workoutType=='group' "
            >
                Trener: {{selectedTrainer.name}} {{selectedTrainer.surname}}
            </p>

            <p class="white">
                Trajanje u minutima:
                <input
                    min="1"
                    max="180"
                    v-model="durationInMinutes"
                    type="number"
                    name="durationInMinutes"
                    id="durationInMinutes"
                />
            </p>
            <p class="white">Opis:</p>
            <textarea
                rows="4"
                cols="50"
                name="description"
                id="description"
                v-model="description"
                placeholder="Opis.."
            />
            <p class="white">Slika aktivnosti:</p>
            <input
                type="file"
                name="picture"
                id="picture"
                accept="image/*"
                class="custom-file-input"
                @change="handleFileUpload"
            />
            <p>
                <img v-bind:src="showPicture" height="200px" width="200px" />
            </p>

            <button v-on:click="CreateActivity">Kreiraj aktivnost</button>
            <p class="white">{{allEntered}}{{alreadyExists}} </p>
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

        axios.get("rest/trainer", yourConfig).then((response) => {
            this.allTrainers = response.data;
        });
    },
    methods: {
        CreateActivity: function () {
            this.allEntered = "";
            this.alreadyExists = "";

            if (this.picture === null || this.name === "") {
                this.allEntered = "Niste uneli sve podatke";
                return;
            }
            if (this.workoutType !== "solo" && !this.selectedTrainer) {
                this.allEntered = "Niste uneli sve podatke";
                return;
            }

            if (this.workoutType === "solo") {
                this.selectedTrainer.id = -1;
            }
            yourConfig = {
                headers: {
                    Authorization: localStorage.getItem("token"),
                    "Content-Type": "application/json",
                },
            };
            const formData = new FormData();
            formData.append("file", this.picture);
            axios
                .post("rest/files/workoutPicture", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                        Authorization: localStorage.getItem("token"),
                    },
                })
                .then(() => {
                    axios
                        .put(
                            "rest/workout/new/" + this.facId,
                            {
                                name: this.name,
                                workoutType: this.workoutType,
                                facilityId: this.facId,
                                durationInMinutes: this.durationInMinutes,
                                trainerId: this.selectedTrainer.id,
                                description: this.description,
                            },
                            yourConfig
                        )
                        .then((response) => {
                            //////////// ovde promeni
                            setTimeout(function () {
                                window.location.href = "#/pocetna";
                            }, 2500);
                        })
                        .catch((error) => {
                            this.alreadyExists =
                                "Već postoji aktivnost sa ovakvim nazivom";
                        });
                })
                .catch((error) => {
                    alert("NIJE USPEO");
                });
        },
        selectT: function (t) {
            if (!t) {
                this.selectedTrainer = null;
            }
            this.selectedTrainer = t;
        },
        handleFileUpload(e) {
            this.picture = null;
            const file = e.target.files[0];
            this.picture = file;
            this.showPicture = URL.createObjectURL(file);
        },
    },
});
