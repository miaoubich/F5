package business;

import java.io.Serializable;
import java.time.LocalDate;
//SS
public class CheckoutRecordEntry implements Serializable {
	private static final long serialVersionUID = 12345L;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private String memberId;
	private BookCopy bookCopy;
	
	public CheckoutRecordEntry(BookCopy bCopy, String libraryMemId) {
		this.checkoutDate = LocalDate.now();
		this.dueDate = LocalDate.now().plusDays(bookCopy.getBook().getMaxCheckoutLength());
		this.bookCopy = bCopy;
		this.memberId = libraryMemId;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	public BookCopy getbookCopy() {
		return bookCopy;
	}

	public void setbCopy(BookCopy bookCopy) {
		this.bookCopy = bookCopy;
	}
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	

}
