#Author:aparnarejo877@
Feature: Feature to verify the create user API request

  Scenario Outline: verify the user API request is unsuccessful with invalid data format
    Given Invalid user data are available from "<SheetName>" and <RowNumber>
    When I add new user Invalid data to list
    Then the <errorCode> errorcode should be displayed

    Examples: 
      | SheetName | RowNumber | errorCode |
      | Sheet     |         0 |       422 |
      | Sheet     |        1 |       422 |

  Scenario Outline: verify the user API request is unsuccessful with invalid data format
    Given user data are available from "<SheetName>" and <RowNumber>
    When I add new user data to list
    Then the user data should be added
    When I update the data with duplicate values
    Then the <error> error should be displayed
    
    Examples: 
      | SheetName | RowNumber | error |
      | Sheet     |         0 |      422 |
      | Sheet     |         1 |      422 |
