package main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import exceptions.IngredientNotFoundException;
import exceptions.RecipeNotFoundException;
import exceptions.UnexpectedIngredientException;
import main.model.Fridge;
import main.model.Recipe;
import main.model.RecipeDifficulty;
import main.model.WeightedIngredient;

public class Database {
	private List<WeightedIngredient> ingredients = new ArrayList<WeightedIngredient>();
	private List<Recipe> favouriteRecipes = new ArrayList<Recipe>();
	private List<Recipe> recipes = new ArrayList<Recipe>();
	private Fridge fridge = new Fridge();

	public Database() {
		initIngredients();
		initRecipes();
		this.fridge = new Fridge();
	}


	public List<WeightedIngredient> getIngredients() {
		return ingredients;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public Fridge getFridge() {
		return fridge;
	}

	public void addIngredientToFridge(int ingredientId, double weight) {
		WeightedIngredient ingredient = new WeightedIngredient(findIngredientById(ingredientId));
		ingredient.setWeight(weight);
		this.fridge.addWeightedIngredient(ingredient);
	}
	public void subtractIngredientFromFridge(int ingredientId, double weight) {
		WeightedIngredient ingredient = new WeightedIngredient(findIngredientById(ingredientId));
		ingredient.setWeight(weight);
		this.fridge.subtractWeightedIngredient(ingredient);
	}

	public void prepereMealFromFridge(String mealTitle, double scaleProcentage) {
		Recipe recipe=findRecipeByTitle(mealTitle);
		fridge.prepereMeal(recipe.getScaledRecipe(scaleProcentage));
	}

	public List<Recipe> findAllRecipesAvailableInFridge() {
		return this.findAllRecipesAvailableInFridge(100); // Da ne bi ponavljao kod. Vrijednost 100 ponistava
															// skaliranje.
	}

	public List<Recipe> findAllRecipesAvailableInFridge(double scaleProcentage) {
		List<Recipe> availableRecipes = new ArrayList<Recipe>();
		for (Recipe recipe : recipes) {
			this.fridge.checkForIngredients(recipe.getScaledRecipe(scaleProcentage));
			availableRecipes.add(recipe.getScaledRecipe(scaleProcentage));
		}
		return availableRecipes;
	}

	public List<Recipe> findAllRecipesByPrice(double price) {
		return this.recipes.stream().filter(recipe -> recipe.getPrice() <= price).collect(Collectors.toList());
	}

	public List<Recipe> findAllRecipesByDifficulty(RecipeDifficulty difficulty) {
		return this.recipes.stream().filter(recipe -> recipe.getDifficulty().equals(difficulty))
				.collect(Collectors.toList());
	}

	public List<Recipe> findAllRecipesByDifficultyAndPrice(double price, RecipeDifficulty difficulty) {
		List<Recipe> recipesByPrice = findAllRecipesByPrice(price);
		List<Recipe> recipesByDifficulty = findAllRecipesByDifficulty(difficulty);
		return recipesByDifficulty.stream().filter(recipesByPrice::contains).collect(Collectors.toList()); // Presek dve liste.
	}

	// Sortiranje od manje ka vecoj vrijednosti u oba slucaja...
	public List<Recipe> getSortedRecipesByDifficulty() {
		return this.recipes.stream()
				.sorted((recipe1, recipe2) -> recipe1.getDifficulty().compareTo(recipe2.getDifficulty()))
				.collect(Collectors.toList());
	}

	public List<Recipe> getSortedRecipesByPrice() {
		return this.recipes.stream().sorted((recipe1, recipe2) -> recipe1.getPrice().compareTo(recipe2.getPrice()))
				.collect(Collectors.toList());
	}

	public List<Recipe> getFavouriteRecipes() {
		return this.favouriteRecipes;
	}

	public void addFavouriteRecipe(String recipeTitle) {
		this.favouriteRecipes.add(this.findRecipeByTitle(recipeTitle));
	}

	public void removeFavouriteRecipe(String recipeTitle) {
		this.favouriteRecipes
				.removeIf(favRecipe -> favRecipe.getTitle().toLowerCase().equals(recipeTitle.trim().toLowerCase()));
	}

	public List<Recipe> getFavouriteRecipesUnderPrice(double price) {
		return this.favouriteRecipes.stream().filter(recipe -> recipe.getPrice() <= price).collect(Collectors.toList());
	}

	private WeightedIngredient findIngredientById(int id) {
		for (WeightedIngredient wi : this.ingredients)
			if (wi.getId() == id)
				return wi;
		throw new IngredientNotFoundException("Sastojak sa id: "+id+"nepostoji.");
	}

	private Recipe findRecipeByTitle(String title) {
		for (Recipe recipe : this.recipes)
			if (recipe.getTitle().toLowerCase().equals(title.trim().toLowerCase()))
				return recipe;
		throw new RecipeNotFoundException("Recept pod imenom " + title+" nepostoji.");
	}

	private void initRecipes() {
		this.recipes.add(initRecipe("Baklava",new ArrayList<Integer>() {{add(2);add(7);add(12);add(19);add(10);}},new ArrayList<Double>() {{add(1000.0);add(100.0);add(200.0);add(150.0);add(95.0);}},RecipeDifficulty.HARD));
		this.recipes.add(initRecipe("Pita od jabuka",new ArrayList<Integer>() {{add(0);add(7);add(12);add(19);}},new ArrayList<Double>() {{add(60.0);add(100.0);add(200.0);add(150.0);}},RecipeDifficulty.MEDIUM));
		this.recipes.add(initRecipe("Kajgana",new ArrayList<Integer>() {{add(4);add(8);}},new ArrayList<Double>() {{add(80.0);add (18.0);}},RecipeDifficulty.BEGINNER));
		this.recipes.add(initRecipe("Pita od dunja",new ArrayList<Integer>() {{add(1);add(7);add(12);add(19);}},new ArrayList<Double>() {{add(150.0);add(100.0);add(200.0);add(150.0);}},RecipeDifficulty.MEDIUM));
		this.recipes.add(initRecipe("Palacinke",new ArrayList<Integer>() {{add(3);add(4);add(8);add(9);}},new ArrayList<Double>() {{add(900.0);add(180.0);add(18.0);add(135.0);}},RecipeDifficulty.EASY));
		this.recipes.add(initRecipe("Pita sa mesom",new ArrayList<Integer>() {{add(5);add(12);add(14);add(19);}},new ArrayList<Double>() {{add(95.0);add(200.0);add(500.0);add(150.0);}},RecipeDifficulty.MEDIUM));
		this.recipes.add(initRecipe("Krofne",new ArrayList<Integer>() {{add(3);add(7);add(8);add(9);add(12);}},new ArrayList<Double>() {{add(90.0);add(100.0);add(18.0);add(135.0);add(200.0);}},RecipeDifficulty.EASY));
		this.recipes.add(initRecipe("Musaka sa krompirom",new ArrayList<Integer>() {{add(4);add(5);add(6);add(8);add(11);add(12);add(14);}},new ArrayList<Double>() {{add(80.0);add(95.0);add(80.0);add(18.0);add(100.0);add(200.0);add(300.0);}},RecipeDifficulty.PRO));
		this.recipes.add(initRecipe("Karbonara",new ArrayList<Integer>() {{add(4);add(5);add(8);add(13);add(17);add(18);}},new ArrayList<Double>() {{add(80.0);add(95.0);add(18.0);add(100.0);add(200.0);add(300.0);}},RecipeDifficulty.MEDIUM));
		this.recipes.add(initRecipe("Pasulj",new ArrayList<Integer>() {{add(4);add(5);add(12);add(15);add(16);}},new ArrayList<Double>() {{add(80.0);add(95.0);add(200.0);add(400.0);add(50.0);}},RecipeDifficulty.HARD));

	}

	private Recipe initRecipe(String title, ArrayList<Integer> ingredientIndexs,ArrayList<Double> scaleWeightOfRecipes,RecipeDifficulty difficulty) {
		Recipe recipe = new Recipe(title);
		for (int i=0;i<ingredientIndexs.size();i++)
			recipe.addWeightedIngredients(ingredients.get(ingredientIndexs.get(i)).withScaledWeight(scaleWeightOfRecipes.get(i)));
		recipe.setDifficulty(difficulty);
		return recipe;
	}


	private void initIngredients() {
		this.ingredients.add(new WeightedIngredient("Jabuke", 1, 60));
		this.ingredients.add(new WeightedIngredient("Dunje", 1, 150));
		this.ingredients.add(new WeightedIngredient("Orasi", 1, 1000));
		this.ingredients.add(new WeightedIngredient("Brasno", 1, 90));
		this.ingredients.add(new WeightedIngredient("So", 1, 80));
		this.ingredients.add(new WeightedIngredient("Biber", 1, 95));
		this.ingredients.add(new WeightedIngredient("Krompir", 1, 80));
		this.ingredients.add(new WeightedIngredient("Secer", 1, 100));
		this.ingredients.add(new WeightedIngredient("Jaja", 1, 18));
		this.ingredients.add(new WeightedIngredient("Mleko", 1, 135));
		this.ingredients.add(new WeightedIngredient("Margarin", 1, 95));
		this.ingredients.add(new WeightedIngredient("Pavlaka", 0.5, 100));
		this.ingredients.add(new WeightedIngredient("Ulje", 1, 200));
		this.ingredients.add (new WeightedIngredient("Špagete", 1,100));
		this.ingredients.add (new WeightedIngredient("Mleveno meso",0.5, 500));
		this.ingredients.add (new WeightedIngredient("Pasulj",1,400 ));
		this.ingredients.add (new WeightedIngredient("Luk",1, 50));
		this.ingredients.add (new WeightedIngredient("Slanina",0.2,200 ));
		this.ingredients.add (new WeightedIngredient("Kackavalj",0.3, 300));
		this.ingredients.add (new WeightedIngredient("Kore za pitu",1,150 ));
	}
}
