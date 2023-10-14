DROP TABLE IF EXISTS `ba_member`;

CREATE TABLE `ba_member` (
                             `member_no`	BIGINT	NOT NULL		AUTO_INCREMENT PRIMARY KEY,
                             `member_email`	VARCHAR(30)	NOT NULL	UNIQUE,
                             `member_pwd`	VARCHAR(100)	NOT NULL,
                             `member_name`	VARCHAR(20)	NOT NULL,
                             `member_zipcode`	VARCHAR(10)	NOT NULL,
                             `member_addr`	VARCHAR(255)	NOT NULL,
                             `member_detail_addr`	VARCHAR(255)	NOT NULL,
                             `member_phone`	VARCHAR(20)	NOT NULL,
                             `member_bank`	VARCHAR(10)	NULL,
                             `member_account`	VARCHAR(30)	NULL,
                             `member_category`	VARCHAR(1)	NULL	DEFAULT 'M'	COMMENT 'M:일반회원,  S:판매자회원, A:관리자',
                             `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `member_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:일반회원, N:탈퇴회원'
);

DROP TABLE IF EXISTS `ba_coupon`;

CREATE TABLE `ba_coupon` (
                             `coupon_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                             `store_no`	BIGINT	NOT NULL,
                             `member_no`	BIGINT	NOT NULL,
                             `coupon_name`	VARCHAR(30)	NOT NULL,
                             `coupon_content`	VARCHAR(255)	NOT NULL,
                             `min_buy_price`	INT	NOT NULL,
                             `discount_per`	INT	NOT NULL,
                             `discount_price`	INT	NOT NULL,
                             `extinction_date`	DATETIME	NULL,
                             `create_date`	DATETIME	NULL,
                             `modify_date`	DATETIME	NULL,
                             `coupon_status`	VARCHAR(1)	NOT NULL	DEFAULT 'Y'	COMMENT 'Y:사용가능 N:사용불가능'
);

DROP TABLE IF EXISTS `ba_dibs`;

CREATE TABLE `ba_dibs` (
                           `member_no`	BIGINT	NOT NULL	,
                           `store_no`	BIGINT	NOT NULL	,
                           `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                           `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                           `dibs_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:찜 N:찜취소'
);

DROP TABLE IF EXISTS `ba_store`;

CREATE TABLE `ba_store` (
                            `store_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                            `member_no`	BIGINT	NOT NULL	,
                            `market_name`	VARCHAR(20)	NOT NULL,
                            `store_name`	VARCHAR(30)	NOT NULL,
                            `license_no`	VARCHAR(10)	NOT NULL,
                            `store_zipcode`	VARCHAR(10)	NOT NULL,
                            `store_addr`	VARCHAR(255)	NOT NULL,
                            `store_detail_addr`	VARCHAR(255)	NOT NULL,
                            `weekday_start_time`	TIME	NOT NULL,
                            `weekday_end_time`	TIME	NOT NULL,
                            `weekend_start_time`	TIME	NOT NULL,
                            `weekend_end_time`	TIME	NOT NULL,
                            `unsupported_type`	VARCHAR(1)	NULL	COMMENT 'Q:퀵 배송, T:포장',
                            `store_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:운영중 , N:폐업'
);

DROP TABLE IF EXISTS `ba_item`;

CREATE TABLE `ba_item` (
                           `item_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                           `item_code`	VARCHAR(10)	NOT NULL	COMMENT 'S:생선/횟감, F:생선/비횟감,  C:갑각류,  M:패류  E:기타',
                           `item_name`	VARCHAR(30)	NOT NULL,
                           `item_size`	VARCHAR(1)	NOT NULL	COMMENT 'S:소, M:중, L:대',
                           `origin_status`	VARCHAR(1)	NOT NULL	COMMENT 'D:국내산,  I:수입산',
                           `origin_place_status`	VARCHAR(2)	NOT NULL	COMMENT '동해:ES, 서해:WS,남해:SS, 제주:JJ, 완도:WD, 일본:JP, 중국:CN, 러시아:RU, 노르웨이:NW',
                           `wild_farm_status`	VARCHAR(1)	NOT NULL	COMMENT 'W:자연산, F:양식',
                           `item_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y,  N'
);

DROP TABLE IF EXISTS `ba_menu`;

