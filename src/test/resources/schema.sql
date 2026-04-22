CREATE TABLE IF NOT EXISTS sources (
    id int AUTO_INCREMENT PRIMARY KEY,
    filename varchar(255) NOT NULL,
    word_count int NOT NULL,
    imported_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS words (
    word varchar(255) PRIMARY KEY,
    total_count int NOT NULL,
    start_count int NOT NULL,
    end_count int NOT NULL,
    uppercase_count int NOT NULL,
    title_count int NOT NULL
);

CREATE TABLE IF NOT EXISTS bigrams (
    first_word varchar(255) NOT NULL,
    second_word varchar(255) NOT NULL,
    count int NOT NULL,
    PRIMARY KEY (first_word, second_word)
);

CREATE TABLE IF NOT EXISTS generated_sentences (
    id int AUTO_INCREMENT PRIMARY KEY,
    sentence TEXT(65535) NOT NULL,
    algorithm varchar(255) NOT NULL,
    generated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
);