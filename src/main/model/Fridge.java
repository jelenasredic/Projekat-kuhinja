package main.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.UnexpectedIngredientException;
import exceptions.UnexpectedWeightException;

public class Fridge {
	private List<WeightedIngredient> weightedIngredients;

	public Fridge() {
		this.weightedIngredients = new ArrayList<WeightedIngredient>();
	}

	public void addWeightedIngredient(WeightedIngredient weightedIngredient) {
		int indexOfIngredientInFridge = weightedIngredients.indexOf(weightedIngredient);
		if (indexOfIngredientInFridge!=-1)
			weightedIngredients.get(indexOfIngredientInFridge).addWeight(weightedIngredient.getWeight());
		else
			weightedIngredients.add(weightedIngredient);
	}
	
	public void subtractWeightedIngredient(WeightedIngredient weightedIngredient) {
		int indexOfIngredientInFridge = weightedIngredients.indexOf(weightedIngredient);
		if (indexOfIngredientInFridge==-1) throw new UnexpectedIngredientException("Namirnica nije u frizideru.");
		weightedIngredients.get(indexOfIngredientInFridge).subtractWeight(weightedIngredient.getWeight());
		if (weightedIngredients.get(indexOfIngredientInFridge).getWeight()==0)
			weightedIngredients.remove(indexOfIngredientInFridge);
	}

	public void checkForIngredients(Recipe recipe) {
		for(WeightedIngredient recipeIngredient:recipe.getWeightedIngredients()) {
			int indexOfIngredientInFridge = weightedIngredients.indexOf(recipeIngredient);
			if (indexOfIngredientInFridge==-1) throw new UnexpectedIngredientException("Namirnica nije u frizideru.");
			if (weightedIngredients.get(indexOfIngredientInFridge).getWeight()<recipeIngredient.getWeight()) throw new UnexpectedWeightException("Navedena tezina je veca od postojece unutar frizidera");
		}
	}

	public void prepereMeal(Recipe recipe) {
		checkForIngredients(recipe);
		for(WeightedIngredient recipeIngredient:recipe.getWeightedIngredients()) {
			this.subtractWeightedIngredient(recipeIngredient);
		}
	}
	
	@Override
	public String toString() {
		String retVal="";
		for (WeightedIngredient wi : this.weightedIngredients) {
			retVal+=wi.toString();
		}
		return "Fridge\n" + retVal;
	}
}
