package test;

import java.text.DecimalFormat;
import java.time.DateTimeException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import Pricer.MCPricer;

class MCPricerTest {
/*
 * These unit tests as following are made to test the accuracy of the Black-Scholes Model functions used within this program, 
 * also the values have been rounded at 2 digits.
 * 1- Testing if the Call and Put option pricer give the accurate price of a call and put using another Black-Scholes pricer 
 * with as the inputs: spot 10, strike 10, time to expiration 1 year, volatility 0.15and risk-free interest rate 0.01 results: 
 * Call priced at 0.65 and put at 0.55.
 * 2- Comparing the Monte Carlo pricer for European Call and Put options with as the inputs: spot 10, strike 10, time to expiration
 * 1 year (23/04/2022-23/04/2023), volatility 0.15, risk-free interest rate 0.01 results for BS Model Call priced at 0.65 and 
 * put at 0.55. Regarding the MC Model, the number of paths is 1,000,000 to be accurate. 
 * 3- Comparing two barrier options with the same inputs to see if they would be equal.
 */
	
//  1- 
	@Test
	public void testBSModelPricerEurOption() {
		
		// The first test scenario: 
		MCPricer bsm = new MCPricer(10, 0.01, 0.15, 100);
		double call = bsm.eurOpPriceBSModel(1, 10, 1); // 1 for a call
		double put = bsm.eurOpPriceBSModel(1, 10, -1); // -1 for a put
		
		// The results comparison 
		Assertions.assertEquals(0.65, Math.round(call*100.0)/100.0);
		Assertions.assertEquals(0.55, Math.round(put*100.0)/100.0); 
	}


//  2- 
	@Test
	public void testMCModelPricerEurOption() {
		// The second test scenario:
		MCPricer mcm_call = new MCPricer(10, 0.01, 0.15, 1000000);
		MCPricer mcm_put = new MCPricer(10, 0.01, 0.15, 1000000);
		mcm_call.europeanOptionMC(10, "23/04/2023", 1); // 1 for a call
		mcm_put.europeanOptionMC(10, "23/04/2023", -1); // -1 for a put
		
		// The results comparison 
		Assertions.assertEquals(0.65, Math.round(mcm_call.opPrice*100.0)/100.0); 
		Assertions.assertEquals(0.55, Math.round(mcm_put.opPrice*100.0)/100.0); 
	}


//  3- 
	@Test
	public void testMCModelPricerBarOption() {
		// The second test scenario:
		MCPricer bar_1 = new MCPricer(100, 0.01, 0.15, 2000000);
		MCPricer bar_2 = new MCPricer(100, 0.01, 0.15, 2000000);
		bar_1.barrierOptionMC(110, "23/04/2023", "downIn", -1, 90); 
		bar_2.barrierOptionMC(110, "23/04/2023", "downIn", -1, 90);  
		
		// The results comparison 
		System.out.println("1 "+(Math.round(bar_1.opPrice*100.0)/100.0)+" 2 "+(Math.round(bar_2.opPrice*100.0)/100.0));
		double comp1 = Math.round(bar_1.opPrice*100.0)/100.0;
		double comp2 = Math.round(bar_2.opPrice*100.0)/100.0;
		Assertions.assertEquals(comp1, comp2); 
	}

}
