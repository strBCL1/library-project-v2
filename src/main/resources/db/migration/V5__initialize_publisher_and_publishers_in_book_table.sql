INSERT INTO postgres.public.publisher(name, address, city, country)
VALUES ('PenguinRandomHouse', 'Neumarker Strasse 28', 'Munich', 'Germany'), ('Arnoldo Mondadori Editore', 'Mondadori 1', 'Milan', 'Italy'), ('HarperCollins', '195 Broadway', 'New York', 'USA');

UPDATE "book"
SET publisher_id='1'
WHERE isbn_id IN ('1111111111111', '2222222222222');

UPDATE "book"
SET publisher_id='2'
WHERE isbn_id IN ('3333333333333', '4444444444444');

UPDATE "book"
SET publisher_id='3'
WHERE isbn_id IN ('5555555555555', '6666666666666');