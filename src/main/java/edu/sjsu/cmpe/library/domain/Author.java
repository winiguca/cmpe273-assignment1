package edu.sjsu.cmpe.library.domain;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import edu.sjsu.cmpe.library.dto.LinksDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Author extends LinksDto 
{
	//properties declaration
	private static int authorKey;
	private int id;
	
	@NotEmpty(message ="name of author is a required value.")
	private String name;
	
	private List<Book> booksByAuthors;

	//methods declaration
	//AuthorId - set and get
	//constructor
	public Author() 
	{
		this.id = ++authorKey;
	}
	
	public int getAuthorId() 
	{
		return id;
	}

	//AuthorName - set and get	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	
	//BooksByAuthor - set and get	
	public List<Book> getBooksByAuthors() 
	{
		return booksByAuthors;
	}
	
	public void setBooksByAuthors(List<Book> booksByAuthors) 
	{
		this.booksByAuthors = booksByAuthors;
	}
}
