package com.txdot.isd.rts.services.reports;

/*
 * Report.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/24/2001	Methods created
 * R Duggirala	09/04/2001	Added comments
 * Ray Rowehl	12/10/2001	Added for CountyWide Added setPages
 * K Harrell	08/07/2004	Initialize ciCurrX,ciCurrY
 *							defect 7428 Ver 5.2.1
 * S Johnston	05/09/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify
 *							defect 7896 Ver 5.2.3
 * K Harrell	08/10/2009	add getWidth() 
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * The Report class provides methods that are commonly used to format
 * report data.
 *
 * <p>Report class is designed to be run on the server.  StringBuffer 
 * object is returned that will then be retrieved by the client to
 * display the report.
 *
 * @version	Defect_POS_F 	08/10/2009
 * @author  Rakesh Duggirala
 * <br>Creation Date:		08/24/2001
 */
public class Report
{
	// defect 7428
	// Initialize current positions
	private int ciCurrX = 1;
	private int ciCurrY = 1;
	// end defect 7428 
	private int ciLinesPerPage; // Height of the report
	// Number of pages in a report initialized to 1
	public int ciPages = 1;
	private int ciWidth; // Width of the report
	public String csName; // Name of the report
	// Formatted report as a string
	protected StringBuffer csReportStr = new StringBuffer();

	/**
	 * Report constructor
	 */
	public Report()
	{
		super();
		if (System.getProperty("os.name").startsWith("AIX"))
		{
			System.setProperty("line.separator", "\r\n");
		}
	}
	
	/**
	 * Constructor to be used for instantiating a Report Class.
	 *
	 * @param asName String Report Name
	 * @param aiNewWidth int Defining page Width of the Report
	 * @param aiLinesPerPage int Defining Page Height of the Report
	 */
	public Report(String asName, int aiNewWidth, int aiLinesPerPage)
	{
		csName = asName;
		ciWidth = aiNewWidth;
		ciLinesPerPage = aiLinesPerPage;
		ciCurrX = 1;
		csReportStr = new StringBuffer("");
		if (System.getProperty("os.name").startsWith("AIX"))
		{
			System.setProperty("line.separator", "\r\n");
		}
	}
	
	/**
	 * Appends blank lines to the StringBuffer csReportStr
	 *
	 * @param aiBlankNum int Number of blanklines to be inserted
	 */
	public void blankLines(int aiBlankNum)
	{
		int liCounter = 0;
		while (liCounter < aiBlankNum)
		{
			csReportStr.append(System.getProperty("line.separator"));
			ciCurrX++; //Incrementing X
			ciCurrY = 1; //Resetting Y to 1
			liCounter++;
		}
	}
	
	/**
	 * Returns Sring with specified blank spaces
	 *
	 * @param aiSpaces int Number of blankspaces (characters) to be
	 * 	inserted
	 * @return String
	 */
	public String blankSpaces(int aiSpaces)
	{
		StringBuffer laStr = new StringBuffer();
		for (int i = 0; i < aiSpaces; i++)
		{
			laStr.append(" ");
		}
		return laStr.toString();
	}
	
	/**
	 * Aligns a String center and automatically points the cursor to
	 * starting of next line
	 *
	 * @param asStr String that needs to be center justified
	 */
	public void center(String asStr)
	{
		int liMidPoint = (int) ciWidth / 2;
		int liStartPoint =
			liMidPoint + (int) asStr.length() / 2 - asStr.length();
		csReportStr.append(blankSpaces(liStartPoint - ciCurrY));
		csReportStr.append(
			asStr + System.getProperty("line.separator"));
		ciCurrX++; //Incrementing X to signal a new line
		ciCurrY = 1; //Resetting Y to 1
	}
	
	/**
	 * center
	 * 
	 * @param asStr String
	 * @param aiLeftbound int
	 * @param aiRighttbound int
	 */
	public void center(String asStr, int aiStartPoint, int aiLength)
	{
		int liMidPoint = aiStartPoint + (int) aiLength / 2;
		String lsSpaces = blankSpaces(
				liMidPoint - (int) asStr.length() / 2 - ciCurrY);
		csReportStr.append(lsSpaces);
		ciCurrY = ciCurrY + lsSpaces.length();
		csReportStr.append(asStr);
		ciCurrY = ciCurrY + asStr.length();
		csReportStr.append(
			blankSpaces(aiStartPoint + aiLength - ciCurrY));
		ciCurrY = aiStartPoint + aiLength;
	}
	
