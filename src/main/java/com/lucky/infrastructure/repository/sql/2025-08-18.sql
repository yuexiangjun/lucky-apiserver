SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `lucky-star`.`grade` ADD COLUMN `editable` int(0) NULL DEFAULT 0 COMMENT '是否可修改';

ALTER TABLE `lucky-star`.`prize_info` ADD COLUMN `replace_prize_id` bigint(0) NULL DEFAULT NULL COMMENT '替换商品id';

SET FOREIGN_KEY_CHECKS=1;