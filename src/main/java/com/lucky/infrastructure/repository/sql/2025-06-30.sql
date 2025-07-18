CREATE TABLE `lucky-star`.`balance_log`  (
  `id` bigint(0) NOT NULL,
  `wechat_user_id` bigint(0) NULL DEFAULT NULL COMMENT '微信用户id',
  `money` decimal(20, 0) NULL DEFAULT NULL COMMENT '金额',
  `operator_id` bigint(0) NULL DEFAULT NULL COMMENT '操作人id',
  `operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT = '余额操作日志';


CREATE TABLE `lucky-star`.`no_counter`  (
  `id` bigint(0) NOT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编号类型',
  `value` int(0) NULL DEFAULT NULL COMMENT '数值',
  `belong_day` date NULL DEFAULT NULL COMMENT '所属天',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT = '编号计数器';

ALTER TABLE `lucky-star`.`wechat_user` ADD COLUMN `owner_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人id' ;



CREATE TABLE `banner` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT ' banner名称',
  `image` varchar(255) DEFAULT NULL COMMENT ' banner图片',
  `enabled` tinyint(1) DEFAULT '0' COMMENT '启用禁用',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)COMMENT = 'banner';

ALTER TABLE `lucky-star`.`banner` ADD COLUMN `sort` int(20) NULL DEFAULT '0' COMMENT '排序' ;

ALTER TABLE `lucky-star`.`logistics_order` ADD COLUMN `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间' ;

ALTER TABLE `lucky-star`.`logistics_order` ADD COLUMN `complete_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间' ;


ALTER TABLE `lucky-star`.`pay_order` ADD COLUMN `business_params` json DEFAULT NULL COMMENT '业务参数' ;