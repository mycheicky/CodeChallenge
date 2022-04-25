
##############################|                             
##############################|  Monte Carlo Pricing (v1.00)  
##############################|                             

##############################|  HOW TO RUN THE PROJECT  
Beforehand, kindly note that I have used Swing instead of Maven Spring because this project in Java is my first experience with Java. I did not have more time to go 
through Maven even though I tried to learn it before using Swing which was more easy and quick to implement for me at that time.

1- Please first double click on the file SolactiveCodeChallenge_SIBI_Cheik.jar
2- Choose firstly the option either European option or Barrier to price. Whether you selected either European option or Barrier option some fields will be appear or 
desappear
3- Select the option type either Call or Put
4- As well as only for a Barrier option, please select the Barrier option type e.g DownOut
5- Fill in the rest of the textfieds with the expected value types.

###############################|        ASSUMPTIONS       
# Assumption 1: regarding of the evolution of assets
The evolution of the underlying asset S is a stochastic process verified the stochastic differential equation: dSt = rStdt + σStdWt, S(t = 0) = S0
• σ volatility of the asset
• r risk-free interest rate
• Wt is the Brownian motion.
We use the exact solution of stochastic differential equation to evaluate the final value S(T) of the underlying asset. The solution of this equation is given by the 
formula: S(T) = S0 exp((r − σ^2/2)T + σW(T))
Furthermore, W(T) is the value of the Brownian motion at the date t = T. This is a random variable that follows the Normal distribution law N(0, sqrt(T)). We can model 
the final value of the Brownian motion by the random variable: W(T) = N(0, 1)sqrt(T), N(0, 1) is the random number that follows standard Normal distribution law. 
Hence we can simulate the asset value using this formula: S(T) = S0 exp((r − σ^2/2)T + sqrt(T) N(0, 1))

# Assumption 2: European option
For pricing of the European option we simulate only the last value of asset S(T) because the European option is path-independent. We model the final value of the 
Brownian motion by the random variable: W(T) = N(0, 1)sqrt(T). The approximated value of European option at t = 0, for initial value of the underlying asset S = S0 
is given by the arithmetic mean: V(S0, 0) = exp(−rT) Sum(n=1->Nmc, max(S0 exp((r − σ^2/2)T + σsqrt(T)N(n)(0, 1)) − K, 0))/Nmc
• V option price
• Nmc monte carlo number of path
• T time to maturity.
NB: across the code the variables r, σ and T are expressed respectively annually and T in days.

# Assumption 3: Barrier option
We simulate the value of asset as done for pricing the European option but we simulate the value of asset every day because it is a path-dependant. 
Thus in case the value of asset breaches up or down the barrier with respect of the option type and barrier type we add up at the maturity the option payoff. Afterwards, the barrier option price is given 
by the arithmetic mean: V(S0, 0) = exp(−rT) Sum(n=1->Nmc, max(S0 exp((r − σ^2/2)T + σsqrt(T)N(n)(0, 1)) − K, 0))/Nmc e.g Barrier European Call option.

###############################|  ABOUT HAVING MORE TIME  
1- First of all, better handled the value type mismatch errors and the other exceptions.
2- Implemented some graphics to illustrer the options payoff, delta, gamma and vega curves
3- Carried on the Unit test of all functions, modules and exceptions to get a better coverage score
4- Implemented Maven Spring Boot instead of Swing
5- Degined more the Layout used

###############################|         FEEDBACK       
This Java project was an opportunity for me to learn Java for a week. Moreover, I enjoyed this "small challenge" because it covered important topics that I might have 
ignored or neglected about Java if I learned by myself as I am used to do.
Thank you for this challenge.

########################################################################################################
########################################################################################################
