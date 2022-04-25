
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * 
 */

/**
 * @author Administrator
 * 
 * I assumed the use of 365 as number of days in one year
 *
 */
public class MCPricer implements FinancialInstrument {
	
	// PARAMETERS
	
	static LocalDate now = LocalDate.now();  
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	Random gaus = new Random();
	
	// Parameters for the engine
	// - Inputs
	int Nmc; // Monte Carlo number of paths
	double S; // Underlying price at valuation date
	double r; // Risk-free annual interest rate
	double sigma; // Annualized constant implied volatility
	private static String toDay = dtf.format(now); // Valuation date
	
	// - Outputs
	public double opPriceBS = 0;
	public double opPrice = 0;
	public double opDelta = 0;
	public double opGamma = 0;
	public double opVega = 0;
	
	
	/*
	 * Constructor
	 * The annualized values become daily ones
	 */
	public MCPricer(double S, double r, double sigma, int Nmc) {
		this.S = S; 
		this.r = r; 
		this.sigma = sigma;
		this.Nmc = Nmc;
	}
	
	
	// FUNCTIONS
	/*
	 * Method to get the number of day until the given maturity
	 */
	private static int daysToMaturity(String mydate) {
		LocalDate date1_ = LocalDate.parse(toDay, dtf);
		LocalDate date2_ = LocalDate.parse(mydate, dtf);
		if(date1_.compareTo(date2_)<0) {
			return (int) ChronoUnit.DAYS.between(date1_, date2_);
		}
		else {
			System.out.println("Error: please make sure the date entered follow this format day/month/year and is greater than the valuation date!");
			return 0;
		}
	}
	
	/*
	 *  functions for the Black-Scholes models use
	 *  
	 */
	// 1- Error function
	private static double erf(double x) {
		try {
	        double t = 1.0 / (1.0 + 0.5 * Math.abs(x));
	        // Use of the Horner's method
	        double res = 1 - t * Math.exp( -x*x   -   1.26551223 +
	                                            t * ( 1.00002368 +
	                                            t * ( 0.37409196 + 
	                                            t * ( 0.09678418 + 
	                                            t * (-0.18628806 + 
	                                            t * ( 0.27886807 + 
	                                            t * (-1.13520398 + 
	                                            t * ( 1.48851587 + 
	                                            t * (-0.82215223 + 
	                                            t * ( 0.17087277))))))))));
	        if (x >= 0) return  res;
	        else return -res;
		}
		catch (IllegalArgumentException ex) {
			  return Double.NaN;
		}
    }
	// 2- Normal Cumulative Distribution function
	private double N_dist(double x) {
		 try {
			 return 0.5 * (1 + erf(x/Math.sqrt(2)));
		 }
		 catch (IllegalArgumentException ex) {
		  return Double.NaN;
		 }
	} 
	// 3- The derivative of Normal Cumulative Distribution function
	private double N_dist_(double x) {
		 try {
			 return Math.exp(-x*x/2)/Math.sqrt(2*Math.PI);
		 }
		 catch (IllegalArgumentException ex) {
		  return Double.NaN;
		 }
	}
	// 4- Functions to compute d1 and d2 from BS Model
	private double d_1(double T, double K) {
		double x = S/K;
		try {
			 return (Math.log(x) + (r+sigma*sigma*0.5)*T)/(sigma*Math.sqrt(T)); 
		 }
		 catch (IllegalArgumentException ex) {
		  return Double.NaN;
		 }
	}
	private double d_2(double T, double K) {
		try {
			 return (Math.log(S/K) + (r-sigma*sigma*0.5)*T)/(sigma*Math.sqrt(T)); 
		 }
		 catch (IllegalArgumentException ex) {
		  return Double.NaN;
		 } 
	}

