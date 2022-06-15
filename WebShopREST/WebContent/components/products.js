Vue.component("products", { 
	data: function () {
	    return {
	      products: null
	    }
	},
	    template: ` 
    	<div>
    		<h3>Prikaz proizvoda</h3>
    		<table border="1">
	    		<tr bgcolor="lightgrey">
	    			<th>Naziv</th>
	    			<th>Cena</th>
	    		</tr>
	    			
	    		<tr v-for="(p, index) in products">
	    			<td>{{p.name}}</td>
	    			<td>{{p.price}}</td>
	    			<td>
	    				<button v-on:click="editProduct(p.id)">Izmeni</button>
	    				<button v-on:click="deleteProduct(p.id, index)">Obriši</button>
	    			</td>
	    		</tr>
	    	</table>
    		<button v-on:click = "addProduct">Dodaj nov proizvod</button>
    	</div>		  
    	`,
    mounted () {
        axios
          .get('rest/products/')
          .then(response => (this.products = response.data))
    },
    methods: {
    	addProduct : function() {
    		router.push(`/products/-1`);
    	},
    	editProduct : function(id) {
    		router.push(`/products/${id}`);
    	},
    	deleteProduct : function(id, index) {
    		r = confirm("Are you sure?")
    		if (r){
	    		axios
	            .delete('rest/products/' + id)
	            .then(response => (this.products.splice(index, 1)))
    		}
    	}
    }
});