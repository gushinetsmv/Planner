Planner
=======

Employee report planner

1. Project directory stucture:

	db - full database dump

	lib - 3rd-party libs

	src - sources

	src\main\resources\confing.properties - configuration properties file for database connection and context name

2. ANT tasks:
	
	"ant war" (or just "ant") - build .war
	
	"ant restoreDB" - creates new table in database and fills it with db/planner.dump schema

3. Some implementation details...
