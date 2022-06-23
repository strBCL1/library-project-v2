CREATE TABLE IF NOT EXISTS "publisher" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(45) NULL,
    address VARCHAR(255) NULL,
    city VARCHAR(45) NULL,
    country VARCHAR(45) NULL
);

ALTER TABLE "book"
    ADD COLUMN publisher_id BIGINT;

ALTER TABLE "book"
    ADD CONSTRAINT jklasdjfklasdfa FOREIGN KEY (publisher_id) REFERENCES postgres.public.publisher(id);