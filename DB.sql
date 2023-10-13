DROP DATABASE IF EXISTS `JAM`;
CREATE DATABASE JAM;
USE JAM;

CREATE TABLE article(
    id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title VARCHAR(100) NOT NULL,
    `body` TEXT NOT NULL
);

SHOW TABLES;
DESC article;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = CONCAT('제목', RAND()),
`body` = CONCAT('내용', RAND());


UPDATE article
SET updateDate =NOW(),
title = CONCAT('제목', RAND()),
`body` = CONCAT('내용', RAND())
WHERE id = 6;

CREATE TABLE `member`(
    id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId VARCHAR(100) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    `name` VARCHAR(100) NOT NULL
);

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'test 1',
loginPw = 'test 1',
`name` = 'test 1';


SELECT *
FROM `member`

-- INSERT INTO article
-- SET regDate = NOW(),
-- updateDate = NOW(),
-- title = CONCAT('제목', ROUND(RAND() * 100)),
-- `body` = CONCAT('내용', ROUND(RAND() * 100));