package com.validation.processor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="record")
@XmlAccessorType(XmlAccessType.PROPERTY)

public class Record {

	// Mapping values from XML to Objects
	private String reference;
	private String accountNumber;
	private String description;
	private double startBalance;
	private double mutation;
	private double endBalance;

	// XML elements names as final String literals
	static final String ROOT = "records";
	static final String RECORD = "record";
	static final String REFERENCE = "reference";
	static final String ACCOUNTNUMBER = "accountNumber";
	static final String DESCRIPTION = "description";
	static final String STARTBALANCE = "startBalance";
	static final String MUTATION = "mutation";
	static final String ENDBALANCE = "endBalance";

	// getter setter method for each Objects
	public String getReference() {
		return reference;
	}

	public void setReference(String freference) {
		this.reference = freference;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String faccountNumber) {
		this.accountNumber = faccountNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String fdescription) {
		this.description = fdescription;
	}

	public double getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(double fstartBalance) {
		this.startBalance = fstartBalance;
	}

	public double getMutation() {
		return mutation;
	}

	public void setMutaion(double fmutation) {
		this.mutation = fmutation;
	}

	public double getEndBalance() {
		return endBalance;
	}

	public void setEndBalance(double fendBalance) {
		this.endBalance = fendBalance;
	}

	public String toString() {
		return "Reference: " + this.reference + "\n" + 
				"Account number: " + this.accountNumber+ "\n"+
				"Description: " + this.description+ "\n"+
				"Starting Balance: " + this.startBalance+ "\n"+
				"Mutation: " + this.mutation+ "\n"+
				"Ending Balance: " + this.endBalance+ "\n";
	}
}
