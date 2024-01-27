CREATE TABLE users
(
    id                SERIAL PRIMARY KEY,
    telegram_id       VARCHAR(255),
    telegram_username VARCHAR(255),
    phone_number      VARCHAR(255)
);

CREATE TABLE product
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255),
    url  VARCHAR(255)
);

CREATE TABLE price
(
    id         SERIAL PRIMARY KEY,
    price      DOUBLE PRECISION,
    price_date DATE,
    product_id SERIAL REFERENCES product (id)
);

CREATE TABLE user_subscriptions
(
    user_id   SERIAL,
    product_id SERIAL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (product_id) REFERENCES product (id),
    UNIQUE (user_id, product_id)
);