CREATE TABLE `ba_menu` (
                           `menu_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                           `store_no`	BIGINT	NOT NULL	,
                           `menu_name`	VARCHAR(30)	NOT NULL,
                           `menu_size`	VARCHAR(1)	NOT NULL	COMMENT 'S:소, M:중, L:대',
                           `menu_price`	INT	NOT NULL,
                           `menu_content`	VARCHAR(255)	NULL,
                           `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                           `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                           `menu_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);

DROP TABLE IF EXISTS `ba_review`;

CREATE TABLE `ba_review` (
                             `review_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                             `store_no`	BIGINT	NOT NULL	,
                             `member_no`	BIGINT	NOT NULL	,
                             `review_content`	MEDIUMTEXT	NOT NULL,
                             `review_freshness`	VARCHAR(1)	NOT NULL	DEFAULT 'M'	COMMENT 'L:낮음, M:중간, H:높음',
                             `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `review_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);

DROP TABLE IF EXISTS `ba_answer`;

CREATE TABLE `ba_answer` (
                             `answer_no`	BIGINT	NOT NULL    AUTO_INCREMENT PRIMARY KEY,
                             `review_no`	BIGINT	NOT NULL	,
                             `member_no`	BIGINT	NOT NULL	,
                             `answer_content`	MEDIUMTEXT	NOT NULL,
                             `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `answer_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);

DROP TABLE IF EXISTS `ba_attach`;

CREATE TABLE `ba_attach` (
                             `file_no`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `member_no`	BIGINT	NULL	,
                             `store_no`	BIGINT	NULL	,
                             `item_no`	BIGINT	NULL	,
                             `menu_no`	BIGINT	NULL	,
                             `review_no`	BIGINT	NULL	,
                             `notice_no`	BIGINT	NULL	,
                             `file_path`	VARCHAR(255)	NOT NULL,
                             `origin_filename`	VARCHAR(255)	NOT NULL,
                             `save_filename`	VARCHAR(255)	NOT NULL,
                             `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW()	COMMENT '삭제 처리시 수정일시 등록',
                             `file_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:사용 중, N:삭제 건'
);

DROP TABLE IF EXISTS `ba_store_item_daily_price`;

CREATE TABLE `ba_store_item_daily_price` (
                                             `daily_price_no`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                             `item_no`	BIGINT	NOT NULL	,
                                             `store_no`	BIGINT	NOT NULL	,
                                             `daily_price`	INT	NOT NULL	COMMENT 'KG상관없이 가격',
                                             `daliy_price_date`	DATE	NULL	DEFAULT (current_date)
);

DROP TABLE IF EXISTS `ba_pay`;

CREATE TABLE `ba_pay` (
                          `pay_no`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `pay_type`	VARCHAR(1)	NOT NULL,
                          `pay_price`	INT	NOT NULL,
                          `pay_datetime`	DATETIME	NULL	DEFAULT NOW(),
                          `pay_cancel_datetime`	DATETIME	NULL	DEFAULT NOW(),
                          `pay_status`	VARCHAR(1)	NOT NULL	DEFAULT 'Y'	COMMENT 'Y:결제완료, N:결제취소',
                          `order_no`	BIGINT	NOT NULL
);

DROP TABLE IF EXISTS `ba_notice`;

CREATE TABLE `ba_notice` (
                             `notice_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                             `notice_title`	VARCHAR(255)	NOT NULL,
                             `notice_content`	VARCHAR(255)	NOT NULL,
                             `member_no`	BIGINT	NOT NULL	,
                             `reg_datetiem`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `notice_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);

DROP TABLE IF EXISTS `ba_order`;

CREATE TABLE `ba_order` (
                            `order_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                            `order_type`	VARCHAR(1)	NOT NULL	COMMENT 'Q:퀵 배송, T:포장',
                            `order_price`	INT	NOT NULL,
                            `order_request`	VARCHAR(255)	NULL,
                            `recipient_phone`	VARCHAR(20)	NULL,
                            `recipient_name`	VARCHAR(20)	NULL,
                            `recipient_zipcode`	VARCHAR(10)	NULL,
                            `recipient_addr`	VARCHAR(255)	NULL,
                            `recipient_detail_addr`	VARCHAR(255)	NULL,
                            `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                            `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                            `order_status`	VARCHAR(255)	NULL	DEFAULT 'Y'
);

DROP TABLE IF EXISTS `ba_order_menu`;

