CREATE TABLE IF NOT EXISTS "author_book" (
    orcid_id VARCHAR(16),
    isbn_id VARCHAR(13),
    PRIMARY KEY (orcid_id, isbn_id),
    CONSTRAINT fjaslkdfjaksldfa FOREIGN KEY (orcid_id) REFERENCES postgres.public.author(orcid_id),
    CONSTRAINT ajdfklajsdflkasa FOREIGN KEY (isbn_id) REFERENCES postgres.public.book(isbn_id)
);