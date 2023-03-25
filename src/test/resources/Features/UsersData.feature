#Author:aparnarejo877@
Feature: Feature to verify the create user API request

 Scenario Outline: verify the user API request is successful 
    Given  user data are available from "<SheetName>" and <RowNumber>
    When I add new user data to list
    Then the user data should be added
    When I update the data
    Then the list should be updated
    When I change the data
    Then the list should be updated
    When i remove data from list
    Then the user data should be removed
    When i get the deleted user data
    Then the error should be displayed 

    Examples: 
      | SheetName | RowNumber | 
      | Sheet     |         0 |       
      | Sheet     |         1 |
         
     