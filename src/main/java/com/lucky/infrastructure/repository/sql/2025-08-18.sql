SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `lucky-star`.`grade` ADD COLUMN `editable` int(0) NULL DEFAULT 0 COMMENT '是否可修改';

ALTER TABLE `lucky-star`.`prize_info` ADD COLUMN `replace_prize_id` bigint(0) NULL DEFAULT NULL COMMENT '替换商品id';

ALTER TABLE `lucky-star`.`series_topic` ADD COLUMN `sort` int(10) NULL DEFAULT 1 NULL COMMENT '排序';

SET FOREIGN_KEY_CHECKS=1;