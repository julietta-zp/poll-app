# Poll Application (Spring Boot, REST, Json)
This is a simple server standalone application. 

### Features
It provides the following features for registered application:
* create a new poll
* add options for voting
* access to the poll via auto-generated links
* vote by reference 
* close the poll by reference
* monitor results of the poll by reference (show statistics with option and total votes counter)

### Poll Controller
Contains general poll managing logic and validation.

Method	| Path	| Description	
------------- | ------------------------- | ------------- 
GET	| /polls	| Get all created polls	
GET	| /polls/{pollHash}	| Get specified poll
PUT	| /polls/{pollHash}	| Update specified poll
PUT	| /close	| Close poll specified in param (@RequestParam String pollHash)
POST	| /polls	| Create new poll

### Vote and Results Controllers
Method	| Path	| Description	
------------- | ------------------------- | ------------- 
GET	| /results	| Get results of the poll specified in param (@RequestParam String pollHash)
GET	| /polls/{pollHash}/votes	| Get all votes in specified poll
POST	| /polls/{pollHash}/votes	| Vote for particular option in specified poll


