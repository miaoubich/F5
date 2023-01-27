package business;

import java.io.Serializable;
import java.util.List;
//SS
public class CheckoutRecord implements Serializable {
	
	private static final long serialVersionUID = 12345L;
	
	private List<CheckoutRecordEntry> checkoutRecordEntryList;
	
	private LibraryMember libraryMem;
	
	public CheckoutRecord(List<CheckoutRecordEntry> cOutRecordEntry, LibraryMember libMem){
		this.checkoutRecordEntryList = cOutRecordEntry;
		this.libraryMem = libMem;
		
	}

	public List<CheckoutRecordEntry> getCheckoutRecordEntryList() {
		return checkoutRecordEntryList;
	}

	public void setCheckoutRecordEntryList(List<CheckoutRecordEntry> checkoutRecordEntryList) {
		this.checkoutRecordEntryList = checkoutRecordEntryList;
	}

	public LibraryMember getLibraryMem() {
		return libraryMem;
	}

	public void setLibraryMem(LibraryMember libraryMem) {
		this.libraryMem = libraryMem;
	}
	
	

}