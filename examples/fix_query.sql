DROP TABLE kerning_pairs;

CREATE TABLE kerning_pairs (
    id BIGINT NOT NULL,
    left_code_point INT NOT NULL,
    right_code_point INT NOT NULL,
    kerning_value INT NOT NULL,
    PRIMARY KEY (id)
);
