# Compute a score for list of names
This project is calculate the score for list of name and can be extended to enhance or change the algorithm to calculate the score.By default it is implementing the below algorithm. 
Compute Score By First -  Read all the comma separate name from file and sum up all the char's int value (A=1,B=2...Z=26) and multiply the summed value with index of name after sorting.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Assumptions
- All the name should contains chars from [A-Z or a-Z] to calculate the score correctly.
- If name contains small letters then it will be converted to upper case and then calculate the score.
- Performing the in-memory computation if file size is less then 50KB. This value is configuration through properties.
- Performing computation using external sorting in chunks (each chunk of 20KB size and configurable through properties) if file size is greater then 50KB

### Prerequisites
- Java 8 or later must be installed on your machine

### Installing / Running
If you have maven installed on your machine then run the below maven command.
```
For Windows :
	mvnw.cmd spring-boot:run
	
For Linux
	.\mvnw spring-boot:run
```
If you don't have the maven installed on your machine then run the below maven command.
```
mvn spring-boot:run
```
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
### Installing / Running
Here are some testing screenshots
![Score Calculation](/Testing_Guidence.JPG?raw=true)

## Author
* **Subhash Chand** - [GitHub Link](https://github.com/subhash982)
