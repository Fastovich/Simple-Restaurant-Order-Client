import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Order {
	private int orderID;
	private static int idCounter = 1000;
	//hard coded, can be set to hashmap just needs a lot of extra steps not needed for 3 items
	public double valueBurger = 5.00;
	public double valueFries = 2.00;
	public double valueShake = 3.50;
	public double quantityBurger;
	public double quantityFries;
	public double quantityShake;
	
	public Order(double quantityBurger, double quantityFries, double quantityShake) {
		this.quantityBurger = quantityBurger;
		this.quantityFries = quantityFries;
		this.quantityShake = quantityShake;
		this.orderID = idCounter++;
	}


	public int getOrderID(){
		return orderID;
	}

	public double getTotal() {
		double cost = this.valueBurger * quantityBurger;
		double cost1 = this.valueFries * quantityFries;
		double cost2 = this.valueShake * quantityShake;
		double subTotal = cost + cost1 + cost2;
		double tax = subTotal * .06;
		double total = subTotal + tax;
		return total;
	}

//	NumberFormat formatter = NumberFormat.getCurrencyInstance();

//

//

}
