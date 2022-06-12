INSERT INTO postgres.public.author(orcid_id, first_name, last_name)
VALUES ('1111111111111111', 'Dan', 'Brown'), ('2222222222222222', 'Neil', 'Gaiman'), ('3333333333333333', 'J. K.', 'Rowling');

INSERT INTO postgres.public.book(isbn_id, title)
VALUES ('1111111111111', 'Wild Symphony'), ('2222222222222', 'Origin'), ('3333333333333', 'American Gods'), ('4444444444444', 'Anansi Boys'), ('5555555555555', 'Harry Potter and the Goblet of Fire'), ('6666666666666', 'Harry Potter and the Deathly Hallows');

INSERT INTO postgres.public.author_book(orcid_id, isbn_id)
VALUES ('1111111111111111', '1111111111111'), ('1111111111111111', '2222222222222'), ('2222222222222222', '3333333333333'), ('2222222222222222', '4444444444444'), ('3333333333333333', '5555555555555'), ('3333333333333333', '6666666666666');