

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener {
	JButton button;
	JTextField txt_S;
	JComboBox txt_oT;
	JLabel lb_bT;
	JTextField txt_K;
	JTextField txt_T;
	JTextField txt_Nmc;
	JTextField txt_bar;
	JTextField txt_r;
	JTextField txt_sigma;
	JLabel mc;
	JLabel bs;
	JLabel delta;
	JLabel gamma;
	JLabel vega;
	JComboBox opt;
	JComboBox txt_bT;
	JLabel lb_bar;
	
	Frame(){
		this.setSize(500, 1000);
		this.setTitle("Welcome to the Monte Carlo Pricer");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(19, 1));
		
		// Option
		JLabel opt_lb = new JLabel("Option to price");
		String[] opt_ = {"European", "Barrier"};
		opt = new JComboBox(opt_);
		opt.addActionListener(this);
		
		// European option
		String[] oT = {"Call", "Put"};
		JLabel lb_oT = new JLabel("Option type ");
		txt_oT = new JComboBox(oT);
		txt_oT.addActionListener(this);
		txt_oT.setPreferredSize(new Dimension(50, 30));
	
		JLabel lb_S = new JLabel();
		txt_S = new JTextField();
		lb_S.setText("Underlying price ");
		txt_S.setPreferredSize(new Dimension(50, 30));
		lb_S.setHorizontalAlignment(JLabel.LEFT);
		
		JLabel lb_K = new JLabel();
		txt_K = new JTextField();
		lb_K.setText("Strike price ");
		txt_K.setPreferredSize(new Dimension(50, 30));
		
		JLabel lb_T = new JLabel();
		txt_T = new JTextField();
		lb_T.setText("Maturity ");
		txt_T.setPreferredSize(new Dimension(50, 30));
		
		JLabel lb_sigma = new JLabel();
		txt_sigma = new JTextField();
		lb_sigma.setText("Volatility (ex: 0.15)");
		txt_sigma.setPreferredSize(new Dimension(50, 30));
	
		JLabel lb_r = new JLabel();
		txt_r = new JTextField();
		lb_r.setText("Risk-free interest rate (ex: 0.01)");
		txt_r.setPreferredSize(new Dimension(50, 30));
		
		// Barrier option
		String[] bT = {"downOut", "upOut", "downIn", "upIn"};
		txt_bT = new JComboBox(bT);
		lb_bT = new JLabel();
		lb_bT.setText("Barrier type ");
		txt_bT.setPreferredSize(new Dimension(50, 30));
		lb_bT.setVisible(false);
		txt_bT.setVisible(false);;
		txt_bT.addActionListener(this);
		
		lb_bar = new JLabel();
		txt_bar = new JTextField();
		lb_bar.setText("Barrier price ");
		txt_bar.setPreferredSize(new Dimension(50, 30));
		lb_bar.setVisible(false);
		txt_bar.setVisible(false);;
		txt_bar.addActionListener(this);
		
		// Monte Carlo input
		JLabel lb_Nmc = new JLabel();
		txt_Nmc = new JTextField();
		lb_Nmc.setText("Number of path ");
		txt_Nmc.setPreferredSize(new Dimension(250, 30));
		
		// Calculation
		button = new JButton("Calculate");
		button.addActionListener(this);
		
		// Result
		JLabel res_lb = new JLabel("MC Price");
		mc = new JLabel();
		JLabel res_bs = new JLabel("BS Price: ");
		bs = new JLabel();
		JLabel res_del = new JLabel("Delta: ");
		delta = new JLabel();
		JLabel res_gam = new JLabel("Gamma: ");
		gamma = new JLabel();
		JLabel res_veg = new JLabel("Vega: ");
		vega = new JLabel();
	
		
		this.add(opt_lb);
		this.add(opt);
		
		this.add(lb_oT);
		this.add(txt_oT);
		
		this.add(lb_S);
		this.add(txt_S);
		
		this.add(lb_K);
		this.add(txt_K);
		
		this.add(lb_T);
		this.add(txt_T);
		
		this.add(lb_sigma);
		this.add(txt_sigma);
		
		this.add(lb_r);
		this.add(txt_r);
		
		this.add(lb_Nmc);
		this.add(txt_Nmc);
		
		this.add(lb_bT);
		this.add(txt_bT);
		
		this.add(lb_bar);
		this.add(txt_bar);

		this.add(res_lb);
		this.add(mc);
		this.add(res_bs);
		this.add(bs);
		this.add(res_del);
		this.add(delta);
		this.add(res_gam);
		this.add(gamma);
		this.add(res_veg);
		this.add(vega);
		
		this.add(button);
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button) {
			if(opt.getSelectedItem()=="European") {
				double S = Double.parseDouble(txt_S.getText());
				double K = Double.parseDouble(txt_K.getText());
				String T = txt_T.getText();
				double r = Double.parseDouble(txt_r.getText());
				int Nmc = Integer.parseInt(txt_Nmc.getText());
				double sigma = Double.parseDouble(txt_sigma.getText());
				
				int oT;
				if(txt_oT.getSelectedItem()=="Call") oT=1;
				else oT = -1;
				
				MCPricer euro = new MCPricer(S, r, sigma, Nmc);
				euro.europeanOptionMC(K, T, oT);
				mc.setText(String.valueOf(euro.opPrice));
				bs.setText(String.valueOf(euro.opPriceBS));
				delta.setText(String.valueOf(euro.opDelta));
				gamma.setText(String.valueOf(euro.opGamma));
				vega.setText(String.valueOf(euro.opVega));
			}
			

			if(opt.getSelectedItem()=="Barrier") {
				double S = Double.parseDouble(txt_S.getText());
				double K = Double.parseDouble(txt_K.getText());
				String T = txt_T.getText();
				double r = Double.parseDouble(txt_r.getText());
				int Nmc = Integer.parseInt(txt_Nmc.getText());
				double sigma = Double.parseDouble(txt_sigma.getText());
				
				int oT;
				if(txt_oT.getSelectedItem()=="Call") oT=1;
				else oT = -1;
				
				String bT = (String) txt_bT.getSelectedItem();
				double bar = Double.parseDouble(txt_bar.getText());
				
				MCPricer barr = new MCPricer(S, r, sigma, Nmc);
				barr.barrierOptionMC(K, T, bT, oT, bar);
				mc.setText(String.valueOf(barr.opPrice));
				bs.setText(String.valueOf(barr.opPriceBS));
				delta.setText(String.valueOf(barr.opDelta));
				gamma.setText(String.valueOf(barr.opGamma));
				vega.setText(String.valueOf(barr.opVega));
				
			}
		}
		
		if(e.getSource()==opt) {
			if(opt.getSelectedItem()=="Barrier") {
				lb_bT.setVisible(true);
				lb_bar.setVisible(true);
				txt_bT.setVisible(true);
				txt_bar.setVisible(true);
			}
		}
		
	}
	

}
