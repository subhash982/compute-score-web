# Compute a score for list of names
This project is calculate the score for list of names and can be extended to enhance or change the algorithm to calculate the score.By default it is implementing the below algorithm. 
<br/>
<b>Compute Score By First Name:</b>  Read all the comma separate name from file and sum up all the char's int value (A=1,B=2...Z=26) and multiply the summed value with index of name after sorting.
<br/>
<b>Compute Score By Full Name:</b> Default implementation provided same as First name but can be changed accordingly. 

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Assumptions
- All the name should contains chars from [A-Z or a-Z] to calculate the score correctly.If it contains other characters then score calculation will not as expected.
- If name contains small letters then it will be converted to upper case and then calculate the score.
- Performing the in-memory computation if file size is less then **50KB**. This value is configuration through properties.
- Performing computation using external sorting in chunks (each chunk of **20KB** size and configurable through properties) if file size is greater then **50KB**.
- Only implemented for first name based algorithm but can be extended for other algorithms. 
- Entire data should be in single line or can be splitted into multiple lines but multiple line shouldn't have different data set. Here are the some examples for valid / invalid data.
<pre>
<b>Valid Records :</b>
1) "MARY","PATRICIA" 
	Score=211
	
2) "MARY","PATR
	ICIA"  
	Score=211
	
3) "MARY",
	"PATRICIA" 
	Score =211
<b>Note:</b> Record will be converted to "MARY","PATRICIA" before processing and then score will be calculated.

<b>Invalid Records</b>
"MARY"
"PATRICIA"
Score = 134

<b>Note:</b>This record will be converted to "MARYPATRICIA" before processing and then score will be calculated.

</pre>
### Prerequisites
- Java 8 or later must be installed on your machine

### Installing / Running
Run the following command
<pre>
<b>For Windows :</b>
	mvnw.cmd spring-boot:run
	
<b>For Linux : </b>
	.\mvnw spring-boot:run
</pre>
After running the application, You can verify the application in two ways
- From Browser
```
http://localhost:8081/

After loading the application, Fill the form and submit to compute the score.

```
- From Postman / Rest Client
```
API URL : http://localhost:8081/api/score
HTTP Method : POST
Request Parameter : 
	file - Select the file using file picker
	algorithm - Type scoring algorithm. Possible values are from [scoreByFirstName,scoreByFullName]
```
### Testing Guidance
Here are some testing screenshots
![Score Calculation](/Testing_Guidance.JPG?raw=true)

## Author
* **Subhash Chand** - [GitHub Link](https://github.com/subhash982)
