/*
 Navicat Premium Data Transfer

 Source Server         : Awille
 Source Server Type    : MySQL
 Source Server Version : 100138
 Source Host           : localhost:3306
 Source Schema         : music

 Target Server Type    : MySQL
 Target Server Version : 100138
 File Encoding         : 65001

 Date: 10/03/2019 21:16:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for songlist
-- ----------------------------
DROP TABLE IF EXISTS `songlist`;
CREATE TABLE `songlist`  (
  `song_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`song_list_id`) USING BTREE,
  UNIQUE INDEX `song_list_unique`(`user_id`, `name`) USING BTREE,
  INDEX `userId`(`user_id`) USING BTREE,
  CONSTRAINT `userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
