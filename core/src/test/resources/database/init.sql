-- init.sql - SQLite 内存数据库初始化脚本

-- 创建用户表
CREATE TABLE IF NOT EXISTS users
(
    id
    INTEGER
    PRIMARY
    KEY
    AUTOINCREMENT,
    username
    TEXT
    NOT
    NULL
    UNIQUE,
    password
    TEXT
    NOT
    NULL,
    email
    TEXT
    NOT
    NULL
    UNIQUE,
    created_at
    TIMESTAMP
    DEFAULT
    CURRENT_TIMESTAMP,
    is_active
    BOOLEAN
    DEFAULT
    1
);

-- 创建产品表
CREATE TABLE IF NOT EXISTS products
(
    id
    INTEGER
    PRIMARY
    KEY
    AUTOINCREMENT,
    name
    TEXT
    NOT
    NULL,
    description
    TEXT,
    price
    DECIMAL
(
    10,
    2
) NOT NULL,
    stock INTEGER DEFAULT 0,
    category_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY
(
    category_id
) REFERENCES categories
(
    id
)
    );

-- 创建分类表
CREATE TABLE IF NOT EXISTS categories
(
    id
    INTEGER
    PRIMARY
    KEY
    AUTOINCREMENT,
    name
    TEXT
    NOT
    NULL
    UNIQUE,
    description
    TEXT
);

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders
(
    id
    INTEGER
    PRIMARY
    KEY
    AUTOINCREMENT,
    user_id
    INTEGER
    NOT
    NULL,
    order_date
    TIMESTAMP
    DEFAULT
    CURRENT_TIMESTAMP,
    total_amount
    DECIMAL
(
    10,
    2
) NOT NULL,
    status TEXT DEFAULT 'pending',
    FOREIGN KEY
(
    user_id
) REFERENCES users
(
    id
)
    );

-- 创建订单详情表
CREATE TABLE IF NOT EXISTS order_items
(
    id
    INTEGER
    PRIMARY
    KEY
    AUTOINCREMENT,
    order_id
    INTEGER
    NOT
    NULL,
    product_id
    INTEGER
    NOT
    NULL,
    quantity
    INTEGER
    NOT
    NULL,
    unit_price
    DECIMAL
(
    10,
    2
) NOT NULL,
    FOREIGN KEY
(
    order_id
) REFERENCES orders
(
    id
),
    FOREIGN KEY
(
    product_id
) REFERENCES products
(
    id
)
    );

-- 插入初始分类数据
INSERT INTO categories (name, description)
VALUES ('Electronics', 'Electronic devices and accessories'),
       ('Clothing', 'Apparel and fashion items'),
       ('Books', 'Books and educational materials'),
       ('Home', 'Home and kitchen products');

-- 插入初始产品数据
INSERT INTO products (name, description, price, stock, category_id)
VALUES ('Smartphone X', 'Latest model smartphone with advanced features', 799.99, 100, 1),
       ('Wireless Headphones', 'Noise cancelling wireless headphones', 199.99, 50, 1),
       ('Cotton T-Shirt', '100% cotton comfortable t-shirt', 24.99, 200, 2),
       ('Programming Book', 'Comprehensive guide to modern programming', 49.99, 30, 3),
       ('Coffee Maker', 'Automatic drip coffee maker', 89.99, 40, 4);

-- 插入测试用户数据
INSERT INTO users (username, password, email)
VALUES ('admin', 'hashed_password_here', 'admin@example.com'),
       ('johndoe', 'hashed_password_here', 'john.doe@example.com'),
       ('janedoe', 'hashed_password_here', 'jane.doe@example.com');

-- 插入测试订单数据
INSERT INTO orders (user_id, total_amount, status)
VALUES (2, 1049.98, 'completed'),
       (3, 74.98, 'shipped');

-- 插入订单详情数据
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
VALUES (1, 1, 1, 799.99),
       (1, 2, 1, 199.99),
       (2, 3, 2, 24.99),
       (2, 5, 1, 89.99);

-- 创建索引提高查询性能
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);
