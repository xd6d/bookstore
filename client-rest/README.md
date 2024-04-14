### Simple demonstration of some primitive RESTful client

I haven't put a lot of effort at this, so this API could be easily broken. Consider this just as Proof Of Concept

### Execution

``./gradlew clean bootRun``


### Example requests

---

#### Create book's record
```
POST localhost:8081/api/book

Content-Type: application/json

{
    "author": "Example Author",
    "title": "Some title",
    "isbn": "97845316",
    "quantity": 30
}
```

Expected Response: 

```
200 OK

{
    "id": "dadfa8a4-92cc-4a87-ab0c-ca6b932531c7",
    "title": "Some title",
    "author": "Example Author",
    "isbn": "97845316",
    "quantity": 30
}
```

---

#### Get all books matching example
```
GET localhost:8081/api/book

Content-Type: application/json

{
    "author": "",
    "title": "",
    "isbn": "",
    "quantity": 0
}
```
Note: the request above means 'Get all without any filters'

Expected Response:

```
200 OK

[
    {
        "id": "067c0818-8893-4c34-8232-e5a7b356f45c",
        "title": "The Metamorphosis",
        "author": "Franz Kafka",
        "isbn": "123",
        "quantity": 5
    },
    {
        "id": "b46db26c-9630-4326-9951-8835c6fdf8a1",
        "title": "test",
        "author": "test",

        ...

        "isbn": "97845316",
        "quantity": 30
    }
]
```

---

#### Update book's record
```
PUT localhost:8081/api/book

Content-Type: application/json

{
    "id": "61384666-757e-484d-acd0-226d461bf1a5",
    "author": "test update",
    "title": "update test",
    "isbn": "0000000001",
    "quantity": 51
}
```

Expected Response:

```
200 OK
```

---

#### Delete book's record
```
DELETE localhost:8081/api/book

Content-Type: application/json

{
    "id": "61384666-757e-484d-acd0-226d461bf1a5",
    "author": "test update",
    "title": "update test",
    "isbn": "0000000001",
    "quantity": 51
}
```

Expected Response:

```
200 OK
```