CREATE USER analyzer_user WITH PASSWORD 'analyzer_password';
CREATE DATABASE analyzer OWNER analyzer_user;
GRANT ALL PRIVILEGES ON DATABASE analyzer TO analyzer_user;

CREATE USER product_user WITH PASSWORD 'product_password';
CREATE DATABASE product_db OWNER product_user;
GRANT ALL PRIVILEGES ON DATABASE product_db TO product_user;

CREATE USER order_user WITH PASSWORD 'order_password';
CREATE DATABASE order_db OWNER order_user;
GRANT ALL PRIVILEGES ON DATABASE order_db TO order_user;

CREATE USER inventory_user WITH PASSWORD 'inventory_password';
CREATE DATABASE inventory_db OWNER inventory_user;
GRANT ALL PRIVILEGES ON DATABASE inventory_db TO inventory_user;