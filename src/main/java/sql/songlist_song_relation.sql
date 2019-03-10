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

 Date: 10/03/2019 21:16:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for songlist_song_relation
-- ----------------------------
DROP TABLE IF EXISTS `songlist_song_relation`;
CREATE TABLE `songlist_song_relation`  (
  `song_list_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  UNIQUE INDEX `relation`(`song_list_id`, `song_id`) USING BTREE,
  INDEX `song`(`song_id`) USING BTREE,
  CONSTRAINT `song` FOREIGN KEY (`song_id`) REFERENCES `song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userlist` FOREIGN KEY (`song_list_id`) REFERENCES `songlist` (`song_list_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
