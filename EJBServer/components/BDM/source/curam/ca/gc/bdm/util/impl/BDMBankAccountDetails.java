package curam.ca.gc.bdm.util.impl;

/**
 *  POJO class for Bank account Details.
 *
 *
 * @author Ruturaj Varne
 *
 */
public class BDMBankAccountDetails {
	
	// Private attribute to hold institutionNumber value.
	private String institutionNumber = "";

	// Private attribute to hold transitNumber value.	
	private String transitNumber = "";
	
	/**
	 * Getter for institutionNumber
	 */
	public String getInstitutionNumber(){
		return this.institutionNumber;
	}
	
	/**
	 * Getter for transitNumber
	 */
	public String getTransitNumber(){
		return this.transitNumber;
	}
	
	/**
	 * Setter for institutionNumber
	 */
	public void setInstitutionNumber(String institutionNumber){
		this.institutionNumber = institutionNumber;
	}
	
	/**
	 * Setter for transitNumber
	 */
	public void setTransitNumber(String transitNumber){
		this.transitNumber = transitNumber;
	}
}