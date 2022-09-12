package main.model;

import exceptions.UnexpectedWeightException;

public class WeightedIngredient extends Ingredient {
	private static int count = 0;
	private double weight;
	private double pricePerUnit;

	public WeightedIngredient(int id, String name, double weight, double pricePerUnit) {
		super(id, name);
		this.weight = weight;
		this.pricePerUnit = pricePerUnit;
	}
	
	public WeightedIngredient(String name, double weight, double pricePerUnit) {
		super(count++, name);
		this.weight = weight;
		this.pricePerUnit = pricePerUnit;
	}

	public WeightedIngredient(WeightedIngredient wi) {
		super(wi.getId(), wi.getName());
		this.weight = wi.weight;
		this.pricePerUnit = wi.pricePerUnit;
	}
	
	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		WeightedIngredient.count = count;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public void addWeight(double weight) {
		this.weight+=weight;
	}
	
	public void subtractWeight(double weight) {
		if(this.weight<weight) throw new UnexpectedWeightException("Navedena tezina je veca od postojece.");
		this.weight-=weight;
	}
	
	@Override
	public Double getPrice() {
		return weight * pricePerUnit;
	}

	public WeightedIngredient withScaledWeight(double procentage) {
		return new WeightedIngredient(getId(), getName(), weight * (procentage / 100), pricePerUnit);
	}

	@Override
	public String toString() {
		return "Id: " + this.getId() + " Ingredient: " + this.getName() + ", weight: " + this.weight
				+ ", pricePerUnit: " + this.pricePerUnit + "\n";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WeightedIngredient)
			return ((WeightedIngredient) obj).getId() == this.getId();
		return false;
	}
}
