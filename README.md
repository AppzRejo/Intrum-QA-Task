# Intrum-QA-Task

1.Requirements
Install Java.
Install Eclipse IDE for Java.
Install Maven.
Install Selenium Webdriver.
Install JUnit.
Install Cucumber.

2.How to run tests 
Step 1: Create TestRunner class in src/test/java>StepDefinition folder
Step 2:Add the gluecode or feature classes before the TestRunner class definition as follows
@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/Features",glue= {"StepDefinitions"})
Step 3:RightClick on the TestRunner class file and select Run as >Junit Test.
Step 4.Test scenarios will be displayed in Junit window along with their results 


3.How to generate reports

To generate reports Run the project as above

Step 1: Select project folder ,RightClick >Select Refresh
Step 2: Go to the Target folder > JSONReports /JunitReports , Also the html report can be found in the cucumber-report.html
Step 3: Click on the html report ,Number of passed and failed test will be displayed 