	// 5- Black Scholes Model pricing function only for European options
	public double eurOpPriceBSModel(double T, double K, int oT) {
		double d1 = d_1(T, K);
		double d2 = d_2(T, K);
		try {
			 return oT*S*N_dist(oT*d1)-oT*K*Math.exp(-r*T)*N_dist(oT*d2); // oT allows to change the signs of this formula whether it is Call 1 or Put -1
		 }
		 catch (IllegalArgumentException ex) {
		  return Double.NaN;
		 }
	}
	
	
	// CORE
	/*
	 * European Option 
	 * ---------------
	 * Method to get the pay-off of the option only use whether 1 for a call or -1 for a put
	 * - for a call, set oT = 1
	 * - for a put, set oT = -1
	 */
	@Override
	public void europeanOptionMC(double K, String T, int oT) {
		try {
			if(oT==1 || oT==-1) {
				double payoff = 0;
				double daysT = daysToMaturity(T);
				double uS; // Underlying asset value at date 0
				
				for(int i=1; i<=this.Nmc; i++) {
					uS = this.S * Math.exp((this.r-this.sigma*this.sigma*0.5)*daysT/365 + this.sigma*Math.sqrt(daysT/365)*gaus.nextGaussian()); // The underlying asset value at the maturity according to the Black-Scholes' EDP solution 
					payoff = payoff + Math.max(oT*uS - oT*K, 0); // Payoff either Call if oT=1 or Put if oT=-1
				}
				
				// Outputs
				this.opPrice = Math.exp(-this.r*daysT/365)*payoff/this.Nmc; // Return the arithmetic mean of the discounting payoffs as expected payoff
				this.opPriceBS = eurOpPriceBSModel(daysT/365, K, oT); // Returns the price based on the Black-Scholes model's analytical solutions
				this.opDelta = N_dist(d_1(daysT/365, K)) + Math.min(oT, 0); // Min(oT, 0) is used to avoid to use a conditional loop
				this.opGamma = N_dist_(d_1(daysT/365, K))/(this.S*this.sigma*Math.sqrt(daysT/365));
				this.opVega = this.S*N_dist_(d_1(daysT/365, K))*Math.sqrt(daysT/365)/100; // Divide by 100 to get vega as option price change for 1% point change in volatility
			}
			else {
				System.out.println("Error: please the option type must be either 1 for a Call and -1 for a Put!");
			}
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println("Error: an unknown error occured create an Index out of bounds exception!");
		}
		catch(DateTimeException ex) {
			System.out.println("Error: date format error found!");
		}
		catch(NegativeArraySizeException ex) {
			System.out.println("Error: please, make sure that the date of pricing is greater than the valuation date!");
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex);
		}

	}

	/*
	 * Barrier Option 
	 * ---------------
	 * Method to get the Barrier type only use as oB the followings
	 * - for an Down-and-Out call option, set oB = downOut & oT = 1
	 * - for an Up-and-Out put option, set oB = upOut & oT = -1
	 * - for an Down-and-In put option, set oB = downIn & oT = -1
	 * - for an Up-and-In call option, set oB = upIn & oT = 1
	 * Method to get the pay-off of the option only use whether 1 for a call or -1 for a put
	 * - for a call, set oT = 1
	 * - for a put, set oT = -1
	 * Computing the different Barrier option types by combining DownOut & UpIn then DownIn & UpOut conditional because they are similar somehow 
	 * 
	 */
	@Override
	public void barrierOptionMC(double K, String T, String bT, int oT, double bar) {		
		try {
			double payoff = 0;
			int j = 0;
			double t = 0;
			double daysT = daysToMaturity(T);
			Double[] St = new Double[(int)daysT]; // Here only the # of days (integer value) is required not the fractional one
			
			if((oT==1 || oT==-1) && (bT=="downIn" || bT=="upIn" || bT=="downOut" || bT=="upOut")) {
				for(int i=1; i<=this.Nmc; i++) {
					St[0] = this.S; // Initialization of the underlying asset value at every new path
					for(j=0; j<St.length-1; j++) { 
						t = daysT-j;
						St[j+1] = St[j] * Math.exp((this.r-this.sigma*this.sigma*0.5)*t/365 + this.sigma*Math.sqrt(t/365)*gaus.nextGaussian()); // The underlying asset value at the maturity according to the Black-Scholes' EDP solution 	
						if(bT=="downOut" || bT=="upIn") if(St[j+1]<bar) break;
						if(bT=="upOut" || bT=="downIn") if(St[j+1]>bar) break;
					}
					// According to
					if(j==daysT-1) {
						payoff = payoff + Math.max(oT*St[(int)daysT-1] - oT*K, 0); // Payoff either Call if oT=1 or Put if oT=-1
					}
				}
				
				// Outputs
				this.opPrice = Math.exp(-this.r*daysT/365)*payoff/this.Nmc; // Return the arithmetic mean of the discounting payoffs as expected payoff
				this.opPriceBS = eurOpPriceBSModel(daysT/365, K, oT); // Returns the price based on the Black-Scholes model's analytical solutions
				this.opDelta = N_dist(d_1(daysT/365, K)) + Math.min(oT, 0); // Min(oT, 0) is used to avoid to use a conditional loop
				this.opGamma = N_dist_(d_1(daysT/365, K))/(this.S*this.sigma*Math.sqrt(daysT/365));
				this.opVega = this.S*N_dist_(d_1(daysT/365, K))*Math.sqrt(daysT/365)/100; // Divide by 100 to get vega as option price change for 1% point change in volatility
			}
			else {
				System.out.println("Error: please the type of Barrier must be downOut or upOut or downIn or upIn or the option type 1 for a call and -1 for a put!");
			}
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println("Error: an unknown error occured create an Index out of bounds exception!");
		}
		catch(DateTimeException ex) {
			System.out.println("Error: date format error found!");
		}
		catch(NegativeArraySizeException ex) {
			System.out.println("Error: please, make sure that the date of pricing is greater than the valuation date!");
		}
		catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
	}
	
	


}
