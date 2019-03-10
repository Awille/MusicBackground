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

 Date: 10/03/2019 21:17:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  `time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `like` int(255) NULL DEFAULT 0,
  `dislike` int(255) NULL DEFAULT 0,
  `reply_comment_id` int(11) NULL DEFAULT 0,
  `comment_level` int(11) NULL DEFAULT NULL,
  `reply_amount` int(255) NULL DEFAULT 0,
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `songIndex`(`song_id`) USING BTREE,
  INDEX `user_constraint`(`user_id`) USING BTREE,
  CONSTRAINT `song_constraint` FOREIGN KEY (`song_id`) REFERENCES `song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_constraint` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Triggers structure for table comment
-- ----------------------------
DROP TRIGGER IF EXISTS `update_time`;
delimiter ;;
CREATE TRIGGER `update_time` BEFORE INSERT ON `comment` FOR EACH ROW BEGIN
	SET NEW.time = NOW();
	SET NEW.`like` = 0;
	SET NEW.dislike = 0;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
