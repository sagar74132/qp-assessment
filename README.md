# Goal

* To build grocery application as per
  instructed [here](https://surveys.questionpro.com/a/TakeSurvey?tt=End01kiL3M8ECHrPeIW9eQ%3D%3D&custom1=29542859&firstname=Sagar&lastname=Singh&stack=JAVA).

1. Admin Responsibilities:
    - Add new grocery items to the system
    - View existing grocery items
    - Remove grocery items from the system
    - Update details (e.g., name, price) of existing grocery items
    - Manage inventory levels of grocery items
2. User Responsibilities:
    - View the list of available grocery items
    - Ability to book multiple grocery items in a single order

- Containerize the application using Docker for ease of deployment and scaling.
  Database:
- Use any relational database of your choice.

# Important Notes

* Docker desktop or any lite-docker env must be available.
* For first, execute `docker-compose build` command from project's root directory.
    - This will create and build docker image with all dependencies.
* Once above command gets executed, we need to make the web server up. To do this execute below command
    - `docker-compose up` from project's root directory
* Above command will make the postgres and application up. Executes all the sql statements
  from `src/main/resources/data.sql` file.
* For testing purpose, below users will get created which can be used to call the endpoints:
    - Email: `admin_user@qp.com` Password: `1234` Role: _ADMIN_
    - Email: `user@qp.com` Password: `1234` Role: _USER_
* By this point everything is set up. It's time to call the APIs.
* Application will be running on port `8080` in `localhost`. We can call the Swagger-UI OR Postman to test the
  endpoints.
    - Swagger-UI endpoint: [Swagger-UI](http://localhost:8080/swagger-ui/index.html).
    - Import `QP Grocery Application.postman_collection.json` in Postman.

# Tests

* First we have to call the login API. Go to section `login-controller` (in swagger-ui and click on `Try it out`).
* Login with ADMIN user using above-mentioned cred.
    - Response
      ```json
      {
      "token": "eyJhbGciOiJ.....Y07I99HA",
      "message": "Login success."
      }
      ```
* Inside Postman, we are ready to call other APIs but in Swagger-UI we have to copy the token returned from login API
  and then scroll at the top and click on `Authorize` button and paste this token and save.
* In the `Grocery Controller`, user `create` API to create a grocery item as shown below.

### Payload

```json
[
  {
    "name": "Mango",
    "price": 120,
    "description": "Fresh mangoes",
    "category": "Fruits/Vegetables,Dairy,Snacks,Sweets",
    "quantity": 100
  },
  {
    "name": "Cow Milk",
    "price": 35,
    "description": "1L cow milk",
    "category": "Dairy",
    "quantity": 150
  }
]
```

### Response

```json
{
  "data": [
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Mango",
      "price": 120,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 100
    },
    {
      "id": "97a68282-38be-451e-b0f9-1492d080b9df",
      "name": "Cow Milk",
      "price": 35,
      "description": "1L cow milk",
      "category": "Dairy",
      "quantity": 150
    }
  ],
  "message": "All items added successfully",
  "status": "CREATED",
  "errors": [],
  "success": [
    "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
    "97a68282-38be-451e-b0f9-1492d080b9df"
  ]
}
```

* Now we have created two items Mango & Cow Milk
* Now, login with user-role id from above cred.
    - Payload
      ```json
      {
      "email": "user@qp.com",
      "password": "1234"
      }
      ```
    - Response
      ```json
      {
      "token": "eyJhbGc.....dmXzVQJVgQ",
      "message": "Login success."
      }
      ```

* Let's place order and try to book multiple items in single order. Goto `Order Controller` > `Create Order`

### Payload

```json
[
  {
    "groceryItemId": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
    "quantity": 10
  },
  {
    "groceryItemId": "97a68282-38be-451e-b0f9-1492d080b9df",
    "quantity": 2
  }
]
```

### Response

```json
{
  "data": {
    "orderId": "1193498e-6b62-4df4-bf20-e30423ea6baa",
    "paymentId": "31c021ad-62db-4671-852c-ac19463ed309",
    "orderItems": [
      {
        "groceryItemId": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
        "groceryName": "Mango",
        "quantity": 10,
        "price": 1200.00
      },
      {
        "groceryItemId": "97a68282-38be-451e-b0f9-1492d080b9df",
        "groceryName": "Cow Milk",
        "quantity": 2,
        "price": 70.00
      }
    ],
    "totalPrice": 1270.00,
    "createdAt": "2025-01-26T16:45:15.93008088"
  },
  "message": "Order 1193498e-6b62-4df4-bf20-e30423ea6baa has been placed successfully.",
  "status": "CREATED",
  "errors": null,
  "success": null
}
```

* Order is now successfully placed. Now let's fetch available items. Goto `Grocery Controller` > `Available`

### Response

```json
{
  "data": [
    {
      "id": "7a3a393f-21c6-4eae-953d-a20740ca91e2",
      "name": "Apple",
      "price": 10.00,
      "description": "Fresh",
      "category": "Fruits",
      "quantity": 874
    },
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Mango",
      "price": 120.00,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 90
    },
    {
      "id": "97a68282-38be-451e-b0f9-1492d080b9df",
      "name": "Cow Milk",
      "price": 35.00,
      "description": "1L cow milk",
      "category": "Dairy",
      "quantity": 148
    }
  ],
  "message": "All items fetched successfully",
  "status": "OK",
  "errors": null,
  "success": null
}
```

* Let's book all the mangoes in a single order.

### Payload

```json
[
  {
    "groceryItemId": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
    "quantity": 90
  }
]
```

### Response

```json
{
  "data": {
    "orderId": "bfb9ebc0-d957-4535-a768-97dd0c76cb68",
    "paymentId": "9ee8cdf8-4890-4be7-9adb-fe3caa62a035",
    "orderItems": [
      {
        "groceryItemId": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
        "groceryName": "Mango",
        "quantity": 90,
        "price": 10800.00
      }
    ],
    "totalPrice": 10800.00,
    "createdAt": "2025-01-26T16:49:18.437330298"
  },
  "message": "Order bfb9ebc0-d957-4535-a768-97dd0c76cb68 has been placed successfully.",
  "status": "CREATED",
  "errors": null,
  "success": null
}
```

* Now the item is out of stock and should not come into Available API, let's try to get available items

### Response

```json
{
  "data": [
    {
      "id": "7a3a393f-21c6-4eae-953d-a20740ca91e2",
      "name": "Apple",
      "price": 10.00,
      "description": "Fresh",
      "category": "Fruits",
      "quantity": 874
    },
    {
      "id": "97a68282-38be-451e-b0f9-1492d080b9df",
      "name": "Cow Milk",
      "price": 35.00,
      "description": "1L cow milk",
      "category": "Dairy",
      "quantity": 148
    }
  ],
  "message": "All items fetched successfully",
  "status": "OK",
  "errors": null,
  "success": null
}
```

* Mango is not in the available items.

* Now, let's try to fetch all the grocery items with user-role. As this is Admin API, it is available
  inside `Grocery Controler` > `Admin APIs` in Postman

### Response

```
Access Denied: Full authentication is required to access this resource
```

* Now let's try one more Admin API from user-role. Let's try to update inventory using the
  API `Grocery Controler` > `Admin APIs` > `Update Inventory` in Postman.

### Payload

```json
{
  "8e4a6c32-8400-47c8-b38f-3c437b2adfdf": 190
}
```

### Response

```
Access Denied: Full authentication is required to access this resource
```

* Now let's try to call the above APIs from Admin role.

* First fetch all items from grocery

### Response

```json
{
  "data": [
    {
      "id": "7a3a393f-21c6-4eae-953d-a20740ca91e2",
      "name": "Apple",
      "price": 10.00,
      "description": "Fresh",
      "category": "Fruits",
      "quantity": 874
    },
    {
      "id": "97a68282-38be-451e-b0f9-1492d080b9df",
      "name": "Cow Milk",
      "price": 35.00,
      "description": "1L cow milk",
      "category": "Dairy",
      "quantity": 148
    },
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Mango",
      "price": 120.00,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 0
    }
  ],
  "message": "All items fetched successfully",
  "status": "OK",
  "errors": null,
  "success": null
}
```

* Update the stock of Mangoes and Apple. Goto `Grocery Controller` > `Update Inventory`

### Payload

```json
{
  "7a3a393f-21c6-4eae-953d-a20740ca91e2": 12,
  "8e4a6c32-8400-47c8-b38f-3c437b2adfdf": 190
}
```

### Response

```json
{
  "data": [
    {
      "id": "7a3a393f-21c6-4eae-953d-a20740ca91e2",
      "name": "Apple",
      "price": 10.00,
      "description": "Fresh",
      "category": "Fruits",
      "quantity": 12
    },
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Mango",
      "price": 120.00,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 190
    }
  ],
  "message": "All items updated successfully",
  "status": "OK",
  "errors": [],
  "success": [
    "7a3a393f-21c6-4eae-953d-a20740ca91e2",
    "8e4a6c32-8400-47c8-b38f-3c437b2adfdf"
  ]
}
```

* Let's fetch all items now.

### Response

```json
{
  "data": [
    {
      "id": "97a68282-38be-451e-b0f9-1492d080b9df",
      "name": "Cow Milk",
      "price": 35.00,
      "description": "1L cow milk",
      "category": "Dairy",
      "quantity": 148
    },
    {
      "id": "7a3a393f-21c6-4eae-953d-a20740ca91e2",
      "name": "Apple",
      "price": 10.00,
      "description": "Fresh",
      "category": "Fruits",
      "quantity": 12
    },
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Mango",
      "price": 120.00,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 190
    }
  ],
  "message": "All items fetched successfully",
  "status": "OK",
  "errors": null,
  "success": null
}
```

* Let's delete a grocer item apple from inventory. Goto `Grocery Controller` > `Delete Item`

### Payload

```json
[
  "7a3a393f-21c6-4eae-953d-a20740ca91e2"
]
```

### Response

```json
{
  "data": null,
  "message": "All items deleted successfully",
  "status": "OK",
  "errors": [],
  "success": [
    "7a3a393f-21c6-4eae-953d-a20740ca91e2"
  ]
}
```

* Let's fetch all items

### Response

```json
{
  "data": [
    {
      "id": "97a68282-38be-451e-b0f9-1492d080b9df",
      "name": "Cow Milk",
      "price": 35.00,
      "description": "1L cow milk",
      "category": "Dairy",
      "quantity": 148
    },
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Mango",
      "price": 120.00,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 190
    }
  ],
  "message": "All items fetched successfully",
  "status": "OK",
  "errors": null,
  "success": null
}
```

* Update the grocery Mangoes and rename it to Apple also update the price also.
  Goto `Grocery Controller` > `Update Item`

### Payload

```json
[
  {
    "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
    "name": "Apple",
    "price": 199,
    "quantity": 122
  }
]
```

### Response

```json
{
  "data": [
    {
      "id": "8e4a6c32-8400-47c8-b38f-3c437b2adfdf",
      "name": "Apple",
      "price": 199,
      "description": "Fresh mangoes",
      "category": "Fruits",
      "quantity": 122
    }
  ],
  "message": "All items updated successfully",
  "status": "OK",
  "errors": [],
  "success": [
    "8e4a6c32-8400-47c8-b38f-3c437b2adfdf"
  ]
}
```