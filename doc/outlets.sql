/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.100
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : 192.168.1.100:3306
 Source Schema         : outlets

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 23/05/2022 15:48:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth
-- ----------------------------
DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth` (
  `id` bigint NOT NULL COMMENT '认证ID',
  `dtid` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数字身份',
  `id_card` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证',
  `name` varchar(12) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='认证表';

-- ----------------------------
-- Table structure for clock_in_user
-- ----------------------------
DROP TABLE IF EXISTS `clock_in_user`;
CREATE TABLE `clock_in_user` (
  `id` bigint NOT NULL,
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名（账户昵称）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `gender` tinyint DEFAULT NULL COMMENT '性别（0:未知 1:男，2:女）',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `birthday` date DEFAULT NULL COMMENT '生日 格式为年/月/',
  `nick_name` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `open_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信小程序opendId',
  `contact_address` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
  `auth_id` bigint DEFAULT NULL COMMENT '认证ID，关联认证表的主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='打卡用户表';

-- ----------------------------
-- Table structure for gift_voucher
-- ----------------------------
DROP TABLE IF EXISTS `gift_voucher`;
CREATE TABLE `gift_voucher` (
  `id` bigint NOT NULL,
  `gift_voucher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '礼品券id',
  `gift_voucher_type` int DEFAULT NULL COMMENT '礼品券类型（1:实物兑换券，2:消费优惠券）',
  `gift_voucher_qrcode` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '礼品券二维码',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `gift_voucher_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '礼品券名称',
  `exchange_deadline` bigint DEFAULT NULL COMMENT '兑换有效期（截止日期时间戳）',
  `gift_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '礼品包名',
  `gift_id` bigint DEFAULT NULL COMMENT '礼品包id',
  `exchange_instructions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '兑换说明',
  `state` int DEFAULT NULL COMMENT '状态(0:未核销，1:已核销)',
  `exchange_time` bigint DEFAULT NULL COMMENT '核销时间',
  `exchange_user_id` bigint DEFAULT NULL COMMENT '核销人id',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='礼品券表';

-- ----------------------------
-- Table structure for operator
-- ----------------------------
DROP TABLE IF EXISTS `operator`;
CREATE TABLE `operator` (
  `id` bigint NOT NULL,
  `account` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户账号（账号昵称）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='运营人员表[pc端的管理人员表]\n';

-- ----------------------------
-- Table structure for punch_log
-- ----------------------------
DROP TABLE IF EXISTS `punch_log`;
CREATE TABLE `punch_log` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `destination_id` bigint DEFAULT NULL COMMENT '目的地id',
  `destination_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目的地名称',
  `punch_time` bigint DEFAULT NULL COMMENT '打卡时间',
  `punch_longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经度',
  `punch_latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '纬度',
  `integral_value` int DEFAULT NULL COMMENT '积分值',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  `address` text COLLATE utf8mb4_general_ci COMMENT '目的地地址',
  `destination_recommend_square_image` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目的地推荐图片（列表页正方形缩略图）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='打卡日志\n';

-- ----------------------------
-- Table structure for t_log_record
-- ----------------------------
DROP TABLE IF EXISTS `t_log_record`;
CREATE TABLE `t_log_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant` varchar(100) NOT NULL DEFAULT '' COMMENT '租户标识',
  `type` varchar(100) NOT NULL DEFAULT '' COMMENT '日志业务标识',
  `biz_no` varchar(100) NOT NULL DEFAULT '' COMMENT '业务businessNo',
  `operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人',
  `action` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '动作',
  `fail` varchar(200) NOT NULL COMMENT '失败',
  `sub_type` varchar(100) NOT NULL DEFAULT '' COMMENT '种类',
  `extra` varchar(2000) NOT NULL DEFAULT '' COMMENT '修改的详细信息，可以为json',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_biz_key` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';

-- ----------------------------
-- Table structure for write_off_user
-- ----------------------------
DROP TABLE IF EXISTS `write_off_user`;
CREATE TABLE `write_off_user` (
  `id` bigint NOT NULL,
  `account` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户账号',
  `password` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `phone` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `gender` tinyint DEFAULT NULL COMMENT '性别（0:未知 1:男 2:女）',
  `birthday` date DEFAULT NULL COMMENT '生日 格式为年/月/',
  `avatar` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `nick_name` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `contact_address` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
  `open_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信的openId',
  `auth_id` bigint DEFAULT NULL COMMENT '认证ID，关联认证表的主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='核销用户表';

SET FOREIGN_KEY_CHECKS = 1;
