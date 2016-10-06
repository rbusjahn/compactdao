#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios 
#<> (placeholder)
#""
## (Comments)

#Sample Feature Definition Template
@acceptance_test
Feature: Compact Dao CRUD DAO
	As a developer I want to have a simple crud DAO class.
Scenario: I want to load a model entity.
Given A model entity
And A crud DAO 
When a model entity is loaded
Then the model entity contains all database table fields
Then the model entity id is loaded 

Scenario: I want to persist a model entity.
Given A model entity
And A crud DAO 
When a model entity is persisted
Then the model entity has a database ID
Then the model entity can be reloaded from database
Scenario: I want to delete a model entity.
Given A model entity
And A crud DAO 
When a model is deleted
Then I cannot find a model entity by its ID anymore.


Scenario: I want to update a model entity.
Given A model entity
Given A crud DAO
When that model entity is updated and reloaded
Then The previous made changes are applied to that model entity.

