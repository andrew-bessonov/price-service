CREATE TABLE price
(
    id          SERIAL PRIMARY KEY,
    product_name VARCHAR(255),
    price       DOUBLE PRECISION,
    price_date   DATE,
    product_url  VARCHAR(255)
);

CREATE TABLE url
(
    id             SERIAL PRIMARY KEY,
    product_url    VARCHAR(255),
    is_need_update BOOLEAN
);
