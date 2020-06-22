# Bookstore-SpringBoot-REST-DemoApp

## Database Design

#### Entities/Tables - RDS: 
1. `CatalogItem`: any product in the bookstore is a catalogItem. Fields are `catalogItemId`(Primary Key, Auto generated Sequence),
`createdAt`(Date), `updatedAt`(Date)

2. `Book`: fields which map to a specific isbn. Fields are `catalogItemId`(Primary Key, Foreign Key referencing CatalogItem), 
`isbn`(String, unique, not null), `title`(String, not null), `author`(String not null)

3. `Sku`: Sku stores stock and price data for a catalogItem. Fields are `skuId`(Primary Key, Auto generated Sequence), 
`catalogItemId`(Foreign Key referencing catalog item, not null), `quantity`(integer, not null, >=0), 
`price`(double, not null, >=0), `createdAt`(Date), `updatedAt`(Date)

4. `Purchase`: Stores data for every purchase made. Fields are `purchaseId`(Primary Key, Auto generated Sequence), `skuId`(foreign 
key referencing Sku), `purchasedQuantity`(integer, not null, >=0), `purchasePrice`(double, not null, >=0), `createdAt`(Date),
`updatedAt`(Date)

This separation of catalog from book details and mapping of sku to catalogItem makes the design extensible if a new product 
is added after books (with the only change being creation of a new table for product specific details).  Also, in the case
where same catalog item may have different prices/availabilities, the ability to map of multiple skus to a single catalog can solve the
problem.


#### Elastic Search:
Elastic search has been incorporated which stores the aggregated data for each product (book in this case) in a separate index.
The aggregated data consists of `CatalogItem` details and details specific to the product as well as list of `Sku` objects.
Quantity is excluded from the `Sku` objects since it is a highly dynamically changing value.

Currently, this elastic search is being used to provide full text search over `title` and `author` of the book.


## Code Structure
The code follows a simple controller - service - manager/repository layered architecture.

##### Controller Layer
The response object of all controllers follow a defined structure 
`{
     "code": 201,
     "data": {...}
     },
     "error": {...}
 }`
 which is defined as  `class RestResponse<T>` and associated with a corresponding builder for with-data and with-error
 cases.
 
 The Controller Advice takes care of different exceptions and maps it to a corresponding response.
 
 
 ##### Service Layer
 The requirements and business logic is defined in the service layer.
 
 
 ##### Manager/Repository Layer
 This layer is responsible to handle and abstract out external interactions (with other APIs or database transactions) from
 the service layer.
 
 Netflix `Hystrix` has been integrated as a circuit-breaker in case external APIs fail.


## REST APIs
##### Create Book
Adds a new book to the bookstore

`curl --location --request POST 'localhost:9000/books' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "book": {
         "isbn": "100000001",
         "title": "Harry Potter and The Chamber of Secrets",
         "author": "JK Rowling",
         "price": 1000,
         "quantity": 10
     }
 }'`


##### Search Book
Searches books on the basis of given filters. Title and Author searches allow for partial matches. Returns paginated results,
pageNumber and pageSize are mandatory parameters. pageNumber starts from 0.

`curl --location --request GET 'localhost:9000/books?pageNumber=0&pageSize=10&isbn=100000000&title=Harry&author=Rowling' \
 --data-raw ''`


##### Create Purchase
Creates a purchase against a sku Id.

`curl --location --request POST 'localhost:9000/purchases' \
 --header 'Content-Type: application/json' \
 --data-raw '{
 	"skuId": 1,
 	"quantity": 5
 }'`


##### Fetch Media Coverage
Fetches media coverage given a isbn(as path variable)

`curl --location --request GET 'localhost:9000/mediaCoverage/100000000'`




    