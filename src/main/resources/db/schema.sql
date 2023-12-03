CREATE TABLE price
(
    id          SERIAL PRIMARY KEY,
    product_name VARCHAR(255),
    price       DOUBLE PRECISION,
    price_date   DATE,
    product_url  VARCHAR(255)
);