	/**
	 * doubleDashes
	 * Write a user defined number of double dashes
	 * 
	 * @param aiNumOfDashes int
	 * @return String
	 */
	public String doubleDashes(int aiNumOfDashes)
	{
		StringBuffer laDashes = new StringBuffer();
		for (int i = 0; i < aiNumOfDashes; i++)
		{
			laDashes.append("=");
		}
		return laDashes.toString();
	}
	
	/**
	 * Appends the StringBuffer csReportStr with a dashed line
	 */
	public void drawDashedLine()
	{
		for (int i = 0; i < ciWidth; i++)
		{
			csReportStr.append("-");
		}
		csReportStr.append(System.getProperty("line.separator"));
		ciCurrX++; //Incrementing X to signal a new line
		ciCurrY = 1; //Resetting Y to 1
	}
	
	/**
	 * Method to return report width 
	 * 
	 * @return
	 */
	public int getWidth()
	{
		return ciWidth; 	
	}
	
	/**
	 * Method to return current line number of the report
	 * 
	 * @return int
	 */
	public int getCurrX()
	{
		return ciCurrX;
	}
	
	/**
	 * Return the current Page Number.
	 * Primarily for use in CountyWide and SubStation Summary
	 * 
	 * @return int ciPages
	 */
	public int getPages()
	{
		return ciPages;
	}
	
	/**
	 * getReport
	 * 
	 * @return StringBuffer
	 */
	public StringBuffer getReport()
	{
		return csReportStr;
	}
	
	/**
	 * Method to create a new Page.
	 * Page number is automatically incremented
	 */
	public void newPage()
	{
		int i = ciCurrX;
		while (i < ciLinesPerPage)
		{
			csReportStr.append(System.getProperty("line.separator"));
			i++;
		}
		ciCurrX = 1; //Resetting X to 1
		ciCurrY = 1; //Resetting Y to 1
		ciPages++; //Incrementing pageNumber
	}
	
	/**
	 * Method to position current co-ordinates to the next line
	 */
	public void nextLine()
	{
		csReportStr.append(System.getProperty("line.separator"));
		ciCurrX++;
		ciCurrY = 1;
	}
	
	/**
	 * Appends a String to the StringBuffer csReportStr.  The position
	 * of the cursor is right after the String is writter and on the
	 * same line
	 *
	 * @param asStr String that would be appended to the
	 * 	StringBuffer.
	 */
	public void print(String asStr)
	{
		if (asStr == null)
		{
			asStr = "";
		}
		csReportStr.append(asStr);
		ciCurrY = ciCurrY + asStr.length();
	}
	
	/**
	 * Appends a String to the StringBuffer csReportStr.
	 * If the string length is shorter than the specified length, then
	 * padding is required.  If the string length is longer than the
	 * specified length, then truncation is required.  In either case,
	 * the length of the string is that of the specified length
	 *
	 * @param asStr String that needs to be printed
	 * @param aiLength int length that the String will occupy.
	 */
	public void print(String asStr, int aiLength)
	{
		if (asStr == null)
		{
			asStr = "";
		}
		if (asStr.length() < aiLength)
		{
			//padding required
			String lsPadding = "";
			for (int i = 0; i < aiLength - asStr.length(); i++)
			{
				lsPadding += " ";
			}
			csReportStr.append(asStr + lsPadding);
			ciCurrY = ciCurrY + aiLength;
		}
		else if (asStr.length() > aiLength)
		{
			//truncation required
			csReportStr.append(asStr.substring(0, aiLength));
			ciCurrY = ciCurrY + aiLength;
		}
		else
		{
			csReportStr.append(asStr);
			ciCurrY = ciCurrY + asStr.length();
		}
	}
	
