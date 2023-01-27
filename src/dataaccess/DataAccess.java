package dataaccess;

import java.util.HashMap;

import business.Book;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public void saveNewMember(LibraryMember member);
	public void updateBook(Book b);
	
	public HashMap<Integer, BookCopy> readBookCopy();
	public HashMap<String, HashMap<Integer, BookCopy>> readBookCopies();
	public HashMap<String, List<CheckoutRecordEntry>> readCheckoutRecord();
	public List<CheckoutRecordEntry> getCheckoutRecordByMemberId(String LibraryMemId);
	public void saveToCheckoutRecord(String memberId, CheckoutRecordEntry checkOutRE);
	public void saveCheckoutRecordEntry(CheckoutRecordEntry checkOutRE);
	public void saveCheckoutRecord(String memberId, List<CheckoutRecordEntry> checkOutRE); 

}
