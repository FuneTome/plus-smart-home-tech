CREATE USER analyzer_user WITH PASSWORD 'analyzer_password';
CREATE DATABASE analyzer OWNER analyzer_user;
GRANT ALL PRIVILEGES ON DATABASE analyzer TO analyzer_user;