CREATE TABLE `ba_order_menu` (
                                 `order_menu_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                                 `member_no`	BIGINT	NOT NULL	,
                                 `menu_no`	BIGINT	NOT NULL	,
                                 `order_no`	BIGINT	NOT NULL	,
                                 `order_menu_count`	INT	NOT NULL,
                                 `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                                 `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                                 `order_menu_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);


ALTER TABLE `ba_coupon` ADD CONSTRAINT `FK_ba_store_TO_ba_coupon_1` FOREIGN KEY (
                                                                                 `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_coupon` ADD CONSTRAINT `FK_ba_member_TO_ba_coupon_1` FOREIGN KEY (
                                                                                  `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_dibs` ADD CONSTRAINT `FK_ba_member_TO_ba_dibs_1` FOREIGN KEY (
                                                                              `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_dibs` ADD CONSTRAINT `FK_ba_store_TO_ba_dibs_1` FOREIGN KEY (
                                                                             `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_store` ADD CONSTRAINT `FK_ba_member_TO_ba_store_1` FOREIGN KEY (
                                                                                `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_menu` ADD CONSTRAINT `FK_ba_store_TO_ba_menu_1` FOREIGN KEY (
                                                                             `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_review` ADD CONSTRAINT `FK_ba_store_TO_ba_review_1` FOREIGN KEY (
                                                                                 `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_review` ADD CONSTRAINT `FK_ba_member_TO_ba_review_1` FOREIGN KEY (
                                                                                  `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_answer` ADD CONSTRAINT `FK_ba_review_TO_ba_answer_1` FOREIGN KEY (
                                                                                  `review_no`
    )
    REFERENCES `ba_review` (
                            `review_no`
        );

ALTER TABLE `ba_answer` ADD CONSTRAINT `FK_ba_member_TO_ba_answer_1` FOREIGN KEY (
                                                                                  `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_attach` ADD CONSTRAINT `FK_ba_member_TO_ba_attach_1` FOREIGN KEY (
                                                                                  `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_attach` ADD CONSTRAINT `FK_ba_store_TO_ba_attach_1` FOREIGN KEY (
                                                                                 `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_attach` ADD CONSTRAINT `FK_ba_item_TO_ba_attach_1` FOREIGN KEY (
                                                                                `item_no`
    )
    REFERENCES `ba_item` (
                          `item_no`
        );

ALTER TABLE `ba_attach` ADD CONSTRAINT `FK_ba_menu_TO_ba_attach_1` FOREIGN KEY (
                                                                                `menu_no`
    )
    REFERENCES `ba_menu` (
                          `menu_no`
        );

ALTER TABLE `ba_attach` ADD CONSTRAINT `FK_ba_review_TO_ba_attach_1` FOREIGN KEY (
                                                                                  `review_no`
    )
    REFERENCES `ba_review` (
                            `review_no`
        );

ALTER TABLE `ba_attach` ADD CONSTRAINT `FK_ba_notice_TO_ba_attach_1` FOREIGN KEY (
                                                                                  `notice_no`
    )
    REFERENCES `ba_notice` (
                            `notice_no`
        );

ALTER TABLE `ba_store_item_daily_price` ADD CONSTRAINT `FK_ba_item_TO_ba_store_item_daily_price_1` FOREIGN KEY (
                                                                                                                `item_no`
    )
    REFERENCES `ba_item` (
                          `item_no`
        );

ALTER TABLE `ba_store_item_daily_price` ADD CONSTRAINT `FK_ba_store_TO_ba_store_item_daily_price_1` FOREIGN KEY (
                                                                                                                 `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_pay` ADD CONSTRAINT `FK_ba_order_TO_ba_pay_1` FOREIGN KEY (
                                                                           `order_no`
    )
    REFERENCES `ba_order` (
                           `order_no`
        );

ALTER TABLE `ba_notice` ADD CONSTRAINT `FK_ba_member_TO_ba_notice_1` FOREIGN KEY (
                                                                                  `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_order_menu` ADD CONSTRAINT `FK_ba_member_TO_ba_order_menu_1` FOREIGN KEY (
                                                                                          `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_order_menu` ADD CONSTRAINT `FK_ba_menu_TO_ba_order_menu_1` FOREIGN KEY (
                                                                                        `menu_no`
    )
    REFERENCES `ba_menu` (
                          `menu_no`
        );

ALTER TABLE `ba_order_menu` ADD CONSTRAINT `FK_ba_order_TO_ba_order_menu_1` FOREIGN KEY (
                                                                                         `order_no`
    )
    REFERENCES `ba_order` (
                           `order_no`
        );

