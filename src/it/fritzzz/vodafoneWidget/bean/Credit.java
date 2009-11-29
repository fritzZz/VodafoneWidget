package it.fritzzz.vodafoneWidget.bean;

import java.io.Serializable;

public class Credit implements Serializable{
	
	private static final long serialVersionUID = -4256443698898611640L;
	private String date;
	private String amount;
	
	/***
	 * Returs date object (" " is given for fix view bug of italic character)
	 * @return
	 */
	public String getDate() {
		return date+" ";
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
