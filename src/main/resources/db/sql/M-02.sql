ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT now();

ALTER TABLE product ADD COLUMN created_at TIMESTAMP DEFAULT now();

ALTER TABLE price ADD COLUMN created_at TIMESTAMP DEFAULT now();

ALTER TABLE user_subscriptions ADD COLUMN created_at TIMESTAMP DEFAULT now();

