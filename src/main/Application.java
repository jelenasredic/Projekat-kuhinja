package main;

import java.util.List;
import java.util.Scanner;

import main.model.Recipe;
import main.model.RecipeDifficulty;
import main.model.WeightedIngredient;

public class Application {

	private static void printIngredients(List<WeightedIngredient> ingredients) {
		for (WeightedIngredient wi : ingredients) {
			System.out.println(wi.toString());
		}
	}

	private static int tryParseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			System.out.println("Neispravan unos");
			return -1;
		}
	}

	private static double tryParseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			System.out.println("Neispravan unos");
			return -1;
		}
	}

	private static RecipeDifficulty tryParseRecipeDifficulty(String value) {
		try {
			return RecipeDifficulty.values()[Integer.parseInt(value)];
		} catch (Exception e) {
			System.out.println("Neispravan unos");
			return null;
		}
	}

	public static void main(String[] args) {
		boolean run = true;
		Scanner scanner = new Scanner(System.in);
		Database database = new Database();
		while (run) {
			String menu = "Izaberite opciju iz menija:\n1. Dodavanje namirnica u firzider.\n2. Brisanje namirnice iz frizidera\n3. Lista jela koja su dostupna u frizideru\n"
					+ "4. Lista skaliranih jela koja su dostupna u frizideru\n5. Priprema jela\n6. Lista jela do odredjene cene\n"
					+ "7. Lista jela odredjene tezine\n8. Kombinacija 6. i 7.\n9. Lista sortiranih jela po tezini\n10. Lista sortiranih jela po ceni\n"
					+ "11. Lista omiljenih recepata\n12. Dodaj omiljeni recept\n13. Brisanje omiljenog recepta\n14. Lista omiljenih recept do odredjene cene\n0. Kraj";
			System.out.println(menu);
			String command = scanner.nextLine();
			switch (command) {
			case ("1"): {
				printIngredients(database.getIngredients());
				System.out.print("Unesite ID namirnice:");
				String id = scanner.nextLine();
				System.out.print("Unesite tezinu namirnice:");
				String weight = scanner.nextLine();
				try {
					if (tryParseInt(id) == -1 || tryParseDouble(weight) == -1)
						break;
					database.addIngredientToFridge(Integer.parseInt(id), Double.parseDouble(weight));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case ("2"): {
				System.out.println(database.getFridge().toString());
				System.out.print("Unesite ID namirnice:");
				String id = scanner.nextLine();
				System.out.print("Unesite tezinu namirnice:");
				String weight = scanner.nextLine();
				try {
					if (tryParseInt(id) == -1 || tryParseDouble(weight) == -1)
						break;
					database.subtractIngredientFromFridge(Integer.parseInt(id), Double.parseDouble(weight));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case ("3"): {
				for (Recipe recipe : database.findAllRecipesAvailableInFridge())
					System.out.println(recipe.toString());
				break;
			}
			case ("4"): {
				System.out.print("Unesite procenat skaliranja:");
				String procentage = scanner.nextLine();
				if (tryParseDouble(procentage) == -1)
					break;
				for (Recipe recipe : database.findAllRecipesAvailableInFridge(Double.parseDouble(procentage))) {
					System.out.println(recipe.toString());
				}
				break;
			}
			case ("5"): {
				System.out.print("Unesite naziv recepta:");
				String recipeTitle = scanner.nextLine();
				System.out.print("Unesite procenat skaliranja:");
				String procentage = scanner.nextLine();
				if (tryParseDouble(procentage) == -1)
					break;
				double scaleProcentage = 0;
				if (procentage.trim().isEmpty())
					scaleProcentage = 100;
				else
					scaleProcentage = Double.parseDouble(procentage);
				try {
					database.prepereMealFromFridge(recipeTitle, scaleProcentage);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case ("6"): {
				System.out.print("Unesite maksimalnu cenu:");
				String price = scanner.nextLine();
				if (tryParseDouble(price) == -1)
					break;
				for (Recipe recipe : database.findAllRecipesByPrice(Double.parseDouble(price))) {
					System.out.println(recipe.toString());
				}
				break;
			}
			case ("7"): {
				System.out.print("Unesite tezinu:\n");
				for (RecipeDifficulty diff : RecipeDifficulty.values()) {
					System.out.println(diff.ordinal() + ". " + diff.toString() + "\n");
				}
				String diff = scanner.nextLine();
				RecipeDifficulty difficulty = tryParseRecipeDifficulty(diff);
				List<Recipe> list = database.findAllRecipesByDifficulty(difficulty);
				for (Recipe recipe : list)
					System.out.println(recipe.toString());
				break;
			}
			case ("8"): {
				System.out.print("Unesite maksimalnu cenu:");
				String price = scanner.nextLine();
				System.out.print("Unesite tezinu:\n");
				for (RecipeDifficulty diff : RecipeDifficulty.values()) {
					System.out.println(diff.ordinal() + ". " + diff.toString() + "\n");
				}
				String diff = scanner.nextLine();
				RecipeDifficulty difficulty = tryParseRecipeDifficulty(diff);
				for (Recipe recipe : database.findAllRecipesByDifficultyAndPrice(Double.parseDouble(price),
						difficulty)) {
					System.out.println(recipe.toString());
				}
				break;

			}
			case ("9"): {
				for (Recipe wi : database.getSortedRecipesByDifficulty()) {
					System.out.println(wi.toString());
				}
				break;
			}
			case ("10"): {
				for (Recipe wi : database.getSortedRecipesByPrice()) {
					System.out.println(wi.toString());
				}
				break;
			}
			case ("11"): {
				for (Recipe wi : database.getFavouriteRecipes()) {
					System.out.println(wi.toString());
				}
				break;
			}
			case ("12"): {
				System.out.print("Unesite naziv recepta:");
				String recipeTitle = scanner.nextLine();
				try {
					database.addFavouriteRecipe(recipeTitle);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case ("13"): {
				System.out.print("Unesite naziv recepta:");
				String recipeTitle = scanner.nextLine();
				try {
					database.removeFavouriteRecipe(recipeTitle);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case ("14"): {
				System.out.print("Unesite maksimalnu cenu:");
				String price = scanner.nextLine();
				for (Recipe wi : database.getFavouriteRecipesUnderPrice(Double.parseDouble(price))) {
					System.out.println(wi.toString());
				}
				break;
			}
			case ("0"): {
				run = false;
				break;
			}
			default: {
				continue;
			}
			}
		}
	}
}
