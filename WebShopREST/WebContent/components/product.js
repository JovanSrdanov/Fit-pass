Vue.component("edit-product", {
	data: function () {
		    return {
		      title: "Dodaj proizvod",
		      value: "Dodaj",
		      id : -1,
		      product: {id: '', name:null, price:null}
		    }
	},
	template: ` 
<div>
	{{title}}
	<form>
		<label>Ime</label>
		<input type = "text" v-model = "product.name" name = "name">
		<label>Cena</label>
		<input type = "number" v-model = "product.price" name = "price">
		<input type = "submit" v-on:click = "editProduct" v-bind:value = "this.value">
	</form>
</div>		  
`
	, 
	methods : {
		editProduct : function () {
			event.preventDefault();
			if (this.id != -1){
				axios.put('rest/products/' + this.product.id, this.product).
				then(response => (router.push(`/`)));
			}
			else{
				axios.post('rest/products', this.product).
				then(response => (router.push(`/`)));
			}
		}
	},
	mounted () {
		this.id = this.$route.params.id;
		if (this.id != -1){
			this.title = "Izmeni proizvod";
			this.value = "Izmeni";
	        axios
	          .get('rest/products/' + this.id)
	          .then(response => (this.product = response.data))
		}
    }
});