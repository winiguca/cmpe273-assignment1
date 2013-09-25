package edu.sjsu.cmpe.library.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Book 
{
	//Book Class Properties declaration
	private static int bookKey;
	private int isbn;
	
	@NotEmpty(message ="title is required field.")
	private String title;
	
	@NotNull(message ="publicaiton date is required field.")
	private Date publicationDate;
	
	private String language;
	private int numberOfPages;
	private BookStatus eBookStatus;
	private List<Author> authors;
	private List<Review> bookReview;

	public enum BookStatus {Available, CheckedOut, InQueue, Lost}	
	@SuppressWarnings("serial")
	public static final Map<String, BookStatus> BookStatusMapper = Collections
            .unmodifiableMap(new HashMap<String, BookStatus>()
       		{
                { 
                    put("available", BookStatus.Available);
                    put("checked-out", BookStatus.CheckedOut);
                    put("in-queue", BookStatus.InQueue);
                    put("lost", BookStatus.Lost);
                }
            });
	
	//constructor
	//isbn - set and get	
	public Book() 
	{
		this.isbn = ++bookKey;
	}
	 	
	public int getIsbn()
	{
		return isbn;
	}
	
	//title - set and get
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title  = checkNotNull(title, "title is null");;
	}

	//publication date - set and get
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	@JsonProperty("publication-date")
	public String getPublicationDate() {
		return dateFormat.format(publicationDate);
	}
	public void setPublicationDate(String publicationDate) 
	{
		try 
		{
			this.publicationDate = dateFormat.parse(publicationDate);
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

	//language -set and get
	public String getLanguage()
	{
		return language;
	}
	public void setLanguage(String language) 
	{
		this.language = language;
	}

	//number of pages - set and get
	@JsonProperty("num-pages")
	public int getNumberOfPages() 
	{
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) 
	{
		this.numberOfPages = numberOfPages;
	}

	//book status - set and get
	@JsonProperty("status")
	public BookStatus geteBookStatus() 
	{
		return eBookStatus;
	}

	public void seteBookStatus(String eBookStatus) 
	{
		if(eBookStatus ==null)
		{
			this.eBookStatus = BookStatusMapper.get("available");
		}
		else
		{
			this.eBookStatus = BookStatusMapper.get(eBookStatus);
		}
	}

	//authors - get and set
	public List<Author> getAuthors()
	{
		return authors;
	}
	
	public void addBookAuthor(Author author) 
	{
		if(this.authors ==null)
		{
			authors = new ArrayList<Author>();
		}
		this.authors.add(author);
	}
	
	public void addBookAuthors(List<Author> authors)
	{
		for(Author item:  authors)
		{
			this.authors.add(item);
		}
	}

	//review -set and get
	@JsonProperty("reviews")
	public List<Review> getBookRating() 
	{
		return bookReview;
	}

	public void addBookReview(Review bookRating) 
	{
		if(this.bookReview ==null)
		{
			bookReview = new ArrayList<Review>();
		}
		this.bookReview.add(bookRating);
	}
	
	public void addBookReviews(List<Review> bookReviews)
	{
		for(Review item:  bookReviews)
		{
			this.bookReview.add(item);
		}
	}

} //class book
