<h1>About</h1> 

1. Reading excel files from a specified directory and creating table based on the 
excel name and columns based on the excel header  and storing the values in database.<br>
2. The excel file will be removed from the directory once its read write operation completes.

<h3>Annotations Used : </h3>
1. The **@EnableScheduling** annotation is required to enable support for scheduled tasks in Spring.
It tells Spring to look for methods annotated with @Scheduled and schedule them according to the specified 
parameters.
2. The **@Scheduled** annotation is used in Spring to define methods that should be executed at fixed 
intervals or based on a cron expression.
3. The **@Value** to read values from the **application.properties** file.
4. Both **schedule.time** and **directory.name** values are set in the properties file.
5. The **@PersistenceContext** annotation in Java is used in JPA to
   inject an EntityManager into a Spring-managed bean (such as services or repositories).
6. The **@PersistenceContext** is used in custom repository implementations when you need to write 
custom queries or interact with the EntityManager directly
7. **@Transactional** is an annotation in Spring that tells the system to treat a method (or all methods in a class)
as a transaction. A transaction is a set of operations that must all happen successfully together. 
If one part of the transaction fails, the whole transaction is rolled back, meaning nothing gets saved to the 
database.
8. **@Transactional** in Spring is a way to group multiple operations into a single transaction. 
It makes sure that either all operations succeed together or everything is undone if something goes wrong.
It's like a safety net that keeps your data consistent.

<h3>Dependencies : </h3>

1. **Apache POI** (Poor Obfuscation Implementation) is a Java library used to handle various Microsoft Office 
document formats (such as Excel, Word, and PowerPoint) in Java applications. 
It provides a way to read, write, and manipulate documents in these formats without needing Microsoft Office 
software installed.
   `<dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.3</version>
   </dependency>`


<h3>MISC : </h3>
* **Workbook** and **Sheet** are part of the Apache POI library, which is used for handling Microsoft Excel files
* A **Workbook** in Apache POI represents an entire Excel file. It contains all the sheets within the file.
* It can contain multiple sheets, so a Workbook serves as the main object that you interact with when working with Excel files in Apache POI.
* A **Sheet** represents a single sheet within the Excel workbook. An Excel file can contain multiple sheets, but here we are working with the first one.
* **sheet** is retrieved from the workbook using the getSheetAt(0) method. The argument 0 refers to the first sheet (Excel sheets are indexed starting from 0).


* **Workbook** is the object that represents the entire Excel file (it could have multiple sheets). <br>
  **Sheet** is an individual sheet within that Excel file, where you can access rows and columns of data.
* A **Row** represents a single row in an Excel sheet.
* A **Row** contains multiple **Cell** objects, each representing a specific cell within that row (like a column entry).
* A **Cell** represents a single cell in an Excel sheet. It holds the data of a specific column in a specific row.