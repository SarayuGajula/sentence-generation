CREATE TABLE sources (
    id int IDENTITY(1,1) AUTO_INCREMENT PRIMARY KEY,
    filename varchar(255) NOT NULL,
    word_count int NOT NULL,
    imported_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE words (
    word varchar(255) PRIMARY KEY,
    general_count int NOT NULL,
    start_count int NOT NULL,
    end_count int NOT NULL,
);

CREATE TABLE bigrams (
    first_word_id int NOT NULL,
    second_word_id int NOT NULL,
    count int NOT NULL,
    PRIMARY KEY (first_word_id, second_word_id),
    FOREIGN KEY (first_word_id) REFERENCES words(id),
    FOREIGN KEY (second_word_id) REFERENCES words(id)
);