package main.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Priceable {

	private String title;
	private RecipeDifficulty difficulty;
	private List<WeightedIngredient> weightedIngredients;

	public Recipe(String title) {
		this.title = title;
		this.weightedIngredients = new ArrayList<WeightedIngredient>();
	}

	public Recipe(String title, List<WeightedIngredient> weightedIngredients) {
		this.title = title;
		this.weightedIngredients = weightedIngredients;
	}

	@Override
	public Double getPrice() {
		return weightedIngredients.stream().mapToDouble(WeightedIngredient::getPrice).sum();
	}

	public RecipeDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(RecipeDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public void setWeightedIngredients(List<WeightedIngredient> weightedIngredients) {
		this.weightedIngredients = weightedIngredients;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<WeightedIngredient> getWeightedIngredients() {
		return weightedIngredients;
	}

	public void addWeightedIngredients(WeightedIngredient weightedIngredients) {
		if (this.weightedIngredients == null)
			this.weightedIngredients = new ArrayList<WeightedIngredient>();
		this.weightedIngredients.add(weightedIngredients);
	}

	public void removeWeightedIngredients(int weightedIngredientId) {
		this.weightedIngredients.removeIf(recipe -> recipe.getId() == weightedIngredientId);
	}

	public Recipe getScaledRecipe(double procentage) {
		Recipe scaledRecipe = new Recipe(this.title);
		for (WeightedIngredient wi : this.weightedIngredients)
			scaledRecipe.addWeightedIngredients(wi.withScaledWeight(procentage));
		scaledRecipe.difficulty=this.difficulty;
		return scaledRecipe;
	}

	@Override
	public String toString() {
		String ingredients = "";
		for (var wi : this.weightedIngredients)
			ingredients += wi.toString();
		return "Recipe Title: '" + this.title + "', Difficulty: "+this.difficulty.toString()+ ", price: " + this.getPrice().toString()+
				"\nWeighted ingredients:\n" + ingredients;
	}
}
