# DatabasesProject
## 1. Classes
### 1.1 main package
In this package, we have added a class (Main) that starts our project. In
fact, it has an attribute that references to the business logic singleton
instance (explained later) that it is used to access to the different 
transactions in the project.

Using the *start()* method, we ask the user to select a transaction (from
the console), and when selected, a method from the business logic is called
(with the needed parameters).

### 1.2 businessLogic package
In this package, we have a BlFacadeImplementation class, in which we have added all the methods that call to the dataAccess package methods (having created in the class the instance for calling it).
That is, the transactions and queries were implemented using this class (in which each method represents a single query or transaction) but they can internally call more than once to the database manager to get or insert information. 

### 1.3 dataAccess package