	/**
	 * Appends a String to the StringBuffer csReportStr starting at a
	 * specific point If the string length is shorter than the specified
	 * length, then padding is required.  If the string length is longer
	 * than the specified length, then truncation is required.  In
	 * either case, the length of the string is that of the specified
	 * length
	 *
	 * @param asStr String that needs to be printed
	 * @param aiStartPt int The starting point where the string will be
	 * 	printed
	 * @param aiLength int length that the String will occupy.
	 */
	public void print(String asStr, int aiStartPt, int aiLength)
	{
		if (asStr == null)
		{
			asStr = "";
		}
		if (ciCurrY < aiStartPt)
		{
			int liSpaces = aiStartPt - ciCurrY;
			for (int i = 0; i < liSpaces; i++)
			{
				csReportStr.append(" ");
				ciCurrY++;
			}
		}
		if (asStr.length() < aiLength)
		{
			//padding required
			String lsPadding = "";
			for (int i = 0; i < aiLength - asStr.length(); i++)
			{
				lsPadding += " ";
			}
			csReportStr.append(asStr + lsPadding);
			ciCurrY = ciCurrY + aiLength;
		}
		else if (asStr.length() > aiLength)
		{
			//truncation required
			csReportStr.append(asStr.substring(0, aiLength));
			ciCurrY = ciCurrY + aiLength;
		}
		else
		{
			csReportStr.append(asStr);
			ciCurrY = ciCurrY + asStr.length();
		}
	}
	
	/**
	 * printAttributes
	 * 
	 * @param asStr String
	 */
	public void printAttributes(String asStr)
	{
		if (asStr == null)
		{
			asStr = "";
		}
		csReportStr.append(
			asStr + System.getProperty("line.separator"));
		ciCurrX = 1; //Resetting X to 1
		ciCurrY = 1;
	}
	
	/**
	 * printAttributesNoReturn
	 * 
	 * @param asStr String
	 */
	public void printAttributesNoReturn(String asStr)
	{
		if (asStr == null)
		{
			asStr = "";
		}
		csReportStr.append(asStr);
	}
	
	/**
	 * Appends a String to the StringBuffer csReportStr and place the
	 * cursor to the next line. ie automatic carriage return
	 *
	 * @param asStr String that needs to be printed
	 */
	public void println(String asStr)
	{
		if (asStr == null)
		{
			asStr = "";
		}
		csReportStr.append(
			asStr + System.getProperty("line.separator"));
		ciCurrX++;
		ciCurrY = 1;
	}
	
	/**
	 * Appends a String rightmost Aligned to the StringBuffer
	 * csReportStr. Automatic carriage return
	 *
	 * @param asStr String that needs to be rightmost justified
	 * 	and printed
	 */
	public void rightAlign(String asStr)
	{
		int liStartPoint = ciWidth - asStr.length();
		csReportStr.append(blankSpaces(liStartPoint - ciCurrY));
		csReportStr.append(asStr +
			System.getProperty("line.separator"));
		ciCurrY = 1;
		ciCurrX = 1;
		ciPages++;
	}
	
	/**
	 * Appends a String right Aligned to the StringBuffer csReportStr.
	 * Automatic carriage return
	 *
	 * @param asStr String that needs to be right justified and
	 * 	printed
	 * @param aiStartPt int Starting position of the string to be right
	 * 	justified
	 * @param aiLength int Length within which the string should be
	 * 	right justified
	 */
	public void rightAlign(String asStr, int aiStartPt, int aiLength)
	{
		if (ciCurrY < aiStartPt)
		{
			int liSpaces = aiStartPt - ciCurrY;
			for (int i = 0; i < liSpaces; i++)
			{
				csReportStr.append(" ");
				ciCurrY++;
			}
		}
		int liStartPoint = ciCurrY + aiLength - asStr.length();
		csReportStr.append(blankSpaces(liStartPoint - ciCurrY));
		csReportStr.append(asStr);
		ciCurrY = aiStartPt + aiLength;
	}
	
	/**
	 * Method to set the footer margin
	 * This method appends blank spaces to the remaining lines of the
	 * page
	 * 
	 * @param aiX int
	 */
	public void setFooter(int aiX)
	{
		for (int i = ciCurrX; i <= ciLinesPerPage - aiX; i++)
		{
			csReportStr.append(System.getProperty("line.separator"));
		}
		ciCurrY = 1;
		ciCurrX = ciLinesPerPage - aiX;
	}
	
	/**
	 * Set the Page Number.
	 * Primarily for use in CountyWide and SubStation Summary.
	 * 
	 * @param aiPageNumber int
	 */
	public void setPages(int aiPageNumber)
	{
		ciPages = aiPageNumber;
	}
	
	/**
	 * singleDashes
	 * Write a user defined number of single dashes
	 * 
	 * @param aiNumOfDashes int
	 */
	public String singleDashes(int aiNumOfDashes)
	{
		StringBuffer laDashes = new StringBuffer();
		for (int i = 0; i < aiNumOfDashes; i++)
		{
			laDashes.append("-");
		}
		return laDashes.toString();
	}
}
