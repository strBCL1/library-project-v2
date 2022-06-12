CREATE TABLE IF NOT EXISTS "author" (
    orcid_id VARCHAR(16) PRIMARY KEY,
    first_name VARCHAR(45) NULL,
    last_name VARCHAR(45) NULL
);

CREATE TABLE IF NOT EXISTS "book" (
    isbn_id VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NULL
);