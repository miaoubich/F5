package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<String> allBookIds();
	public Book addBookCopy(String isbn) throws Exception;
	
	public BookCopy checkoutBook(String LibraryMemberId, String isbn) throws Exception;
	public List<CheckoutRecordEntry> getCheckoutRecordByMemberId(String libraryMemberId) throws Exception;
	
}
