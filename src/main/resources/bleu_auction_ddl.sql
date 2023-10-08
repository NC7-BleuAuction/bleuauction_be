CREATE TABLE `ba_member` (
                             `member_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
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

CREATE TABLE `ba_coupon` (
                             `coupon_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                             `store_no`	BIGINT	NOT NULL	,
                             `member_no`	BIGINT	NOT NULL	,
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

CREATE TABLE `ba_dibs` (
                           `member_no`	BIGINT	NOT NULL	,
                           `store_no`	BIGINT	NOT NULL	,
                           `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                           `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                           `dibs_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:찜 N:찜취소'
);


CREATE TABLE `ba_store` (
                            `store_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                            `member_no`	BIGINT	NOT NULL	,
                            `market_name`	VARCHAR(20)	NULL,
                            `store_name`	VARCHAR(30)	NOT NULL,
                            `license_no`	VARCHAR(10)	NOT NULL,
                            `store_zipcode`	VARCHAR(10)	NOT NULL,
                            `store_addr`	VARCHAR(255)	NOT NULL,
                            `store_detail_addr`	VARCHAR(255)	NOT NULL,
                            `weekday_start_time`	TIME	NULL,
                            `weekday_end_time`	TIME	NULL,
                            `weekend_start_time`	TIME	NULL,
                            `weekend_end_time`	TIME	NULL,
                            `unsupported_type`	VARCHAR(1)	NULL	COMMENT 'Q:퀵 배송, T:포장',
                            `store_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:운영중 , N:폐업'
);


CREATE TABLE `ba_item` (
                           `item_no` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `item_code` varchar(10) NOT NULL COMMENT 'S:생선/횟감, F:생선/비횟감,  C:갑각류,  M:패류  E:기타',
                           `item_name` varchar(30) NOT NULL,
                           `item_size` varchar(1) NOT NULL COMMENT 'S:소, M:중, L:대',
                           `origin_status` varchar(1) NOT NULL COMMENT 'D:국내산,  I:수입산',
                           `origin_place_status` varchar(2) NOT NULL COMMENT '동해:ES, 서해:WS,남해:SS, 제주:JJ, 완도:WD, 일본:JP, 중국:CN, 러시아:RU, 노르웨이:NW',
                           `wild_farm_status` varchar(1) NOT NULL COMMENT 'W:자연산, F:양식',
                           `item_status` varchar(1) DEFAULT 'Y' COMMENT 'Y,  N'
);


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


CREATE TABLE `ba_answer` (
                             `answer_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                             `review_no`	BIGINT	NOT NULL	,
                             `member_no`	BIGINT	NOT NULL	,
                             `answer_content`	MEDIUMTEXT	NOT NULL,
                             `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `answer_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);


CREATE TABLE `ba_attach` (
                             `file_no`	BIGINT	NOT NULL 	AUTO_INCREMENT PRIMARY KEY,
                             `store_no`	BIGINT	NULL	,
                             `item_no`	BIGINT	NULL	,
                             `menu_no`	BIGINT	NULL	,
                             `review_no`	BIGINT	NULL	,
                             `file_path`	VARCHAR(255)	NOT NULL,
                             `origin_filename`	VARCHAR(255)	NOT NULL,
                             `save_filename`	VARCHAR(255)	NOT NULL,
                             `reg_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `mdf_datetime`	DATETIME	NULL	DEFAULT NOW(),
                             `file_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:사용 중, N:삭제 건'
);


CREATE TABLE `ba_store_item_daily_price` (
                                             `daily_price_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                                             `item_no`	BIGINT	NOT NULL	,
                                             `store_no`	BIGINT	NOT NULL	,
                                             `daily_price`	INT	NOT NULL	COMMENT 'KG상관없이 가격',
                                             `daliy_price_date`	DATE	NULL	DEFAULT (current_date)
);


CREATE TABLE `ba_pay` (
                          `pay_no`	BIGINT	NOT NULL	AUTO_INCREMENT PRIMARY KEY,
                          `order_no`	BIGINT	NOT NULL	,
                          `pay_type`	VARCHAR(1)	NOT NULL,
                          `pay_price`	INT	NOT NULL,
                          `pay_datetime`	DATETIME	NULL	DEFAULT NOW(),
                          `pay_cancel_datetime`	DATETIME	NULL	DEFAULT NOW(),
                          `pay_status`	VARCHAR(1)	NOT NULL	DEFAULT 'Y'	COMMENT 'Y:결제완료, N:결제취소'
);


CREATE TABLE `ba_notice` (
                             `notice_no`	BIGINT	NOT NULL		AUTO_INCREMENT PRIMARY KEY,
                             `notice_title`	VARCHAR(255)	NOT NULL,
                             `notice_content`	VARCHAR(255)	NOT NULL,
                             `member_no`	BIGINT	NOT NULL	,
                             `reg_datetime`	VARCHAR(255)	NULL,
                             `mdf_datetime`	VARCHAR(255)	NULL,
                             `notice_status`	VARCHAR(1)	NULL	DEFAULT 'Y'
);


CREATE TABLE `ba_order` (
                            `order_no`	BIGINT	NOT NULL		AUTO_INCREMENT PRIMARY KEY,
                            `store_no`	BIGINT	NOT NULL	,
                            `member_no`	BIGINT	NOT NULL	,
                            `menu_no`	BIGINT	NOT NULL	,
                            `coupon_no`	BIGINT	NULL	,
                            `order_price`	INT	NOT NULL,
                            `order_count`	INT	NOT NULL,
                            `order_request`	VARCHAR(255)	NULL,
                            `recipient_phone`	VARCHAR(30)	NOT NULL,
                            `recipient_name`	VARCHAR(30)	NOT NULL,
                            `recipient_zipcode`	VARCHAR(10)	NOT NULL,
                            `recipient_addr`	VARCHAR(255)	NOT NULL,
                            `recipient_detail_addr`	VARCHAR(255)	NOT NULL,
                            `order_datetime`	DATETIME	NULL	DEFAULT NOW(),
                            `order_cancel_datetime`	DATETIME	NULL	DEFAULT NOW(),
                            `order_status`	VARCHAR(1)	NULL	DEFAULT 'Y'	COMMENT 'Y:주문완료, N:주문취소'
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

ALTER TABLE `ba_order` ADD CONSTRAINT `FK_ba_store_TO_ba_order_1` FOREIGN KEY (
                                                                               `store_no`
    )
    REFERENCES `ba_store` (
                           `store_no`
        );

ALTER TABLE `ba_order` ADD CONSTRAINT `FK_ba_member_TO_ba_order_1` FOREIGN KEY (
                                                                                `member_no`
    )
    REFERENCES `ba_member` (
                            `member_no`
        );

ALTER TABLE `ba_order` ADD CONSTRAINT `FK_ba_menu_TO_ba_order_1` FOREIGN KEY (
                                                                              `menu_no`
    )
    REFERENCES `ba_menu` (
                          `menu_no`
        );

ALTER TABLE `ba_order` ADD CONSTRAINT `FK_ba_coupon_TO_ba_order_1` FOREIGN KEY (
                                                                                `coupon_no`
    )
    REFERENCES `ba_coupon` (
                            `coupon_no`
        );

