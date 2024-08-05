# -- Create user table
# CREATE TABLE IF NOT EXISTS user (
#                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
#                                     username VARCHAR(255) UNIQUE NOT NULL,
#                                     password VARCHAR(255) NOT NULL,
#                                     email VARCHAR(255),
#                                     role VARCHAR(255),
#                                     enabled BOOLEAN DEFAULT FALSE
# );
#
# -- Create verification_token table
# CREATE TABLE IF NOT EXISTS verification_token (
#                                                   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
#                                                   token VARCHAR(255) NOT NULL,
#                                                   expiry_date TIMESTAMP NOT NULL,
#                                                   user_id BIGINT,
#                                                   CONSTRAINT fk_user
#                                                       FOREIGN KEY (user_id)
#                                                           REFERENCES user(id)
#                                                           ON DELETE CASCADE
# );
#
# -- Create user_stock table
# CREATE TABLE IF NOT EXISTS user_stock (
#                                           id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
#                                           user_id BIGINT NOT NULL,
#                                           symbol VARCHAR(255) NOT NULL,
#                                           CONSTRAINT fk_user_stock_user
#                                               FOREIGN KEY (user_id)
#                                                   REFERENCES user(id)
#                                                   ON DELETE CASCADE,
#                                           UNIQUE (user_id, symbol)
# );
