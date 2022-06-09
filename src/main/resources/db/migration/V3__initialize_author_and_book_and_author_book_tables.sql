INSERT INTO postgres.public.author(first_name, last_name) VALUES ('Dan', 'Brown'), ('Neil', 'Gaiman'), ('J. K.', 'Rowling');

INSERT INTO postgres.public.book(title) VALUES ('Wild Symphony'), ('Origin'), ('American Gods'), ('Anansi Boys'), ('Harry Potter and the Goblet of Fire'), ('Harry Potter and the Deathly Hallows');

INSERT INTO postgres.public.author_book(author_id, book_id) VALUES ('1', '1'), ('1', '2'), ('2', '3'), ('2', '4'), ('3', '5'), ('3', '6');