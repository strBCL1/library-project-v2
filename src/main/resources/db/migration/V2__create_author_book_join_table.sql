CREATE TABLE IF NOT EXISTS "author_book" (
    author_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    PRIMARY KEY (author_id, book_id),
    CONSTRAINT fjaslkdfjaksldfa FOREIGN KEY (author_id) REFERENCES postgres.public.author(id),
    CONSTRAINT ajdfklajsdflkasa FOREIGN KEY (book_id) REFERENCES postgres.public.book(id)
);