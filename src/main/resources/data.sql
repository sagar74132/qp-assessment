-- Grocery Items
CREATE TABLE grocery_items
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    description TEXT,
    category    VARCHAR(255),
    quantity    INT            NOT NULL CHECK (quantity >= 0),
    is_deleted  INT DEFAULT 0
);

-- Users (if you need user data for orders)
CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(50)         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted INT       DEFAULT 0
);

-- Orders
CREATE TABLE orders
(
    id          UUID PRIMARY KEY,
    user_id     UUID           NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    is_deleted  INT         DEFAULT 0,
    status      VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Order Items
CREATE TABLE order_items
(
    id              UUID PRIMARY KEY,
    order_id        UUID           NOT NULL,
    grocery_item_id UUID           NOT NULL,
    quantity        INT            NOT NULL CHECK (quantity > 0),
    price           DECIMAL(10, 2) NOT NULL,
    is_deleted      INT DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (grocery_item_id) REFERENCES grocery_items (id)
);

-- Payments table
CREATE TABLE payments
(
    id           UUID PRIMARY KEY,
    order_id     UUID NOT NULL,
    status       VARCHAR(50) DEFAULT 'PENDING',
    payment_date TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);
