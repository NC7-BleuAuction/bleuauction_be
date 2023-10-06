CREATE TABLE `ba_member` (
    `member_no` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `member_email` VARCHAR(30) NOT NULL UNIQUE,
    `member_pwd` VARCHAR(100) NOT NULL,
    `member_name` VARCHAR(20) NOT NULL,
    `member_zipcode` VARCHAR(10) NOT NULL,
    `member_addr` VARCHAR(255) NOT NULL,
    `member_detail_addr` VARCHAR(255) NOT NULL,
    `member_phone` VARCHAR(20) NOT NULL,
    `member_bank` VARCHAR(10) NULL,
    `member_account` VARCHAR(30) NULL,
    `member_category` VARCHAR(1) DEFAULT 'M' COMMENT 'M:일반회원, S:판매자회원, A:관리자',
    `reg_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `mdf_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `member_status` VARCHAR(1) DEFAULT 'Y' COMMENT 'Y:일반회원, N:탈퇴회원'
);
